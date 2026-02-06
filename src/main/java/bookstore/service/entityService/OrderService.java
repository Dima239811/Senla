package bookstore.service.entityService;

import bookstore.comporator.order.DateOrderComporator;
import bookstore.comporator.order.PriceOrderComporator;
import bookstore.comporator.order.StatusOrderComporator;
import bookstore.dependesies.annotation.Inject;
import bookstore.enums.OrderStatus;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.repo.dao.OrderDAO;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService implements IService<Order> {

    @Inject
    private OrderDAO orderDAO;

    public void createOrder(Book book, Customer customer, Date orderDate) {
        Order order = new Order(book, customer, orderDate, book.getPrice());
        add(order);
    }

    public void createOrderWithStatus(Book book, Customer customer, Date orderDate, OrderStatus status) {
        Order order = new Order(book, customer, orderDate, book.getPrice(), status);
        add(order);
    }

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

    public List<Order> sortOrders(String criteria) {
        try {
            List<Order> orders = orderDAO.getAllWithBooksAndCustomers();
            switch (criteria.toLowerCase()) {
                case "по дате":
                    orders.sort(new DateOrderComporator());
                    return orders;
                case "по цене":
                    orders.sort(new PriceOrderComporator());
                    return orders;
                case "по статусу":
                    orders.sort(new StatusOrderComporator());
                    return orders;
                default:
                    System.out.println("Ошибка: неопознанный критерий сортировки.");
                    return orders;
            }
        } catch (DaoException e) {
            throw new ServiceException("Fail to get all order " +  " in orderSevice in sortOrders()", e);
        }
    }

    private List<Order> filterOrdersByDateAndStatus(Date from, Date to, OrderStatus status) {
        return orderDAO.getAllWithBooksAndCustomers().stream()
                .filter(order -> order.getStatus() == status)
                .filter(order -> !order.getOrderDate().before(from) && !order.getOrderDate().after(to))
                .collect(Collectors.toList());
    }

    public List<Order> sortPerformOrders(String criteria, Date from, Date to) {
        try {
            List<Order> completedOrders = filterOrdersByDateAndStatus(from, to, OrderStatus.COMPLETED);
            //System.out.println("выполненные заказы в методе " + completedOrders);
            switch (criteria.toLowerCase()) {
                case "по дате":
                    completedOrders.sort(new DateOrderComporator());
                    return completedOrders;
                case "по цене":
                    completedOrders.sort(new PriceOrderComporator());
                    return completedOrders;
                default:
                    return completedOrders;
            }
        } catch (DaoException e) {
            throw new ServiceException("Fail sortPerformOrders " +  " in orderSevice in sortPerformOrders()", e);
        }
    }

    public double calculateIncomeForPerioud(Date from, Date to) {
        try {
            return filterOrdersByDateAndStatus(from, to, OrderStatus.COMPLETED)
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
            return filterOrdersByDateAndStatus(from, to, OrderStatus.COMPLETED).size();
        } catch (DaoException e) {
            throw new ServiceException("Fail getCountPerformedOrder " +
                    " in orderSevice in getCountPerformedOrder()", e);
        }
    }


    @Override
    public List<Order> getAll() {
        try {
            return orderDAO.getAllWithBooksAndCustomers();
        } catch (DaoException e) {
            throw new ServiceException("Fail getAll " +  " in orderSevice in getAll()", e);
        }
    }

    @Override
    public Order getById(int id) {
        try {
            return orderDAO.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Fail getById " +  " in orderSevice in getById()", e);
        }
    }

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

    @Override
    public void update(Order item) {
        try {
            orderDAO.update(item);
        } catch (DaoException e) {
            throw new ServiceException("Fail update " +  " in orderSevice in update()", e);
        }
    }
}
