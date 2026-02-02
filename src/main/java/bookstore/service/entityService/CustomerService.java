package bookstore.service.entityService;

import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Customer;
import bookstore.repo.dao.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;

@Service
public class CustomerService implements IService<Customer> {

    @Autowired
    private CustomerDAO customerDAO;


    @Override
    public List<Customer> getAll() {
        try {
            List<Customer> customers = customerDAO.getAll();
            return customers == null ? Collections.emptyList() : customers;
        } catch (DaoException e) {
            throw new ServiceException("Failed to fetch all customers " + e.getMessage(), e);
        }
    }

    // вернет null если не найдет
    @Override
    public Customer getById(int id) {
        try {
            Customer customer = customerDAO.findById(id);
            return customer;
        } catch (DaoException e) {
            throw new ServiceException("Failed to find customer with ID " + id, e);
        }
    }

    @Override
    public void add(Customer item) {
        try {
            customerDAO.create(item);
        } catch (DaoException e) {
            throw new ServiceException("Failed to add customer with ID " + item.getCustomerID(), e);
        }
    }

    @Override
    public void update(Customer item) {
        try {
            customerDAO.update(item);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update customer with ID " + item.getCustomerID(), e);
        }
    }
}
