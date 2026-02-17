package bookstore.service.entityService;

import bookstore.model.entity.Book;

import java.util.List;

public interface BookService extends IService<Book> {
    List<Book> sortBooks(String criteria);
    void writeOffBook(int id);
}
