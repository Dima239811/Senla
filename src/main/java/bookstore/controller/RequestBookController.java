package bookstore.controller;

import bookstore.model.entity.RequestBook;
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

    public List<RequestBook> getAllRequestBook() {
        return requestBookService.getAll();
    }

    public void addRequest(RequestBook requestBook) {
        requestBookService.addRequest(requestBook);
    }

    public List<RequestBook> sortRequest(String criteria) {
        return requestBookService.sortRequest(criteria);
    }
}
