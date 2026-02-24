package bookstore.service;

import bookstore.dto.*;
import bookstore.enums.OrderStatus;
import bookstore.exception.DataManagerException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.model.entity.RequestBook;
import bookstore.model.mapper.BookMapper;
import bookstore.service.entityService.BookService;
import bookstore.service.entityService.CustomerService;
import bookstore.service.entityService.OrderService;
import bookstore.service.entityService.RequestBookService;
import bookstore.util.LibraryConfig;

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
public class InventoryService {

    private final BookService bookService;

    private final OrderService orderService;

    private final RequestBookService requestBookService;

    private final CustomerService customerService;

    private final LibraryConfig libraryConfig;

    private final BookMapper bookMapper;

    @Autowired
    public InventoryService(BookService bookService, OrderService orderService, RequestBookService requestBookService, CustomerService customerService,
                            LibraryConfig libraryConfig, BookMapper bookMapper) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.requestBookService = requestBookService;
        this.customerService = customerService;
        this.libraryConfig = libraryConfig;
        this.bookMapper = bookMapper;
    }

    @Transactional
    public void addBookToWarehouse(BookRequest book) {
        bookService.add(book);

        if (libraryConfig.isAutoCloseRequests()) {
            requestBookService.closeRequest(book.name(), book.author());
        }

        List<Order> orders = orderService.getAllOrder();
        for (Order order : orders) {
            if (Objects.equals(order.getBook().getName(), book.name()) &&
                    Objects.equals(order.getBook().getAuthor(), book.author())
                    && order.getStatus() == OrderStatus.WAITING_FOR_BOOK) {

                orderService.changeOrderStatus(order.getOrderId(), OrderStatus.NEW);

                System.out.println("Заказ #" + order.getOrderId() + " теперь активен — книга поступила.");
            }
        }
    }


    @Transactional(readOnly = true)
    public List<BookResponse> getStaleBooks(int staleMonths) {
        LocalDate staleDate = LocalDate.now().minusMonths(staleMonths);
        try {
            return bookMapper.toBookResponseList(bookService.getAllEntities().stream()
                    .filter(book -> isBookStale(book, staleDate))
                    .collect(Collectors.toList()));
        } catch (ServiceException | DataManagerException ex) {
            throw new DataManagerException("fail to get stale books ", ex.getCause());
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
            throw new DataManagerException("fail to determinate is book stale ", ex.getCause());
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersForBook(int bookId) {
        try {
            return orderService.getAllOrder().stream()
                    .filter(order -> order.getBook().getBookId() == bookId)
                    .collect(Collectors.toList());
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to get orders For Book ", ex.getCause());
        }
    }


    @Transactional
    public void createOrder(OrderRequest order) {

        Book persistedBook = bookService.getEntityById(order.bookId());
        Customer customer = customerService.getEntityById(order.customerId());

        Order newOrder = new Order(persistedBook, customer, new Date(), persistedBook.getPrice());

        orderService.add(newOrder);
    }
//
//    @Transactional
//    public void createRequestForMissingBooks(Customer customer, Book book) {
//        try {
//            customerService.add(customer);
//            RequestBook requestBook = new RequestBook(customer, book);
//            requestBookService.add(requestBook);
//        } catch (Exception e) {
//            throw new DataManagerException("Ошибка при добавлении заявки: " + e.getMessage(), e);
//        }
//    }

    @Transactional
    public void createRequest(RequestBookRequest requestBook) {
        customerService.add(requestBook.customerRequest());
        RequestBook requestBookNew = new RequestBook(customerService.findByEmail(requestBook.customerRequest().email()),
                bookService.getEntityById(requestBook.bookId()));
        requestBookService.add(requestBookNew);
    }
}
