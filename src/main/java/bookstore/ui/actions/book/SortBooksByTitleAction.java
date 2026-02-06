package bookstore.ui.actions.book;

import bookstore.controller.BookController;
import bookstore.enums.TypeSortBooks;
import bookstore.exception.DataManagerException;
import bookstore.service.ApplicationService;
import bookstore.model.entity.Book;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SortBooksByTitleAction implements IAction {
    private final BookController bookController;
    private static final Logger logger = LoggerFactory.getLogger(SortBooksByTitleAction.class);

    public SortBooksByTitleAction(BookController bookController) {
        this.bookController = bookController;
    }
    @Override
    public void execute() {
        logger.info("Пользователь выбрал действие сортировки книг по названию");
        System.out.println("Сортировка книг по алфавиту");

        try {
            List<Book> bookList = bookController.sortBooks(TypeSortBooks.BY_LETTER.getValue());

            if (bookList.isEmpty()) {
                logger.info("Книги не найдены");
                System.out.println("Книги не найдены");
            } else {
                logger.info("Найдено книг {}", bookList.size());
                bookList.forEach(System.out::println);
            }
            System.out.println("-----------------------------------------------");
        } catch (DataManagerException ex) {
            System.out.println("Ошибка при сортировке книг по названию " + ex.getCause());
            logger.error("Ошибка при сортировке книг по названию {}", String.valueOf(ex.getCause()));
        }
    }
}
