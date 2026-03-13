package bookstore.service.entityService;


import java.util.List;

public interface IService<T, R> {
    List<R> getAll();
    R getById(int id);
    void add(T item);
    void update(T item);
}
