package bookstore.repo;

import bookstore.exception.DaoException;
import bookstore.model.entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BookDAO extends HibernateAbstractDao<Book> {
    private static final Logger logger = LoggerFactory.getLogger(BookDAO.class);

    public BookDAO() {
        super(Book.class);
    }

    public boolean existsByNameAndAuthor(String name, String author) {
        try {
            Long count = entityManager.createQuery(
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
