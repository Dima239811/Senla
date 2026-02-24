package bookstore.service.entityService;

import bookstore.comporator.book.AvailableComparator;
import bookstore.comporator.book.LetterComporator;
import bookstore.comporator.book.PriceComporator;
import bookstore.comporator.book.YearComporator;
import bookstore.dto.BookRequest;
import bookstore.dto.BookResponse;
import bookstore.enums.StatusBook;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.mapper.BookMapper;
import bookstore.repo.dao.BookDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class BookService {

    private final BookDAO bookDAO;
    private final BookMapper bookMapper;

    @Autowired
    public BookService(BookDAO bookDAO, BookMapper bookMapper) {
        this.bookDAO = bookDAO;
        this.bookMapper = bookMapper;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getAll() {
        try {
            List<Book> books = bookDAO.getAll();
            return bookMapper.toBookResponseList(books);
        } catch (DaoException e) {
            throw new ServiceException("Failed to get all books", e.getCause());
        }
    }

    @Transactional(readOnly = true)
    public BookResponse getById(int id) {
        try {
            Book book = bookDAO.findById(id);
            return bookMapper.toBookResponse(book);
        } catch (DaoException e) {
            throw new ServiceException("Failed to get books by id " + id + " ", e);
        }
    }

    @Transactional
    public void add(BookRequest item) {
        try {
            Book book = bookMapper.toEntity(item);
            bookDAO.create(book);
        } catch (DaoException e) {
            throw new ServiceException(
                    "Failed to add book with name: " + item.name(), e
            );
        }
    }

    @Transactional
    public void update(Book item) {
        try {
            bookDAO.update(item);
        } catch (DaoException e) {
            throw new ServiceException(
                    "Failed to update book with name: " + item.getName(), e
            );
        }
    }

    @Transactional(readOnly = true)
    public List<BookResponse> sortBooks(String criteria) {
        try {
            List<Book> books = bookDAO.getAll();
            switch (criteria.toLowerCase()) {
                case "по алфавиту":
                    books.sort(new LetterComporator());
                    return bookMapper.toBookResponseList(books);
                case "по цене":
                    books.sort(new PriceComporator());
                    return bookMapper.toBookResponseList(books);
                case "по году издания":
                    books.sort(new YearComporator());
                    return bookMapper.toBookResponseList(books);
                case "по наличию на складе":
                    books.sort(new AvailableComparator());
                    return bookMapper.toBookResponseList(books);
                default:
                    System.out.println("Ошибка: неопознанный критерий сортировки.");
                    return bookMapper.toBookResponseList(books);
            }
        } catch (DaoException e) {
            throw new ServiceException(
                    "Failed to sort books by criteria: " + criteria, e
            );
        }
    }

    @Transactional
    public void writeOffBook(int bookId) {
        List<Book> books = getAllEntities();
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

    @Transactional(readOnly = true)
    public Book getEntityById(int id) {
        return bookDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllEntities() {
        return bookDAO.getAll();
    }
}
