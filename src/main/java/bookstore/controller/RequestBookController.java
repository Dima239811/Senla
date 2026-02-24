package bookstore.controller;

import bookstore.dto.RequestBookRequest;
import bookstore.dto.RequestBookResponse;
import bookstore.model.entity.RequestBook;
import bookstore.service.InventoryService;
import bookstore.service.entityService.RequestBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RequestBookController {
    private final RequestBookService requestBookService;

    private final InventoryService inventoryService;

    @Autowired
    public RequestBookController(RequestBookService requestBookService, InventoryService inventoryService) {
        this.requestBookService = requestBookService;
        this.inventoryService = inventoryService;
    }

    public List<RequestBookResponse> getAllRequestBook() {
        return requestBookService.getAll();
    }

    public void addRequest(RequestBookRequest requestBook) {
        inventoryService.createRequest(requestBook);
    }

    public List<RequestBookResponse> sortRequest(String criteria) {
        return requestBookService.sortRequest(criteria);
    }
}
