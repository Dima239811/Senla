package bookstore.controller;

import bookstore.dto.RequestBookRequest;
import bookstore.dto.RequestBookResponse;
import bookstore.service.entityService.RequestBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestBookController {
    private final RequestBookService requestBookService;

    @Autowired
    public RequestBookController(RequestBookService requestBookService) {
        this.requestBookService = requestBookService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RequestBookResponse> getAllRequestBook() {
        return requestBookService.getAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void addRequest(@RequestBody RequestBookRequest requestBook) {
        requestBookService.createRequest(requestBook);
    }

    @GetMapping("/sort")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RequestBookResponse> sortRequest(@RequestParam String criteria) {
        return requestBookService.sortRequest(criteria);
    }
}
