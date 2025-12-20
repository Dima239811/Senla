package Task8.ui.actions.book;

import Task8.enums.TypeSortBooks;
import Task8.model.Book;
import Task8.model.DataManager;
import Task8.ui.actions.IAction;

import java.util.List;

public class SortBooksByPriceAction implements IAction {

    private DataManager dataManager;

    public SortBooksByPriceAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        System.out.println("Сортировка по цене: ");
        List<Book> books = dataManager.sortBooks(TypeSortBooks.BY_PRICE.getValue());

        if (books.isEmpty()) {
            System.out.println("Книги не найдены");
        } else {
            books.forEach(book -> System.out.println(book));
        }

        System.out.println("-----------------------------------------------");

    }
}
