package bookstore.repo.dao;

import bookstore.model.entity.Customer;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDAO extends HibernateAbstractDao<Customer> {
    public CustomerDAO(SessionFactory sessionFactory) {
        super(Customer.class, sessionFactory);
    }

    public Customer findByEmail(String email) {
        return getCurrentSession().createQuery(
                        "FROM Customer c WHERE c.email = :email",
                        Customer.class
                )
                .setParameter("email", email)
                .uniqueResult();
    }
}
