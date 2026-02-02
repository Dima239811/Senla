package bookstore.repo.dao;

import bookstore.exception.DaoException;
import bookstore.model.entity.Order;
import bookstore.repo.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDAO extends HibernateAbstractDao<Order> {
    private static final Logger logger = LoggerFactory.getLogger(OrderDAO.class);
    public OrderDAO() {
        super(Order.class);
    }

    public List<Order> getAllWithBooksAndCustomers() {
        try {
            return HibernateUtil.getSession().createQuery(
                            "SELECT DISTINCT o FROM Order o " +
                                    "LEFT JOIN FETCH o.book " +
                                    "LEFT JOIN FETCH o.customer", Order.class)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error getting orders with books and customers", e);
            throw new DaoException("Error getting orders with books and customers", e);
        }
    }
}
