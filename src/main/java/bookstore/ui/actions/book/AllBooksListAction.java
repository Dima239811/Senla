package bookstore.ui.actions.book;

import bookstore.controller.BookController;
import bookstore.exception.DataManagerException;
import bookstore.model.entity.Book;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AllBooksListAction implements IAction {

    private final BookController bookController;
    private static final Logger logger = LoggerFactory.getLogger(AllBooksListAction.class);

    public AllBooksListAction(BookController bookController) {
        this.bookController = bookController;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: вывод всех книг");
        System.out.println("Список всех книг");
        System.out.println("вывод из списка книг");

        try {
            List<Book> books = bookController.getAllBooks();
            System.out.println("\n=== СПИСОК ВСЕХ КНИГ ===");
            System.out.println("Всего книг: " + books.size());
            System.out.println("-----------------------------------------------");

            if (books.isEmpty()) {
                logger.info("Список книг пуст");
                System.out.println("Книги не найдены");
            } else {
                logger.info("Выведено {} книг", books.size());
                books.forEach(System.out::println);
            }

            System.out.println("-----------------------------------------------");
        } catch (DataManagerException e) {
            System.err.println("Ошибка: " + e.getMessage());
            logger.error("Ошибка при получении книг", e);
        }
    }
}
