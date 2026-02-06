package bookstore.repo.dao;

import bookstore.model.entity.Customer;

public class CustomerDAO extends HibernateAbstractDao<Customer> {
    public CustomerDAO() {
        super(Customer.class);
    }
}
