package bookstore.controller;

import bookstore.dto.RequestBookRequest;
import bookstore.dto.RequestBookResponse;
import bookstore.service.entityService.RequestBookService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<RequestBookResponse> getAllRequestBook() {
        return requestBookService.getAll();
    }

    @PostMapping
    public void addRequest(@RequestBody RequestBookRequest requestBook) {
        requestBookService.createRequest(requestBook);
    }

    @GetMapping("/sort")
    public List<RequestBookResponse> sortRequest(@RequestParam String criteria) {
        return requestBookService.sortRequest(criteria);
    }
}
