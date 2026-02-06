package bookstore.controller;

import bookstore.model.entity.RequestBook;
import bookstore.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RequestBookController {
    private final ApplicationService applicationService;

    @Autowired
    public RequestBookController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public List<RequestBook> getAllRequestBook() {
        return applicationService.getAllRequestBook();
    }

    public void addRequest(RequestBook requestBook) {
        applicationService.addRequest(requestBook);
    }

    public List<RequestBook> sortRequest(String criteria) {
        return applicationService.sortRequest(criteria);
    }
}
