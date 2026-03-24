package bookstore.repo;

import bookstore.exception.DaoException;
import bookstore.model.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO extends HibernateAbstractDao<User>{
    public UserDAO() {
        super(User.class);
    }

    public User findByLogin(String login) {
        try {
            return entityManager.createQuery("from User where login = :login", User.class).
                    setParameter("login", login).getSingleResult();
        } catch (Exception ex) {
            throw new DaoException("User with login: " + login + " not found", ex.getCause());
        }
    }
}
