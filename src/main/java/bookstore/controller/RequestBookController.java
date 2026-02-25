package bookstore.controller;

import bookstore.dto.RequestBookRequest;
import bookstore.dto.RequestBookResponse;
import bookstore.service.entityService.RequestBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RequestBookController {
    private final RequestBookService requestBookService;

    @Autowired
    public RequestBookController(RequestBookService requestBookService) {
        this.requestBookService = requestBookService;
    }

    public List<RequestBookResponse> getAllRequestBook() {
        return requestBookService.getAll();
    }

    public void addRequest(RequestBookRequest requestBook) {
        requestBookService.createRequest(requestBook);
    }

    public List<RequestBookResponse> sortRequest(String criteria) {
        return requestBookService.sortRequest(criteria);
    }
}
