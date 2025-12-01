package Task7.ui.actions.csv.book;

import Task7.exception.DataImportException;
import Task7.model.Book;
import Task7.model.DataManager;
import Task7.ui.actions.IAction;

import java.util.List;
import java.util.Scanner;

public class ImportBooksAction implements IAction {
    private final DataManager dataManager;
    private final Scanner scanner;

    public ImportBooksAction(DataManager dataManager) {
        this.dataManager = dataManager;
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void execute() {
        try {
            System.out.println("\n=== Импорт книг из CSV ===");
            System.out.print("Введите путь к файлу: ");
            String path = scanner.nextLine().trim();

//            System.out.print("Обновлять существующие записи? (y/n): ");
//            boolean update = scanner.nextLine().equalsIgnoreCase("y");

            List<Book> importedBooks = dataManager.importBooksFromCsv(path);
            System.out.printf("Успешно импортировано %d книг\n", importedBooks.size());

        } catch (DataImportException e) {
            System.err.println("Ошибка импорта: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
        }
    }
}
