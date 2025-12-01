package Task7.ui.actions.book;

import Task7.enums.TypeSortBooks;
import Task7.model.Book;
import Task7.model.DataManager;
import Task7.ui.actions.IAction;

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
