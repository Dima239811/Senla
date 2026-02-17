package bookstore.repo.dao;

import bookstore.exception.DaoException;
import bookstore.model.entity.RequestBook;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RequestBookDAO extends HibernateAbstractDao<RequestBook> {
    private static final Logger logger = LoggerFactory.getLogger(RequestBookDAO.class);

    public RequestBookDAO(SessionFactory sessionFactory) {
        super(RequestBook.class, sessionFactory);
    }

    public RequestBook findByBookId(int id) {
        try {

            String hql = "FROM RequestBook  WHERE bookid = :bookId";
            Query<RequestBook> query = getCurrentSession().createQuery(hql, RequestBook.class);
            query.setParameter("bookId", id);

            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding RequestBook with bookId: {}", id, e);
            throw new DaoException("Error fetching RequestBook with bookId: " + id, e);
        }
    }

    public List<RequestBook> getAllWithBooksAndCustomers() {
        try {
            return getCurrentSession().createQuery(
                            "SELECT DISTINCT r FROM RequestBook  r " +
                                    "LEFT JOIN FETCH r.book " +
                                    "LEFT JOIN FETCH r.customer", RequestBook.class)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error getting all book requests with details", e);
            throw new DaoException("Error getting all book requests with details", e);
        }
    }
}

