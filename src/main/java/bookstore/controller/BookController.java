package bookstore.controller;

import bookstore.dto.BookRequest;
import bookstore.dto.BookResponse;
import bookstore.model.entity.Book;
import bookstore.service.InventoryService;
import bookstore.service.entityService.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {
    private final BookService bookService;

    private final InventoryService inventoryService;

    @Autowired
    public BookController(BookService bookService, InventoryService inventoryService) {
        this.bookService = bookService;
        this.inventoryService = inventoryService;
    }

    public BookResponse getBookById(int id) {
        return bookService.getById(id);
    }

    public void addBook(BookRequest book) {
        inventoryService.addBookToWarehouse(book);
    }

    public List<BookResponse> getAllBooks() {
        return bookService.getAll();
    }

    public List<BookResponse> sortBooks(String criteria) {
        return bookService.sortBooks(criteria);
    }

    public void writeOffBook(int id) {
        bookService.writeOffBook(id);
    }

    public List<BookResponse> getStaleBooks(int staleMonths) {
        return inventoryService.getStaleBooks(staleMonths);
    }
}
