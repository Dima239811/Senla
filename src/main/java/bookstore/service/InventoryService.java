package bookstore.service;

import bookstore.enums.OrderStatus;
import bookstore.exception.DataManagerException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Order;
import bookstore.service.entityService.BookServiceImpl;
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
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final BookServiceImpl bookServiceImpl;

    private final OrderService orderService;

    private final RequestBookService requestBookService;

    private final LibraryConfig libraryConfig;

    @Autowired
    public InventoryService(BookServiceImpl bookServiceImpl, OrderService orderService, RequestBookService requestBookService,
                            LibraryConfig libraryConfig) {
        this.bookServiceImpl = bookServiceImpl;
        this.orderService = orderService;
        this.requestBookService = requestBookService;
        this.libraryConfig = libraryConfig;
    }

    @Transactional
    public void addBookToWarehouse(Book book) {
        bookServiceImpl.add(book);

        if (libraryConfig.isAutoCloseRequests()) {
            requestBookService.closeRequest(book);
        }

        List<Order> orders = orderService.getAll();
        for (Order order : orders) {
            if (order.getBook().getBookId() == book.getBookId()
                    && order.getStatus() == OrderStatus.WAITING_FOR_BOOK) {

                orderService.changeOrderStatus(order.getOrderId(), OrderStatus.NEW);

                System.out.println("Заказ #" + order.getOrderId() + " теперь активен — книга поступила.");
            }
        }
    }


    public List<Book> getStaleBooks(int staleMonths) {
        LocalDate staleDate = LocalDate.now().minusMonths(staleMonths);
        try {
            return bookServiceImpl.getAll().stream()
                    .filter(book -> isBookStale(book, staleDate))
                    .collect(Collectors.toList());
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

    public List<Order> getOrdersForBook(int bookId) {
        try {
            return orderService.getAll().stream()
                    .filter(order -> order.getBook().getBookId() == bookId)
                    .collect(Collectors.toList());
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to get orders For Book ", ex.getCause());
        }
    }
}
