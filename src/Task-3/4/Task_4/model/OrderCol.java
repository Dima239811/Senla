package Task_4.model;

import Task_4.comporator.order.DateOrderComporator;
import Task_4.comporator.order.PriceOrderComporator;
import Task_4.comporator.order.StatusOrderComporator;
import Task_4.enums.OrderStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class OrderCol {
    private List<Order> orderList;

    public OrderCol() {
        this.orderList = new ArrayList<>();
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void addOrder(Book book, Customer customer, Date orderDate) {
        Order order = new Order(book, customer, orderDate, book.getPrice());
        orderList.add(order);
        System.out.println("заказ создан");
    }

    public void addOrderWithStatus(Book book, Customer customer, Date orderDate, OrderStatus orderStatus) {
        Order order = new Order(book, customer, orderDate, book.getPrice(), orderStatus);
        orderList.add(order);
    }

    public void changeStatus(int orderId, OrderStatus status) {
        for (Order ord: orderList) {
            if (ord.getOrderId() == orderId) {
                ord.setStatus(status);
                System.out.println("указанный вами статус " + status + " установлен");
                return;
            }
        }
    }

    public Order findOrder(int id) {
        for (Order b: orderList) {
            if (b.getOrderId() == id) {
                return b;
            }
        }
        return null;
    }


    public List<Order> sortByDate() {
        orderList.sort(new DateOrderComporator());
        return orderList;
    }

    public List<Order> sortByPrice() {
        orderList.sort(new PriceOrderComporator());
        return orderList;
    }

    public List<Order> sortByStatus() {
        orderList.sort(new StatusOrderComporator());
        return orderList;
    }

    private List<Order> filterOrdersByDateAndStatus(Date from, Date to, OrderStatus status) {
        return orderList.stream()
                .filter(order -> order.getStatus().equals(status))
                .filter(order -> !order.getOrderDate().before(from) && !order.getOrderDate().after(to))
                .collect(Collectors.toList());
    }

    public List<Order> sortPerformOrderByDate(Date from, Date to) {
        return filterOrdersByDateAndStatus(from, to, OrderStatus.COMPLETED)
                .stream()
                .sorted(new DateOrderComporator())
                .collect(Collectors.toList());
    }

    public List<Order> sortPerformOrderByPrice(Date from, Date to) {
        return filterOrdersByDateAndStatus(from, to, OrderStatus.COMPLETED)
                .stream()
                .sorted(new PriceOrderComporator())
                .collect(Collectors.toList());
    }

    public double calculateIncomeForPerioud(Date from, Date to) {
        return filterOrdersByDateAndStatus(from, to, OrderStatus.COMPLETED)
                .stream()
                .mapToDouble(Order::getFinalPrice)
                .sum();
    }
}
