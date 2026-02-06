package bookstore.repo.dao;

import bookstore.exception.DaoException;
import bookstore.model.entity.RequestBook;
import bookstore.repo.util.HibernateUtil;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RequestBookDAO extends HibernateAbstractDao<RequestBook> {
    private static final Logger logger = LoggerFactory.getLogger(RequestBookDAO.class);

    public RequestBookDAO() {
        super(RequestBook.class);
    }

    public RequestBook findByBookId(int id) {
        try {

            String hql = "FROM RequestBook  WHERE bookid = :bookId";
            Query<RequestBook> query = HibernateUtil.getSession().createQuery(hql, RequestBook.class);
            query.setParameter("bookId", id);

            RequestBook requestBook = query.uniqueResult();

            if (requestBook != null) {
                return requestBook;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Error finding RequestBook with bookId: " + id, e);
            throw new DaoException("Error fetching RequestBook with bookId: " + id, e);
        }
    }

    public List<RequestBook> getAllWithBooksAndCustomers() {
        try {
            return HibernateUtil.getSession().createQuery(
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

