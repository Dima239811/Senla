package Task_4.model;

import Task_4.enums.OrderStatus;
import Task_4.service.CustomerService;
import Task_4.service.OrderService;
import Task_4.service.RequestBookService;
import Task_4.service.BookService;

import java.util.Date;
import java.util.List;

public class DataManager {
    private final BookService wareHouseService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final RequestBookService requestService;

    public DataManager() {
        this.wareHouseService = new BookService();
        this.orderService = new OrderService();
        this.customerService = new CustomerService();
        this.requestService = new RequestBookService();
    }

    public void writeOffBook(int bookId) {
        wareHouseService.writeOffBook(bookId);
    }

    public void createOrder(Book book, Customer customer, Date orderDate) {
        orderService.createOrder(book, customer, orderDate);
    }

    public void cancelOrder(int orderId) {
        orderService.cancelOrder(orderId);
    }

    public void changeStatusOrder(int orderId, OrderStatus status) {
        orderService.changeOrderStatus(orderId, status);
    }

    public void addBookToWareHouse(Book book) {
        if (wareHouseService.addBook(book)) {
            requestService.closeRequest(book);
        }
    }

    public void addRequest(Book book, Customer customer) {
        requestService.addRequest(customer, book);
    }

    public List<Book> getAllBooks() {
        return wareHouseService.getAllBooks();
    }

    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void setCustomers(List<Customer> customers) {
        customerService.setCustomers(customers);
    }

    public List<RequestBook> getAllRequestBook() {
        return requestService.getAllRequestBook();
    }

    public List<Order> getAllOrder() {
        return orderService.getAllOrder();
    }
}
