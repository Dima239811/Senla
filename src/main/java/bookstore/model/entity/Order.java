package bookstore.model.entity;

import bookstore.enums.OrderStatus;
import bookstore.model.converters.OrderStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "bookid", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "customerid", nullable = false)
    private Customer customer;

    @Column(name = "dateorder", nullable = false)
    private Date orderDate;

    @Column(name = "price", nullable = false)
    private double finalPrice;

    @Convert(converter = OrderStatusConverter.class)
    @Column(nullable = false)
    private OrderStatus status;  // создан/ выполнен/ отменен

    public Order() { }

    public Order(Book book, Customer customer, Date orderDate, double finalPrice) {
        this.book = book;
        this.customer = customer;
        this.orderDate = orderDate;
        this.finalPrice = finalPrice;
        this.status = OrderStatus.NEW;
    }

    public Order(Book book, Customer customer, Date orderDate, double finalPrice, OrderStatus status, int orderId) {
        this.book = book;
        this.customer = customer;
        this.orderDate = orderDate;
        this.finalPrice = finalPrice;
        this.status = status;
        this.orderId = orderId;
    }

    public Order(Book book, Customer customer, Date orderDate, double finalPrice, OrderStatus status) {
        this.book = book;
        this.customer = customer;
        this.orderDate = orderDate;
        this.finalPrice = finalPrice;
        this.status = status;
    }


    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", book=" + book +
                ", customer=" + customer +
                ", orderDate=" + orderDate +
                ", finalPrice=" + finalPrice +
                ", status=" + status +
                '}';
    }
}
