package bookStore.repo.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    void create(T object) throws SQLException;
    T findById(int id) throws SQLException;
    void update(T object) throws SQLException;
    void delete(int id) throws SQLException;
    List<T> getAll() throws SQLException;
}
