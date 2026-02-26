package bookstore.controller;

import bookstore.dto.CustomerRequest;
import bookstore.dto.CustomerResponse;
import bookstore.model.entity.Customer;
import bookstore.service.entityService.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable("id") int id) {
        return customerService.getById(id);
    }

    @PostMapping
    public void addCustomer(@RequestBody CustomerRequest customer) {
        customerService.add(customer);
    }

    @GetMapping
    public List<CustomerResponse> getAllCustomer() {
        return customerService.getAll();
    }
}
