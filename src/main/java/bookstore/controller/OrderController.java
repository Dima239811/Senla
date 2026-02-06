package bookstore.controller;

import bookstore.enums.OrderStatus;
import bookstore.model.entity.Order;
import bookstore.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

@Controller
public class OrderController {
    private final ApplicationService applicationService;

    @Autowired
    public OrderController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public void createOrder(Order order) {
        applicationService.createOrder(order);
    }

    public void cancelOrder(int orderId) {
        applicationService.cancelOrder(orderId);
    }

    public void changeStatusOrder(int orderId, OrderStatus status) {
        applicationService.changeStatusOrder(orderId, status);
    }

    public List<Order> getAllOrder() {
        return applicationService.getAllOrder();
    }

    public List<Order> sortOrders(String criteria) {
        return applicationService.sortOrders(criteria);
    }

    public List<Order> sortPerformOrdersForPeriod(String criteria, Date from, Date to) {
        return applicationService.sortPerformOrdersForPeriod(criteria, from, to);
    }

    public double calculateIncomeForPeriod(Date from, Date to) {
        return applicationService.getCountPerformedOrder(from, to);
    }

    public int getCountPerformedOrder(Date from, Date to) {
        return applicationService.getCountPerformedOrder(from, to);
    }
}
