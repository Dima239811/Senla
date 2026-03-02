package bookstore.ui.actions.csv.order;

import bookstore.controller.DataTransferController;
import bookstore.exception.DataImportException;
import bookstore.exception.DataManagerException;
import bookstore.model.entity.Order;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class ImportOrderAction implements IAction {
    private final DataTransferController dataTransferController;
    private final Scanner scanner;
    private static final Logger logger = LoggerFactory.getLogger(ImportOrderAction.class);

    public ImportOrderAction(DataTransferController dataTransferController) {
        this.dataTransferController = dataTransferController;
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void execute() {
        logger.info("Запуск действия: импорт заказов в CSV.");
        try {
            System.out.println("\n=== Импорт заказов из CSV ===");
            System.out.print("Введите путь к файлу: ");
            String path = scanner.nextLine().trim();

            List<Order> importedOrders = dataTransferController.importOrdersFromCsv(path);
            System.out.printf("Успешно импортировано %d заказов\n", importedOrders.size());
            logger.info("Импорт заказов завершён успешно. Файл: {}", path);
        } catch (DataImportException e) {
            logger.error("Ошибка при импорте заказов: {}", e.getMessage());
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (DataManagerException e) {
            logger.error("Ошибка при добавлении заказов в базу: {}", e.getMessage());
            System.err.println("Ошибка при добавлении заказов в базу: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при импорте заказов {}", e.getMessage());
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
