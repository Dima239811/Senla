package Task5.service;

import Task5.enums.OrderStatus;
import Task5.model.Book;
import Task5.model.Customer;
import Task5.model.Order;
import Task5.model.OrderCol;

import java.util.Date;
import java.util.List;

public class OrderService {
    private final OrderCol orderCol;

    public OrderService() {
        this.orderCol = new OrderCol();
    }

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
}
