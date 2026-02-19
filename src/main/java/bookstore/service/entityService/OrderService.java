package bookstore.service.entityService;

import bookstore.comporator.order.DateOrderComporator;
import bookstore.comporator.order.PriceOrderComporator;
import bookstore.comporator.order.StatusOrderComporator;
import bookstore.enums.OrderStatus;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Order;
import bookstore.repo.dao.OrderDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements IService<Order> {
    private final OrderDAO orderDAO;

    @Autowired
    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
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
    public List<Order> sortOrders(String criteria) {
        try {
            List<Order> orders = orderDAO.getAllWithBooksAndCustomers();
            return switch (criteria.toLowerCase()) {
                case "по дате" -> {
                    orders.sort(new DateOrderComporator());
                    yield orders;
                }
                case "по цене" -> {
                    orders.sort(new PriceOrderComporator());
                    yield orders;
                }
                case "по статусу" -> {
                    orders.sort(new StatusOrderComporator());
                    yield orders;
                }
                default -> {
                    System.out.println("Ошибка: неопознанный критерий сортировки.");
                    yield orders;
                }
            };
        } catch (DaoException e) {
            throw new ServiceException("Fail to get all order " +  " in orderSevice in sortOrders()", e);
        }
    }

    @Transactional(readOnly = true)
    private List<Order> filterOrdersByDateAndStatus(Date from, Date to) {
        return orderDAO.getAllWithBooksAndCustomers().stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .filter(order -> !order.getOrderDate().before(from) && !order.getOrderDate().after(to))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Order> sortPerformOrders(String criteria, Date from, Date to) {
        try {
            List<Order> completedOrders = filterOrdersByDateAndStatus(from, to);
            return switch (criteria.toLowerCase()) {
                case "по дате" -> {
                    completedOrders.sort(new DateOrderComporator());
                    yield completedOrders;
                }
                case "по цене" -> {
                    completedOrders.sort(new PriceOrderComporator());
                    yield completedOrders;
                }
                default -> completedOrders;
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
    @Override
    public List<Order> getAll() {
        try {
            return orderDAO.getAllWithBooksAndCustomers();
        } catch (DaoException e) {
            throw new ServiceException("Fail getAll " +  " in orderSevice in getAll()", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Order getById(int id) {
        try {
            return orderDAO.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Fail getById " +  " in orderSevice in getById()", e);
        }
    }

    @Transactional
    @Override
    public void add(Order item) {

        if (item.getCustomer() == null || item.getCustomer().getCustomerID() <= 0) {
            throw new IllegalArgumentException("Customer must be saved before creating order");
        }

        if (item.getBook() == null || item.getBook().getBookId() <= 0) {
            throw new IllegalArgumentException("Book must be saved before creating order");
        }

        try {
            Order existing = orderDAO.findById(item.getOrderId());
            System.out.println("ID КЛИЕНТА в ордер сервис =  " + item.getCustomer().getCustomerID());
            if (existing != null) {
                update(item);
            } else {
                orderDAO.create(item);
            }
        } catch (DaoException e) {
            throw new ServiceException("Fail add " +  " in orderSevice in add()", e);
        }
    }

    @Transactional
    @Override
    public void update(Order item) {
        try {
            orderDAO.update(item);
        } catch (DaoException e) {
            throw new ServiceException("Fail update " +  " in orderSevice in update()", e);
        }
    }
}
