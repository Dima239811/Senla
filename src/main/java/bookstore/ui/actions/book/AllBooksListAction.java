package bookstore.ui.actions.book;

import bookstore.exception.DataManagerException;
import bookstore.model.entity.Book;
import bookstore.model.DataManager;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AllBooksListAction implements IAction {

    private DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(AllBooksListAction.class);

    public AllBooksListAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал команду: вывод всех книг");
        System.out.println("Список всех книг");
        System.out.println("вывод из списка книг");

        try {
            List<Book> books = dataManager.getAllBooks();
            System.out.println("\n=== СПИСОК ВСЕХ КНИГ ===");
            System.out.println("Всего книг: " + books.size());
            System.out.println("-----------------------------------------------");

            if (books.isEmpty()) {
                logger.info("Список книг пуст");
                System.out.println("Книги не найдены");
            } else {
                logger.info("Выведено {} книг", books.size());
                books.forEach(book -> System.out.println(book));
            }

            System.out.println("-----------------------------------------------");
        } catch (DataManagerException e) {
            System.err.println("Ошибка: " + e.getMessage());
            logger.error("Ошибка при получении книг", e);
        }
    }
}
