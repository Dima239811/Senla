package bookStore.ui.actions.book;

import  bookStore.model.Book;
import  bookStore.model.DataManager;
import bookStore.ui.actions.IAction;

import java.util.List;

public class AllBooksListAction implements IAction {

    private DataManager dataManager;

    public AllBooksListAction(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void execute() {
        System.out.println("Список всех книг");
        System.out.println("вывод из списка книг");
        List<Book> books = dataManager.getAllBooks();
        System.out.println("\n=== СПИСОК ВСЕХ КНИГ ===");
        System.out.println("Всего книг: " + books.size());
        System.out.println("-----------------------------------------------");

        if (books.isEmpty()) {
            System.out.println("Книги не найдены");
        } else {
            books.forEach(book -> System.out.println(book));
        }

        System.out.println("-----------------------------------------------");
    }
}
