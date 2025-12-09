package Task8.service;

import Task8.dependesies.annotation.Inject;
import Task8.enums.OrderStatus;
import Task8.model.Book;
import Task8.model.Customer;
import Task8.model.Order;
import Task8.model.OrderCol;

import java.util.Date;
import java.util.List;

public class OrderService {

    @Inject
    private OrderCol orderCol;

    public void createOrder(Book book, Customer customer, Date orderDate) {
        orderCol.addOrder(book, customer, orderDate);
    }

    public void createOrderWithStatus(Book book, Customer customer, Date orderDate, OrderStatus status) {
        orderCol.addOrderWithStatus(book, customer, orderDate, status);
    }


    public void cancelOrder(int orderId) {
        orderCol.changeStatus(orderId, OrderStatus.CANCELLED);
    }

    public void changeOrderStatus(int orderId, OrderStatus status) {
        Order order = orderCol.findOrder(orderId);

        if (order.getStatus() == OrderStatus.WAITING_FOR_BOOK && status == OrderStatus.COMPLETED) {
            System.out.println("Нельзя завершить заказ: книга ещё не поступила.");
            return;
        }

        orderCol.changeStatus(orderId, status);
    }

    public OrderCol getOrderCol() {
        return orderCol;
    }

    public List<Order> getAllOrder() {
        return orderCol.getOrderList();
    }


    public List<Order> sortOrders(String criteria) {
        switch (criteria.toLowerCase()) {
            case "по дате":
                return orderCol.sortByDate();
            case "по цене":
                return orderCol.sortByPrice();
            case "по статусу":
                return orderCol.sortByStatus();
            default:
                System.out.println("Ошибка: неопознанный критерий сортировки.");
                return orderCol.getOrderList();
        }
    }

    public List<Order> sortPerformOrders(String criteria, Date from, Date to) {
        if (criteria.equals("по дате")) {
            return orderCol.sortPerformOrderByDate(from, to);
        } else if (criteria.equals("по цене")) {
            return orderCol.sortPerformOrderByPrice(from, to);
        } else {
            return orderCol.getOrderList();
        }
    }

    public double calculateIncomeForPerioud(Date from, Date to) {
        return orderCol.calculateIncomeForPerioud(from, to);
    }

    public int getCountPerformedOrder(Date from, Date to) {
        return orderCol.getCountPerformedOrder(from ,to);
    }

    public void addOrder(Order order) {
        orderCol.addOrder(order);
    }

    public Order findOrder(int id) {
        return orderCol.findOrder(id);
    }
}
