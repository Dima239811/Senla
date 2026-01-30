package bookstore.ui.actions.csv.request;

import bookstore.exception.DataImportException;
import bookstore.exception.DataManagerException;
import bookstore.model.DataManager;
import bookstore.model.entity.RequestBook;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class ImportRequestAction implements IAction {
    private final DataManager dataManager;
    private final Scanner scanner;
    private static final Logger logger = LoggerFactory.getLogger(ImportRequestAction.class);

    public ImportRequestAction(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute() {
        logger.info("Запуск действия: импорт запросов в CSV.");
        try {
            System.out.println("\n=== Импорт запросов на книги из CSV ===");
            System.out.print("Введите путь к файлу: ");
            String path = scanner.nextLine().trim();

            List<RequestBook> imported = dataManager.importRequestFromCsv(path);
            System.out.printf("Успешно импортировано %d запросов на книги\n", imported.size());
            logger.info("Импорт запросов завершён успешно. Файл: {}", path);
        } catch (DataImportException e) {
            logger.error("Ошибка при импорте запросов: {}", e.getMessage());
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (DataManagerException e) {
            logger.error("Ошибка при добавлении запросов в базу: {}", e.getMessage());
            System.err.println("Ошибка при добавлении запросов в базу: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при импорте запросов {}", e.getMessage());
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
