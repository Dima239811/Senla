package bookstore.repo.dao;

import bookstore.exception.DaoException;
import bookstore.model.entity.Book;
import bookstore.repo.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookDAO extends HibernateAbstractDao<Book> {
    private static final Logger logger = LoggerFactory.getLogger(BookDAO.class);

    public BookDAO() {
        super(Book.class);
        System.out.println("вызван bookDAO");
    }

    public boolean existsByNameAndAuthor(String name, String author) {
        try {
            Long count = HibernateUtil.getSession().createQuery(
                            "SELECT COUNT(b) FROM Book b WHERE LOWER(b.name) = LOWER(:name) " +
                                    "AND LOWER(b.author) = LOWER(:author)", Long.class)
                    .setParameter("name", name)
                    .setParameter("author", author)
                    .getSingleResult();
            return count > 0;
        } catch (Exception exception) {
            logger.error("Error existsByNameAndAuthor book", exception);
            throw new DaoException("Error existsByNameAndAuthor book", exception);
        }
    }
}
