package bookstore.controller;

import bookstore.model.entity.Book;
import bookstore.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {
    private final ApplicationService applicationService;

    @Autowired
    public BookController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public Book getBookById(int id) {
        return applicationService.findBook(id);
    }

    public void addBook(Book book) {
        applicationService.addBookToWareHouse(book);
    }

    public List<Book> getAllBooks() {
        return applicationService.getAllBooks();
    }

    public List<Book> sortBooks(String criteria) {
        return applicationService.sortBooks(criteria);
    }

    public void writeOffBook(int id) {
        applicationService.writeOffBook(id);
    }

    public List<Book> getStaleBooks(int staleMonths) {
        return applicationService.getStaleBooks(staleMonths);
    }
}
