package bookstore.service.entityService;

import bookstore.comporator.book.AvailableComparator;
import bookstore.comporator.book.LetterComporator;
import bookstore.comporator.book.PriceComporator;
import bookstore.comporator.book.YearComporator;
import bookstore.dto.BookRequest;
import bookstore.dto.BookResponse;
import bookstore.enums.OrderStatus;
import bookstore.enums.StatusBook;
import bookstore.exception.DaoException;
import bookstore.exception.DataManagerException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Order;
import bookstore.model.mapper.BookMapper;
import bookstore.repo.BookDAO;

import bookstore.config.LibraryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class BookService {

    private final BookDAO bookDAO;
    private final OrderService orderService;
    private final RequestBookService requestBookService;
    private final LibraryConfig libraryConfig;
    private final BookMapper bookMapper;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    public BookService(BookDAO bookDAO, OrderService orderService, RequestBookService requestBookService,
                       LibraryConfig libraryConfig, BookMapper bookMapper) {
        this.bookDAO = bookDAO;
        this.orderService = orderService;
        this.requestBookService = requestBookService;
        this.libraryConfig = libraryConfig;
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
    public void add(Book item) {
        try {
            Book existing = bookDAO.findById(item.getBookId());
            if (existing != null) {
                bookDAO.update(item);
            } else {
                bookDAO.create(item);
            }
        } catch (DaoException e) {
            throw new ServiceException(
                    "Failed to add book with name: " + item.getName(), e
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
                    logger.debug("Ошибка: неопознанный критерий сортировки.");
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
        try {
            List<Book> books = getAllEntities();
            for (Book b: books) {
                if (b.getBookId() == bookId && b.getStatus().equals(StatusBook.IN_STOCK)) {
                    b.setStatus(StatusBook.OUT_OF_STOCK);
                    update(b);
                    logger.debug("Статус книги изменен на - отсутсвует");
                    return;
                }
            }
            logger.debug("Книга с id {}  не найдена", bookId);
        } catch (DaoException e) {
            throw new ServiceException("Failed writeoff book with id: " + bookId, e);
        }
    }

    @Transactional(readOnly = true)
    public Book getEntityById(int id) {
        try {
            return bookDAO.findById(id);
        } catch (DaoException ex) {
            throw new ServiceException("Failed to get books by id " + id + " ", ex);
        }
    }

    @Transactional(readOnly = true)
    public List<Book> getAllEntities() {
        try {
            return bookDAO.getAll();
        } catch (DaoException ex) {
            throw new ServiceException("Failed to get all books in getAllEntities" + " ", ex);
        }
    }

    @Transactional
    public void addBookToWarehouse(BookRequest item) {
        try {
            Book book = bookMapper.toEntity(item);
            add(book);

            if (libraryConfig.isAutoCloseRequests()) {
                requestBookService.closeRequest(book.getName(), book.getAuthor());
            }

            List<Order> orders = orderService.getAllOrder();
            for (Order order : orders) {
                if (Objects.equals(order.getBook().getName(), book.getName()) &&
                        Objects.equals(order.getBook().getAuthor(), book.getAuthor())
                        && order.getStatus() == OrderStatus.WAITING_FOR_BOOK) {

                    orderService.changeOrderStatus(order.getOrderId(), "новый");

                    logger.info("Заказ #{} теперь активен — книга поступила.", order.getOrderId());
                }
            }
        } catch (Exception ex) {
            throw new ServiceException("Failed to addBookToWarehouse " + item + " ", ex);
        }
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getStaleBooks(int staleMonths) {
        LocalDate staleDate = LocalDate.now().minusMonths(staleMonths);
        try {
            return bookMapper.toBookResponseList(getAllEntities().stream()
                    .filter(book -> isBookStale(book, staleDate))
                    .collect(Collectors.toList()));
        } catch (ServiceException | DataManagerException ex) {
            throw new ServiceException("fail to get stale books ", ex.getCause());
        }
    }

    private boolean isBookStale(Book book, LocalDate staleDate) {
        Date staleDateAsDate = Date.from(staleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        try {
            return getOrdersForBook(book.getBookId()).stream()
                    .noneMatch(order -> {
                        Date orderDate = order.getOrderDate();
                        return orderDate.after(staleDateAsDate) || orderDate.equals(staleDateAsDate);
                    });
        } catch (ServiceException | DataManagerException ex) {
            throw new ServiceException("fail to determinate is book stale ", ex.getCause());
        }
    }

    public List<Order> getOrdersForBook(int bookId) {
        try {
            return orderService.getAllOrder().stream()
                    .filter(order -> order.getBook().getBookId() == bookId)
                    .collect(Collectors.toList());
        } catch (ServiceException ex) {
            throw new ServiceException("fail to get orders For Book ", ex.getCause());
        }
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        try {
            return bookDAO.getAll();
        } catch (DaoException ex) {
            throw new ServiceException("Failed to get all books in getAllBooks" + " ", ex);
        }
    }
}
