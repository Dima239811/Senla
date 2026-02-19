package bookstore.service.entityService;

import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Customer;
import bookstore.repo.dao.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collections;

@Service
public class CustomerService implements IService<Customer> {
    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> getAll() {
        try {
            List<Customer> customers = customerDAO.getAll();
            return customers == null ? Collections.emptyList() : customers;
        } catch (DaoException e) {
            throw new ServiceException("Failed to fetch all customers " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getById(int id) {
        try {
            Customer customer = customerDAO.findById(id);
            return customer;
        } catch (DaoException e) {
            throw new ServiceException("Failed to find customer with ID " + id, e);
        }
    }

    @Transactional
    @Override
    public void add(Customer item) {
        try {
            customerDAO.create(item);
        } catch (DaoException e) {
            throw new ServiceException("Failed to add customer with ID " + item.getCustomerID(), e);
        }
    }

    @Transactional
    @Override
    public void update(Customer item) {
        try {
            customerDAO.update(item);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update customer with ID " + item.getCustomerID(), e);
        }
    }
}
