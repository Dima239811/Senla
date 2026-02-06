package bookstore.ui.actions.book;

import bookstore.enums.TypeSortBooks;
import  bookstore.model.Book;
import  bookstore.model.DataManager;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SortBooksByPriceAction implements IAction {
    private DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(SortBooksByPriceAction.class);

    public SortBooksByPriceAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        logger.info("Пользователь выбрал действие сортировки книг по цене");
        System.out.println("Сортировка по цене: ");
        List<Book> books = dataManager.sortBooks(TypeSortBooks.BY_PRICE.getValue());

        if (books.isEmpty()) {
            logger.info("Книги не найдены");
            System.out.println("Книги не найдены");
        } else {
            logger.info("Найдено книг " + books.size());
            books.forEach(book -> System.out.println(book));
        }
        System.out.println("-----------------------------------------------");
    }
}
