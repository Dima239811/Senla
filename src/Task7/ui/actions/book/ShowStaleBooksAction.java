package Task7.ui.actions.book;



import Task7.model.Book;
import Task7.model.DataManager;
import Task7.ui.actions.IAction;
import Task7.util.AppConfig;

import java.util.List;

public class ShowStaleBooksAction implements IAction {
    private final DataManager dataManager;

    public ShowStaleBooksAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        try {
            int staleMonths = AppConfig.getStaleBookMonths();
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
