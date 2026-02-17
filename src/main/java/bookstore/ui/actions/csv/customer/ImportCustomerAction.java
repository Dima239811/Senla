package bookstore.ui.actions.csv.customer;

import bookstore.controller.DataTransferController;
import bookstore.exception.DataImportException;
import bookstore.model.entity.Customer;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class ImportCustomerAction implements IAction {
    private final DataTransferController dataTransferController;
    private final Scanner scanner;
    private static final Logger logger = LoggerFactory.getLogger(ImportCustomerAction.class);

    public ImportCustomerAction(DataTransferController dataTransferController) {
        this.dataTransferController = dataTransferController;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute() {
        logger.info("Запуск действия: импорт клиентов в CSV.");
        try {
            System.out.println("\n=== Импорт клиентов из CSV ===");
            System.out.print("Введите путь к файлу: ");
            String path = scanner.nextLine().trim();

            List<Customer> imported = dataTransferController.importCustomersFromCsv(path);
            System.out.printf("Успешно импортировано %d клиентов\n", imported.size());
            logger.info("Импорт клиентов завершён успешно. Файл: {}", path);
        } catch (DataImportException e) {
            logger.error("Ошибка при импорте клиентов: {}", e.getMessage());
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при импорте клиентов {}", e.getMessage());
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}

