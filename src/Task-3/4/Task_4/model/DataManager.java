package Task_4.model;

import Task_4.enums.OrderStatus;
import Task_4.service.CustomerService;
import Task_4.service.OrderService;
import Task_4.service.RequestBookService;
import Task_4.service.BookService;

import java.util.Date;
import java.util.List;

public class DataManager {
    private final BookService bookService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final RequestBookService requestService;

    public DataManager() {
        this.bookService = new BookService();
        this.orderService = new OrderService();
        this.customerService = new CustomerService();
        this.requestService = new RequestBookService();
    }

    public void writeOffBook(int bookId) {
        bookService.writeOffBook(bookId);
    }

    public void createOrder(Book book, Customer customer, Date orderDate) {
        // проверка на наличие книги
        Book findBook = bookService.findBook(book.getBookId());

        if (findBook != null) {
            orderService.createOrder(book, customer, orderDate);
        } else {
            requestService.addRequest(customer, book);
            orderService.createOrderWithStatus(book, customer, orderDate, OrderStatus.WAITING_FOR_BOOK);
        }

    }

    public void cancelOrder(int orderId) {
        orderService.cancelOrder(orderId);
    }

    public void changeStatusOrder(int orderId, OrderStatus status) {
        orderService.changeOrderStatus(orderId, status);
    }

    public void addBookToWareHouse(Book book) {
        if (bookService.addBook(book)) {
            requestService.closeRequest(book);

            // Меняем статус всех заказов, ожидавших эту книгу
            for (Order order : orderService.getAllOrder()) {
                if (order.getBook().getBookId() == book.getBookId()
                        && order.getStatus() == OrderStatus.WAITING_FOR_BOOK) {
                    orderService.changeOrderStatus(order.getOrderId(), OrderStatus.NEW);
                    System.out.println("Заказ #" + order.getOrderId() + " теперь активен — книга поступила.");
                }
            }
        }
    }

    public void addRequest(Book book, Customer customer) {
        requestService.addRequest(customer, book);
    }

    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
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
