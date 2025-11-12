package Task5.ui.actions.book;

import Task5.model.DataManager;
import Task5.enums.TypeSortBooks;
import Task5.model.Book;
import Task5.ui.actions.IAction;

import java.util.List;

public class SortBooksByTitleAction implements IAction {

    private DataManager dataManager;

    public SortBooksByTitleAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    @Override
    public void execute() {
        System.out.println("Сортировка книг по алфавиту");
        List<Book> bookList = dataManager.sortBooks(TypeSortBooks.BY_LETTER.getValue());

        if (bookList.isEmpty()) {
            System.out.println("Книги не найдены");
        } else {
            bookList.forEach(book -> System.out.println(book));
        }

        System.out.println("-----------------------------------------------");
    }
}
