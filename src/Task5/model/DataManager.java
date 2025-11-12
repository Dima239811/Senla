package Task5.model;

import Task5.enums.OrderStatus;
import Task5.service.BookService;
import Task5.service.CustomerService;
import Task5.service.OrderService;
import Task5.service.RequestBookService;

import java.util.Date;
import java.util.List;

public class DataManager {
    private static final DataManager INSTANCE = new DataManager();
    private final BookService bookService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final RequestBookService requestService;

    private DataManager() {
        this.bookService = new BookService();
        this.orderService = new OrderService();
        this.customerService = new CustomerService();
        this.requestService = new RequestBookService();
    }

    public static DataManager getInstance() {
        return INSTANCE;
    }

    public void writeOffBook(int bookId) {
        bookService.writeOffBook(bookId);
    }

    public Book findBook(int id) {
        return bookService.findBook(id);
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


    public List<Order> sortOrders(String criteria) {
        return orderService.sortOrders(criteria);
    }

    public List<Book> sortBooks(String criteria) {
        return bookService.sortBooks(criteria);
    }

    public List<RequestBook> sortRequest(String criteria) {
        return requestService.sortRequest(criteria);
    }

    public List<Order> sortPerformOrdersForPeriod(String criteria, Date from, Date to) {
        return orderService.sortPerformOrders(criteria, from, to);
    }

    public double calculateIncomeForPeriod(Date from, Date to) {
        return orderService.calculateIncomeForPerioud(from, to);
    }

    public int getCountPerformedOrder(Date from, Date to) {
        return orderService.getCountPerformedOrder(from ,to);
    }
}
