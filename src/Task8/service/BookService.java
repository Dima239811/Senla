package Task8.service;

import Task8.dependesies.annotation.Inject;
import Task8.dependesies.annotation.PostConstruct;
import Task8.model.Book;
import Task8.model.BookCol;

import java.util.List;

public class BookService {

    @Inject
    private BookCol wareHouse;


    @PostConstruct
    public void postConstruct() {
        System.out.println("BookService has been inizialized");
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
