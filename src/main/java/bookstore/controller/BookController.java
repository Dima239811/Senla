package bookstore.controller;

import bookstore.model.entity.Book;
import bookstore.service.InventoryService;
import bookstore.service.entityService.BookServiceImpl;
import bookstore.service.entityService.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {
    private final IService<Book> bookService;

    private final InventoryService inventoryService;

    @Autowired
    public BookController(BookServiceImpl bookServiceImpl, InventoryService inventoryService) {
        this.bookService = bookServiceImpl;
        this.inventoryService = inventoryService;
    }

    public Book getBookById(int id) {
        return bookService.getById(id);
    }

    public void addBook(Book book) {
        inventoryService.addBookToWarehouse(book);
    }

    public List<Book> getAllBooks() {
        return bookService.getAll();
    }

    public List<Book> sortBooks(String criteria) {
        return bookService.sortBooks(criteria);
    }

    public void writeOffBook(int id) {
        bookService.writeOffBook(id);
    }

    public List<Book> getStaleBooks(int staleMonths) {
        return inventoryService.getStaleBooks(staleMonths);
    }
}
