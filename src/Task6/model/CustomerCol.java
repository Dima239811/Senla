package Task6.model;

import java.util.ArrayList;
import java.util.List;

public class CustomerCol {
    private List<Customer> customers;

    public CustomerCol() {
        this.customers = new ArrayList<>();
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public Customer findCustomer(int id) {
        for (Customer b: customers) {
            if (b.getCustomerID() == id) {
                return b;
            }
        }
        return null;
    }

    public void addCustomer(Customer customer) {
        if (findCustomer(customer.getCustomerID()) != null) {
            updateCustomer(customer);
            return;
        }

        customers.add(customer);
    }

    public void updateCustomer(Customer customer) {
        Customer existing = findCustomer(customer.getCustomerID());
        if (existing != null) {
            existing.setFullName(customer.getFullName());
            existing.setAge(customer.getAge());
            existing.setPhoneNumber(customer.getPhoneNumber());
            existing.setAddress(customer.getAddress());
            existing.setEmail(customer.getEmail());
            return;
        }
    }
}
