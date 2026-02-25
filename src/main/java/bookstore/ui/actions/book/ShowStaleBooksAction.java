package bookstore.ui.actions.book;

import bookstore.controller.BookController;
import bookstore.dto.BookResponse;
import bookstore.exception.DataManagerException;
import bookstore.ui.actions.IAction;
import bookstore.util.LibraryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ShowStaleBooksAction implements IAction {

    private final BookController bookController;
    private static final Logger logger = LoggerFactory.getLogger(ShowStaleBooksAction.class);


    private final LibraryConfig libraryConfig;

    @Autowired
    public ShowStaleBooksAction(BookController bookController, LibraryConfig libraryConfig) {
        this.bookController = bookController;
        this.libraryConfig = libraryConfig;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: просмотр залежавшихся книг");
        try {
            int staleMonths = libraryConfig.getStaleMonths();

            try {
                List<BookResponse> staleBooks = bookController.getStaleBooks(staleMonths);

                if (staleBooks.isEmpty()) {
                    logger.info("Залежавшихся книг за последние {} месяцев не найдено", staleMonths);
                    System.out.println("Нет залежавшихся книг.");
                } else {
                    logger.info("Найдено {} залежавшихся книг", staleBooks.size());
                    System.out.println("Список залежавшихся книг (не продавались более "
                            + staleMonths + " месяцев):");
                    staleBooks.forEach(book ->
                            System.out.println("• " + book.name() + " (ID: " + book.bookId() + ")"));
                }
            } catch (DataManagerException ex) {
                logger.error("Ошибка при просмотре залежавшихся книг: {}", ex.getMessage());
                System.out.println("Ошибка при просмотре залежавшихся книг: " + ex.getMessage());
            }
        } catch (Exception e) {
            logger.error("Ошибка при получении списка залежавшихся книг", e);
            System.out.println("Ошибка при получении списка залежавшихся книг: " + e.getMessage());
        }
    }
}
