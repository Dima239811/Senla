package bookstore.ui.actions.book;

import bookstore.enums.TypeSortBooks;
import  bookstore.model.Book;
import  bookstore.model.DataManager;
import bookstore.ui.actions.IAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SortBooksByYearDescAction implements IAction {
    private DataManager dataManager;
    private static final Logger logger = LoggerFactory.getLogger(SortBooksByYearDescAction.class);

    public SortBooksByYearDescAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    @Override
    public void execute() {
        logger.info("Пользователь выбрал действие сортировки книг по году издания");
        System.out.println("Сортировка книг по году издания");
        List<Book> bookList = dataManager.sortBooks(TypeSortBooks.BY_YEAR.getValue());

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
