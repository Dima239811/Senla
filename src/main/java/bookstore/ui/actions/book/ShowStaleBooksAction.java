package bookstore.ui.actions.book;


import bookstore.dependesies.annotation.Inject;
import bookstore.model.Book;
import bookstore.model.DataManager;
import bookstore.ui.actions.IAction;
import bookstore.util.LibraryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShowStaleBooksAction implements IAction {

    private DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(ShowStaleBooksAction.class);


    private LibraryConfig libraryConfig;

    @Inject
    public ShowStaleBooksAction(DataManager dataManager, LibraryConfig libraryConfig) {
        this.dataManager = dataManager;
        this.libraryConfig = libraryConfig;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: просмотр залежавшихся книг");
        try {
            int staleMonths = libraryConfig.getStaleMonths();
            List<Book> staleBooks = dataManager.getStaleBooks(staleMonths);

            if (staleBooks.isEmpty()) {
                logger.info("Залежавшихся книг за последние {} месяцев не найдено", staleMonths);
                System.out.println("Нет залежавшихся книг.");
            } else {
                logger.info("Найдено {} залежавшихся книг", staleBooks.size());
                System.out.println("Список залежавшихся книг (не продавались более "
                        + staleMonths + " месяцев):");
                staleBooks.forEach(book ->
                        System.out.println("• " + book.getName() + " (ID: " + book.getBookId() + ")"));
            }
        } catch (Exception e) {
            logger.error("Ошибка при получении списка залежавшихся книг", e);
            System.out.println("Ошибка при получении списка залежавшихся книг: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
