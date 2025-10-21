package Task_4.service;

import Task_4.model.Customer;
import Task_4.model.CustomerCol;

import java.util.List;

public class CustomerService {
    private final CustomerCol customerCol;

    public CustomerService() {
        this.customerCol = new CustomerCol();
    }

    public List<Customer> getAllCustomers() {
        return customerCol.getCustomers();
    }

    public void setCustomers(List<Customer> customers) {
        customerCol.setCustomers(customers);
    }

    public CustomerCol getCustomerCol() {
        return customerCol;
    }

}
