package bookstore.service.entityService;

import bookstore.comporator.book.AvailiableComporator;
import bookstore.comporator.book.LetterComporator;
import bookstore.comporator.book.PriceComporator;
import bookstore.comporator.book.YearComporator;
import bookstore.dependesies.annotation.Inject;
import bookstore.dependesies.annotation.PostConstruct;
import bookstore.enums.StatusBook;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.Book;
import bookstore.repo.dao.BookDAO;

import java.sql.SQLException;
import java.util.List;

public class BookService implements IService<Book> {

    @Inject
    private BookDAO bookDAO;


    @PostConstruct
    public void postConstruct() {
        System.out.println("BookService has been inizialized");
    }

    @Override
    public List<Book> getAll() {
        try {
            List<Book> books = bookDAO.getAll();
            return books;
        } catch (DaoException e) {
            throw new ServiceException("Failed to get all books", e);
        }
    }

    @Override
    public Book getById(int id) {
        try {
            Book book = bookDAO.findById(id);
            return book;
        } catch (DaoException e) {
            throw new ServiceException("Failed to get books by id " + id + " ", e);
        }
    }

    @Override
    public void add(Book item) {
        try {
            Book book = bookDAO.findById(item.getBookId());
            if (book != null) {
                System.out.println("Книга с таким id же существует, ее данные будут обновлены");
                bookDAO.update(item);
            } else {
                bookDAO.create(item);
            }
        } catch (DaoException e) {
            throw new ServiceException(
                    "Failed to add or update book with id " + item.getBookId(), e
            );
        }
    }

    @Override
    public void update(Book item) {
        try {
            bookDAO.update(item);
        } catch (DaoException e) {
            throw new ServiceException(
                    "Failed to update book with id " + item.getBookId(), e
            );
        }
    }

    public List<Book> sortBooks(String criteria) {
        try {
            List<Book> books = bookDAO.getAll();
            switch (criteria.toLowerCase()) {
                case "по алфавиту":
                    books.sort(new LetterComporator());
                    return books;
                case "по цене":
                    books.sort(new PriceComporator());
                    return books;
                case "по году издания":
                    books.sort(new YearComporator());
                    return books;
                case "по наличию на складе":
                    books.sort(new AvailiableComporator());
                    return books;
                default:
                    System.out.println("Ошибка: неопознанный критерий сортировки.");
                    return books;
            }
        } catch (DaoException e) {
            throw new ServiceException(
                    "Failed to sort books by criteria: " + criteria, e
            );
        }
    }

    public void writeOffBook(int bookId) {
        List<Book> books = getAll();
        for (Book b: books) {
            if (b.getBookId() == bookId && b.getStatus().equals(StatusBook.IN_STOCK)) {
                b.setStatus(StatusBook.OUT_OF_STOCK);
                update(b);
                System.out.println("Статус книги изменен на - отсутсвует");
                return;
            }
        }
        System.out.println("Книга с id " + bookId + "  не найдена");
    }
}
