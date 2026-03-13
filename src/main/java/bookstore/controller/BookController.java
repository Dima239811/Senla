package bookstore.controller;

import bookstore.dto.BookRequest;
import bookstore.dto.BookResponse;
import bookstore.service.entityService.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable("id") int id) {
        return bookService.getById(id);
    }

    @PostMapping
    public void addBook(@RequestBody BookRequest book) {
        bookService.addBookToWarehouse(book);
    }

    @GetMapping
    public List<BookResponse> getAllBooks() {
        return bookService.getAll();
    }

    @GetMapping("/sort")
    public List<BookResponse> sortBooks(@RequestParam String criteria) {
        return bookService.sortBooks(criteria);
    }

    @PatchMapping("/{id}/write-off")
    public void writeOffBook(@PathVariable int id) {
        bookService.writeOffBook(id);
    }

    @GetMapping("/stale")
    public List<BookResponse> getStaleBooks(@RequestParam int staleMonths) {
        return bookService.getStaleBooks(staleMonths);
    }
}
