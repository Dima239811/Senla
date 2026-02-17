package bookstore.repo.dao;

import bookstore.exception.DaoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HibernateAbstractDao <T> implements GenericDAO<T> {
    private static final Logger logger = LoggerFactory.getLogger(HibernateAbstractDao.class);
    private final Class<T> type;

    private final SessionFactory sessionFactory;

    protected HibernateAbstractDao(Class<T> type, SessionFactory sessionFactory) {
        this.type = type;
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void create(T object) {
        try {
            getCurrentSession().persist(object);
        } catch (Exception e) {
            logger.error("Error creating " + type.getName(), e);
            throw new DaoException("Failed to create " + type.getName(), e);
        }
    }

    @Override
    public T findById(int id) {
        try {
            return getCurrentSession().get(type, id);
        } catch (Exception e) {
            logger.error("Error creating " + type.getName(), e);
            throw new DaoException("Failed to find " + type.getName(), e);
        }
    }

    @Override
    public void update(T object) {
        try {
            getCurrentSession().merge(object);
        } catch (Exception e) {
            logger.error("Error updating " + type.getName(), e);
            throw new DaoException("Failed to update " + type.getName(), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            T entity = getCurrentSession().get(type, id);
            if (entity != null) {
                getCurrentSession().remove(entity);
            }
        } catch (Exception e) {
            logger.error("Error deleting {}", type.getName(), e);
            throw new DaoException("Failed to delete " + type.getName(), e);
        }
    }

    @Override
    public List<T> getAll() {
        try {
            return getCurrentSession()
                    .createQuery("FROM " + type.getName(), type)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error getting all " + type.getName(), e);
            throw new DaoException("Failed to getAll " + type.getName(), e);
        }
    }
}
