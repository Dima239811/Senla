package Task5.ui.actions.book;

import Task5.model.DataManager;
import Task5.enums.TypeSortBooks;
import Task5.model.Book;
import Task5.ui.actions.IAction;

import java.util.List;

public class SortBooksByAvailiable implements IAction {
    private DataManager dataManager;

    public SortBooksByAvailiable(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        System.out.println("Сортировка книг по наличию на складе");
        List<Book> bookList = dataManager.sortBooks(TypeSortBooks.BY_STOCKS_IN_WAREHOUSE.getValue());

        if (bookList.isEmpty()) {
            System.out.println("Книги не найдены");
        } else {
            bookList.forEach(book -> System.out.println(book));
        }

        System.out.println("-----------------------------------------------");
    }
}
