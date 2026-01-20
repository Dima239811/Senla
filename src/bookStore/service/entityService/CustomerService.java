package bookStore.service.entityService;



import bookStore.dependesies.annotation.Inject;
import bookStore.model.Customer;
import bookStore.repo.dao.CustomerDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Collections;

public class CustomerService implements IService<Customer> {

    @Inject
    private CustomerDAO customerDAO;


    @Override
    public List<Customer> getAll() {
        try {
            List<Customer> customers = customerDAO.getAll();
            return customers == null ? Collections.emptyList() : customers;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all customers " + e.getMessage(), e);
        }
    }

    // вернет null если не найдет
    @Override
    public Customer getById(int id) {
        try {
            Customer customer = customerDAO.findById(id);
            return customer;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find customer with ID " + id, e);
        }
    }

    @Override
    public void add(Customer item) {
        try {
            customerDAO.create(item);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch customer with ID " + item.getCustomerID() + e.getMessage());

        }
    }

    @Override
    public void update(Customer item) {
        try {
            customerDAO.update(item);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update customer with ID " + item.getCustomerID(), e);

        }
    }


}
