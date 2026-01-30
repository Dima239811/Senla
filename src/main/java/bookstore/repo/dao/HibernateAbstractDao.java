package bookstore.repo.dao;

import bookstore.exception.DaoException;
import bookstore.repo.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HibernateAbstractDao <T> implements GenericDAO<T> {
    private static final Logger logger = LoggerFactory.getLogger(HibernateAbstractDao.class);
    private final Class<T> type;

    protected HibernateAbstractDao(Class<T> type) {
        this.type = type;
    }

    @Override
    public void create(T object) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.persist(object);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error creating " + type.getName(), e);
            throw new DaoException("Failed to create " + type.getName(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public T findById(int id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(type, id);
        } catch (Exception e) {
            logger.error("Error creating " + type.getName(), e);
            throw new DaoException("Failed to find " + type.getName(), e);
        }
    }

    @Override
    public void update(T object) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.merge(object);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error updating " + type.getName(), e);
            throw new DaoException("Failed to update " + type.getName(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(int id) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            T entity = session.get(type, id);
            if (entity != null) {
                session.remove(entity);
                tx.commit();
            }
        } catch (Exception e) {
            tx.rollback();
            logger.error("Error deleting {}", type.getName(), e);
            throw new DaoException("Failed to delete " + type.getName(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<T> getAll() {
        try {
            return HibernateUtil.getSession()
                    .createQuery("FROM " + type.getName(), type)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error getting all " + type.getName(), e);
            throw new DaoException("Failed to getAll " + type.getName(), e);
        }
    }
}
