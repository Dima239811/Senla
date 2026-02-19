package bookstore.controller;

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

    public List<RequestBook> getAllRequestBook() {
        return requestBookService.getAll();
    }

    public void addRequest(RequestBook requestBook) {
        inventoryService.createRequest(requestBook);
    }

    public List<RequestBook> sortRequest(String criteria) {
        return requestBookService.sortRequest(criteria);
    }
}
