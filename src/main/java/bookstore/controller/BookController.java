package bookstore.controller;

import bookstore.dto.BookRequest;
import bookstore.dto.BookResponse;
import bookstore.service.entityService.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BookResponse getBookById(@PathVariable("id") int id) {
        return bookService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void addBook(@RequestBody BookRequest book) {
        bookService.addBookToWarehouse(book);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<BookResponse> getAllBooks() {
        return bookService.getAll();
    }

    @GetMapping("/sort")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<BookResponse> sortBooks(@RequestParam String criteria) {
        return bookService.sortBooks(criteria);
    }

    @PatchMapping("/{id}/write-off")
    @PreAuthorize("hasRole('ADMIN')")
    public void writeOffBook(@PathVariable int id) {
        bookService.writeOffBook(id);
    }

    @GetMapping("/stale")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<BookResponse> getStaleBooks(@RequestParam int staleMonths) {
        return bookService.getStaleBooks(staleMonths);
    }
}
