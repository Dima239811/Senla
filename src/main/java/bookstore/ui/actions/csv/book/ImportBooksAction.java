package bookstore.ui.actions.csv.book;

import bookstore.exception.DataImportException;
import  bookstore.model.Book;
import  bookstore.model.DataManager;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class ImportBooksAction implements IAction {
    private final DataManager dataManager;
    private final Scanner scanner;
    private static final Logger logger = LoggerFactory.getLogger(ImportBooksAction.class);

    public ImportBooksAction(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void execute() {
        logger.info("Запуск действия: импорт книг в CSV.");
        try {
            System.out.println("\n=== Импорт книг из CSV ===");
            System.out.print("Введите путь к файлу: ");
            String path = scanner.nextLine().trim();

            List<Book> importedBooks = dataManager.importBooksFromCsv(path);
            System.out.printf("Успешно импортировано %d книг\n", importedBooks.size());
            logger.info("Импорт книг завершён успешно. Файл: {}", path);
        } catch (DataImportException e) {
            logger.error("Ошибка при импорте книг: {}", e.getMessage());
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при импорте книг {}", e.getMessage());
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
