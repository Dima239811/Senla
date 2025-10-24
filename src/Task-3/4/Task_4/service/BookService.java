package Task_4.service;

import Task_4.model.Book;
import Task_4.model.BookCol;

import java.util.List;

public class BookService {
    private final BookCol wareHouse;

    public BookService() {
        this.wareHouse = new BookCol();
    }

    public boolean addBook(Book book) {
        return wareHouse.addBook(book);
    }

    public void writeOffBook(int bookId) {
        wareHouse.writeOffBookFromWareHouse(bookId);
    }

    public List<Book> getAllBooks() {
        return wareHouse.getBooks();
    }

    public Book findBook(int id) {
        return wareHouse.findBook(id);
    }
}
