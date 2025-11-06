package Task_4.model;

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
}
