package bookstore.controller;

import bookstore.model.entity.Customer;
import bookstore.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CustomerController {
    private final ApplicationService applicationService;

    @Autowired
    public CustomerController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public Customer getCustomerById(int id) {
        return applicationService.getCustomerById(id);
    }

    public void addCustomer(Customer customer) {
        applicationService.addCustomer(customer);
    }

    public List<Customer> getAllCustomer() {
        return applicationService.getAllCustomers();
    }
}
