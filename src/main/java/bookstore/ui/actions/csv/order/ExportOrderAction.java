package bookstore.ui.actions.csv.order;

import bookstore.controller.DataTransferController;
import bookstore.exception.DataExportException;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ExportOrderAction implements IAction {
    private final DataTransferController dataTransferController;
    private final Scanner scanner;
    private static final Logger logger = LoggerFactory.getLogger(ExportOrderAction.class);

    public ExportOrderAction(DataTransferController dataTransferController) {
        this.dataTransferController = dataTransferController;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute() {
        logger.info("Запуск действия: экспорт заказов в CSV.");
        try {
            System.out.println("\n=== Экспорт заказов в CSV ===");
            System.out.print("Введите путь для сохранения файла: ");
            String path = scanner.nextLine().trim();

            dataTransferController.exportOrdersToCsv(path);
            System.out.println("Экспорт успешно завершен");
            logger.info("Экспорт заказов завершён успешно. Файл: {}", path);
        } catch (DataExportException e) {
            logger.error("Ошибка при экспорте заказов: {}", e.getMessage());
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при экспорте заказов {}", e.getMessage());
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
