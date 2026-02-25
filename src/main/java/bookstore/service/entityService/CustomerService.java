package bookstore.service.entityService;

import bookstore.dto.CustomerRequest;
import bookstore.dto.CustomerResponse;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Customer;
import bookstore.model.mapper.CustomerMapper;
import bookstore.repo.dao.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collections;

@Service
public class CustomerService {
    private final CustomerDAO customerDAO;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(CustomerDAO customerDAO, CustomerMapper customerMapper) {
        this.customerDAO = customerDAO;
        this.customerMapper = customerMapper;
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> getAll() {
        try {
            List<Customer> customers = customerDAO.getAll();
            return customerMapper.toCustomerResponceList(customers);
        } catch (DaoException e) {
            throw new ServiceException("Failed to fetch all customers " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public CustomerResponse getById(int id) {
        try {
            Customer customer = customerDAO.findById(id);
            return customerMapper.toCustomerResponse(customer);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find customer with ID " + id, e);
        }
    }

    @Transactional
    public void add(CustomerRequest item) {
        try {
            Customer customer = customerMapper.toEntity(item);
            customerDAO.create(customer);
        } catch (DaoException e) {
            throw new ServiceException("Failed to add customer with ID " + item.fullName(), e);
        }
    }

    @Transactional
    public void update(CustomerRequest item) {
        try {
            Customer customer = customerMapper.toEntity(item);
            customerDAO.update(customer);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update customer with ID " + item.fullName(), e);
        }
    }

    @Transactional(readOnly = true)
    public Customer getEntityById(int id) {
        return customerDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public Customer findByEmail(String email) {
        return customerDAO.findByEmail(email);
    }

    @Transactional
    public void addCustomerEntity(Customer customer) {
        try {
            customerDAO.create(customer);
        } catch (DaoException e) {
            throw new ServiceException("Failed to add customer with ID " + customer.getFullName(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomer() {
        return customerDAO.getAll();
    }
}
