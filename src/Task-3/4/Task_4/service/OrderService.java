package Task_4.service;

import Task_4.enums.OrderStatus;
import Task_4.model.Book;
import Task_4.model.Customer;
import Task_4.model.Order;
import Task_4.model.OrderCol;

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
}
