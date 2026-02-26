package bookstore.service.entityService;

import bookstore.comporator.order.DateOrderComporator;
import bookstore.comporator.order.PriceOrderComporator;
import bookstore.comporator.order.StatusOrderComporator;
import bookstore.dto.OrderRequest;
import bookstore.dto.OrderResponse;
import bookstore.enums.OrderStatus;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.model.mapper.OrderMapper;
import bookstore.repo.dao.BookDAO;
import bookstore.repo.dao.CustomerDAO;
import bookstore.repo.dao.OrderDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderDAO orderDAO;

    private final BookDAO bookDAO;

    private final CustomerDAO customerDAO;

    private final OrderMapper orderMapper;


    @Autowired
    public OrderService(OrderDAO orderDAO, BookDAO bookDAO, CustomerDAO customerDAO, OrderMapper orderMapper) {
        this.orderDAO = orderDAO;
        this.bookDAO = bookDAO;
        this.customerDAO = customerDAO;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public void cancelOrder(int orderId) {
        try {
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                System.out.println("Заказ с id: " + orderId + " не существует");
                return;
            }
            order.setStatus(OrderStatus.CANCELLED);
            try {
                orderDAO.update(order);
            } catch (DaoException e) {
                throw new ServiceException("Fail to update order status " + orderId + " in orderSevice in cancelOrder()",
                        e);
            }
        } catch (DaoException e) {
            throw new ServiceException("Fail to get order by id " + orderId + " in orderSevice in cancelOrder()", e);
        }
    }

    @Transactional
    public void changeOrderStatus(int orderId, OrderStatus status) {
        try {
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                System.out.println("Заказ с id: " + orderId + " не существует");
                return;
            }
            order.setStatus(status);
            try {
                orderDAO.update(order);
            } catch (DaoException e) {
                throw new ServiceException("Fail to update order status " + orderId +
                        " in orderSevice in changeOrderStatus()", e);
            }
        } catch (DaoException e) {
            throw new ServiceException("Fail to get order by id " +
                    orderId + " in orderSevice in changeOrderStatus()", e);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> sortOrders(String criteria) {
        try {
            List<Order> orders = orderDAO.getAllWithBooksAndCustomers();
            return switch (criteria.toLowerCase()) {
                case "по дате" -> {
                    orders.sort(new DateOrderComporator());
                    yield orderMapper.toOrderResponseList(orders);
                }
                case "по цене" -> {
                    orders.sort(new PriceOrderComporator());
                    yield orderMapper.toOrderResponseList(orders);
                }
                case "по статусу" -> {
                    orders.sort(new StatusOrderComporator());
                    yield orderMapper.toOrderResponseList(orders);
                }
                default -> {
                    System.out.println("Ошибка: неопознанный критерий сортировки.");
                    yield orderMapper.toOrderResponseList(orders);
                }
            };
        } catch (DaoException e) {
            throw new ServiceException("Fail to get all order " +  " in orderSevice in sortOrders()", e);
        }
    }

    private List<Order> filterOrdersByDateAndStatus(Date from, Date to) {
        return orderDAO.getAllWithBooksAndCustomers().stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .filter(order -> !order.getOrderDate().before(from) && !order.getOrderDate().after(to))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> sortPerformOrders(String criteria, Date from, Date to) {
        try {
            List<Order> completedOrders = filterOrdersByDateAndStatus(from, to);
            return switch (criteria.toLowerCase()) {
                case "по дате" -> {
                    completedOrders.sort(new DateOrderComporator());
                    yield orderMapper.toOrderResponseList(completedOrders);
                }
                case "по цене" -> {
                    completedOrders.sort(new PriceOrderComporator());
                    yield orderMapper.toOrderResponseList(completedOrders);
                }
                default -> orderMapper.toOrderResponseList(completedOrders);
            };
        } catch (DaoException e) {
            throw new ServiceException("Fail sortPerformOrders " +  " in orderSevice in sortPerformOrders()", e);
        }
    }

    @Transactional(readOnly = true)
    public double calculateIncomeForPeriod(Date from, Date to) {
        try {
            return filterOrdersByDateAndStatus(from, to)
                    .stream()
                    .mapToDouble(Order::getFinalPrice)
                    .sum();
        } catch (DaoException e) {
            throw new ServiceException("Fail calculateIncomeForPerioud " +
                    " in orderSevice in calculateIncomeForPerioud()", e);
        }
    }

    public int getCountPerformedOrder(Date from, Date to) {
        try {
            return filterOrdersByDateAndStatus(from, to).size();
        } catch (DaoException e) {
            throw new ServiceException("Fail getCountPerformedOrder " +
                    " in orderSevice in getCountPerformedOrder()", e);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAll() {
        try {
            List<Order> orders = orderDAO.getAllWithBooksAndCustomers();
            return orderMapper.toOrderResponseList(orders);
        } catch (DaoException e) {
            throw new ServiceException("Fail getAll " +  " in orderSevice in getAll()", e);
        }
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(int id) {
        try {
            Order order = orderDAO.findById(id);
            return orderMapper.toOrderResponse(order);
        } catch (DaoException e) {
            throw new ServiceException("Fail getById " +  " in orderSevice in getById()", e);
        }
    }

    @Transactional
    public void add(Order item) {

        if (item.getCustomer() == null || item.getCustomer().getCustomerID() <= 0) {
            throw new IllegalArgumentException("Customer must be saved before creating order");
        }

        if (item.getBook() == null || item.getBook().getBookId() <= 0) {
            throw new IllegalArgumentException("Book must be saved before creating order");
        }

        try {
                orderDAO.create(item);
        } catch (DaoException e) {
            throw new ServiceException("Fail add " +  " in orderSevice in add()", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrder() {
        return orderDAO.getAll();
    }

    @Transactional
    public void createOrder(OrderRequest order) {

        Book persistedBook = bookDAO.findById(order.bookId());
        Customer customer = customerDAO.findById(order.customerId());

        Order newOrder = new Order(persistedBook, customer, new Date(), persistedBook.getPrice());

        add(newOrder);
    }
}
