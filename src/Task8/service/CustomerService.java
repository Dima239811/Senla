package Task8.service;



import Task8.dependesies.annotation.Inject;
import Task8.model.Customer;
import Task8.model.CustomerCol;

import java.util.List;

public class CustomerService {

    @Inject
    private CustomerCol customerCol;


    public List<Customer> getAllCustomers() {
        return customerCol.getCustomers();
    }

    public void setCustomers(List<Customer> customers) {
        customerCol.setCustomers(customers);
    }

    public CustomerCol getCustomerCol() {
        return customerCol;
    }

    public void addCustomer(Customer customer) {
        customerCol.addCustomer(customer);
    }


}
