package bookstore.repo.dao;

import bookstore.model.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDAO extends HibernateAbstractDao<Customer> {
    public CustomerDAO() {
        super(Customer.class);
    }

    public Customer findByEmail(String email) {
        return entityManager.createQuery(
                        "FROM Customer c WHERE c.email = :email",
                        Customer.class
                )
                .setParameter("email", email)
                .getSingleResult();
    }
}
