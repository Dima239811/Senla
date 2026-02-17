package bookstore.controller;

import bookstore.enums.OrderStatus;
import bookstore.model.entity.Order;
import bookstore.service.entityService.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void createOrder(Order order) {
        orderService.createOrder(order);
    }

    public void cancelOrder(int orderId) {
        orderService.cancelOrder(orderId);
    }

    public void changeStatusOrder(int orderId, OrderStatus status) {
        orderService.changeOrderStatus(orderId, status);
    }

    public List<Order> getAllOrder() {
        return orderService.getAll();
    }

    public List<Order> sortOrders(String criteria) {
        return orderService.sortOrders(criteria);
    }

    public List<Order> sortPerformOrdersForPeriod(String criteria, Date from, Date to) {
        return orderService.sortPerformOrders(criteria, from, to);
    }

    public double calculateIncomeForPeriod(Date from, Date to) {
        return orderService.getCountPerformedOrder(from, to);
    }

    public int getCountPerformedOrder(Date from, Date to) {
        return orderService.getCountPerformedOrder(from, to);
    }
}
