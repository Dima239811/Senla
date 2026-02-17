package bookstore.controller;

import bookstore.model.entity.Customer;
import bookstore.service.entityService.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Customer getCustomerById(int id) {
        return customerService.getById(id);
    }

    public void addCustomer(Customer customer) {
        customerService.add(customer);
    }

    public List<Customer> getAllCustomer() {
        return customerService.getAll();
    }
}
