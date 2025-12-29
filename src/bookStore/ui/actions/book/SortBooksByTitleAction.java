package bookStore.ui.actions.book;

import bookStore.enums.TypeSortBooks;
import  bookStore.model.Book;
import  bookStore.model.DataManager;
import bookStore.ui.actions.IAction;

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
