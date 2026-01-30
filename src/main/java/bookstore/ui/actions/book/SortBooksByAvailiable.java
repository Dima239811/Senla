package bookstore.ui.actions.book;

import bookstore.enums.TypeSortBooks;
import bookstore.exception.DataManagerException;
import bookstore.model.entity.Book;
import bookstore.model.DataManager;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SortBooksByAvailiable implements IAction {
    private DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(SortBooksByAvailiable.class);

    public SortBooksByAvailiable(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал действие сортировки книг по наличию на складе");
        System.out.println("Сортировка книг по наличию на складе");

        try {
            List<Book> bookList = dataManager.sortBooks(TypeSortBooks.BY_STOCKS_IN_WAREHOUSE.getValue());

            if (bookList.isEmpty()) {
                logger.info("Книги не найдены");
                System.out.println("Книги не найдены");
            } else {
                logger.info("Найдено книг {}", bookList.size());
                bookList.forEach(System.out::println);
            }
            System.out.println("-----------------------------------------------");
        } catch (DataManagerException ex) {
            System.out.println("Ошибка при сортировке книг по на личию на складе " + ex.getCause());
            logger.error("Ошибка при сортировке книг по на личию на складе {}", String.valueOf(ex.getCause()));
        }
    }
}
