package bookstore.ui.actions.book;

import bookstore.enums.TypeSortBooks;
import  bookstore.model.Book;
import  bookstore.model.DataManager;
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
        logger.info("Пользователь выбрал деййствие сортировки книг по наличию на складе");
        System.out.println("Сортировка книг по наличию на складе");
        List<Book> bookList = dataManager.sortBooks(TypeSortBooks.BY_STOCKS_IN_WAREHOUSE.getValue());

        if (bookList.isEmpty()) {
            logger.info("Книги не найдены");
            System.out.println("Книги не найдены");
        } else {
            logger.info("Найдено книг " + bookList.size());
            bookList.forEach(book -> System.out.println(book));
        }

        System.out.println("-----------------------------------------------");
    }
}
