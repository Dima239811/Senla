package bookstore.repo.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    void create(T entity);

    void update(T entity);

    void delete(int id);

    List<T> getAll();

    T findById(int id);
}
