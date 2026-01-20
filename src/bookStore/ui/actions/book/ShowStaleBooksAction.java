package bookStore.ui.actions.book;


import bookStore.dependesies.annotation.Inject;
import bookStore.model.Book;
import bookStore.model.DataManager;
import bookStore.ui.actions.IAction;
import bookStore.util.LibraryConfig;

import java.util.List;

public class ShowStaleBooksAction implements IAction {

    private DataManager dataManager;

    private LibraryConfig libraryConfig;

    @Inject
    public ShowStaleBooksAction(DataManager dataManager, LibraryConfig libraryConfig) {
        this.dataManager = dataManager;
        this.libraryConfig = libraryConfig;
    }

    @Override
    public void execute() {
        try {
            int staleMonths = libraryConfig.getStaleMonths();
            List<Book> staleBooks = dataManager.getStaleBooks(staleMonths);

            if (staleBooks.isEmpty()) {
                System.out.println("Нет залежавшихся книг.");
            } else {
                System.out.println("Список залежавшихся книг (не продавались более "
                        + staleMonths + " месяцев):");
                staleBooks.forEach(book ->
                        System.out.println("• " + book.getName() + " (ID: " + book.getBookId() + ")"));
            }
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка залежавшихся книг: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
