package bookstore.controller;

import bookstore.dto.OrderRequest;
import bookstore.dto.OrderResponse;
import bookstore.enums.OrderStatus;
import bookstore.service.entityService.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void createOrder(@RequestBody OrderRequest order) {
        orderService.createOrder(order);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void cancelOrder(@PathVariable int id) {
        orderService.cancelOrder(id);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void changeStatusOrder(@PathVariable("id") int id,
                                  @RequestParam String status) {
        orderService.changeOrderStatus(id, status);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrder() {
        return orderService.getAll();
    }

    @GetMapping("/sort")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> sortOrders(@RequestParam String criteria) {
        return orderService.sortOrders(criteria);
    }

    @GetMapping("/performed")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> sortPerformOrdersForPeriod(
            @RequestParam String criteria,
            @RequestParam
            @DateTimeFormat(pattern = "dd.MM.yyyy") Date from,
            @RequestParam
            @DateTimeFormat(pattern = "dd.MM.yyyy") Date to) {
        return orderService.sortPerformOrders(criteria, from, to);
    }

    @GetMapping("/income")
    @PreAuthorize("hasRole('ADMIN')")
    public double calculateIncomeForPeriod(
            @RequestParam
            @DateTimeFormat(pattern = "dd.MM.yyyy")
            Date from,
            @RequestParam
            @DateTimeFormat(pattern = "dd.MM.yyyy")
            Date to) {
        return orderService.calculateIncomeForPeriod(from, to);
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public int getCountPerformedOrder(
            @RequestParam
            @DateTimeFormat(pattern = "dd.MM.yyyy")
            Date from,
            @RequestParam
            @DateTimeFormat(pattern = "dd.MM.yyyy")
            Date to) {
        return orderService.getCountPerformedOrder(from, to);
    }
}
