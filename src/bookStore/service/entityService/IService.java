package bookStore.service.entityService;

import java.util.List;

public interface IService<T> {
    List<T> getAll();
    T getById(int id);
    void add(T item);
    void update(T item);
}
