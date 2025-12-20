package Task8.ui.actions.book;

import Task8.enums.TypeSortBooks;
import Task8.model.Book;
import Task8.model.DataManager;
import Task8.ui.actions.IAction;

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
