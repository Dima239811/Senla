package Task6.service;

import Task6.model.Book;
import Task6.model.BookCol;

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

    public List<Book> sortBooks(String criteria) {
        switch (criteria.toLowerCase()) {
            case "по алфавиту":
                return wareHouse.sortByName();
            case "по цене":
                return wareHouse.sortByPrice();
            case "по году издания":
                return wareHouse.sortByYear();
            case "по наличию на складе":
                return wareHouse.sortByStatus();
            default:
                System.out.println("Ошибка: неопознанный критерий сортировки.");
                return wareHouse.getBooks();
        }
    }
}
