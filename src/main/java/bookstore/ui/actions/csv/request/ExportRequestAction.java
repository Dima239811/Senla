package bookstore.ui.actions.csv.request;

import bookstore.exception.DataExportException;
import  bookstore.model.DataManager;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ExportRequestAction implements IAction {
    private final DataManager dataManager;
    private final Scanner scanner;
    private static final Logger logger = LoggerFactory.getLogger(ExportRequestAction.class);

    public ExportRequestAction(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute() {
        logger.info("Запуск действия: экспорт запросов в CSV.");
        try {
            System.out.println("\n=== Экспорт запросов на книги в CSV ===");
            System.out.print("Введите путь для сохранения файла: ");
            String path = scanner.nextLine().trim();

            dataManager.exportRequestToCsv(path);
            System.out.println("Экспорт успешно завершен");
            logger.info("Экспорт запросов завершён успешно. Файл: {}", path);
        } catch (DataExportException e) {
            logger.error("Ошибка при экспорте запросов: {}", e.getMessage());
            System.err.println("Ошибка экспорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при экспорте запросов {}", e.getMessage());
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
