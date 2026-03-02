package bookstore.repo.dao;

import bookstore.exception.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HibernateAbstractDao <T> implements GenericDAO<T> {
    private static final Logger logger = LoggerFactory.getLogger(HibernateAbstractDao.class);
    private final Class<T> type;

    @PersistenceContext
    protected EntityManager entityManager;

    protected HibernateAbstractDao(Class<T> type) {
        this.type = type;
    }

    @Override
    public void create(T object) {
        try {
            entityManager.persist(object);
        } catch (Exception e) {
            logger.error("Error creating " + type.getName(), e);
            throw new DaoException("Failed to create " + type.getName(), e);
        }
    }

    @Override
    public T findById(int id) {
        try {
            return entityManager.find(type, id);
        } catch (Exception e) {
            logger.error("Error creating " + type.getName(), e);
            throw new DaoException("Failed to find " + type.getName(), e);
        }
    }

    @Override
    public void update(T object) {
        try {
            entityManager.merge(object);
        } catch (Exception e) {
            logger.error("Error updating " + type.getName(), e);
            throw new DaoException("Failed to update " + type.getName(), e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            T entity = entityManager.find(type, id);
            if (entity != null) {
                entityManager.remove(entity);
            }
        } catch (Exception e) {
            logger.error("Error deleting {}", type.getName(), e);
            throw new DaoException("Failed to delete " + type.getName(), e);
        }
    }

    @Override
    public List<T> getAll() {
        try {
            return entityManager
                    .createQuery("FROM " + type.getName(), type)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error getting all " + type.getName(), e);
            throw new DaoException("Failed to getAll " + type.getName(), e);
        }
    }
}
