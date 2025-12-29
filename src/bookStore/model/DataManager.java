package bookStore.model;

import bookStore.dependesies.annotation.Inject;
import bookStore.dependesies.annotation.PostConstruct;
import bookStore.dependesies.annotation.Qualifier;
import bookStore.enums.OrderStatus;
import bookStore.repo.util.DBConnection;
import bookStore.service.entityService.BookService;
import bookStore.service.entityService.CustomerService;
import bookStore.service.entityService.OrderService;
import bookStore.service.entityService.RequestBookService;
import bookStore.service.csv.ICsvService;
import bookStore.util.LibraryConfig;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DataManager {
    @Inject
    private BookService bookService;

    @Inject
    private OrderService orderService;

    @Inject
    private CustomerService customerService;

    @Inject
    private RequestBookService requestService;

    @Inject
    @Qualifier("bookCsvService")
    private ICsvService<Book> bookCsvService;

    @Inject
    @Qualifier("orderCsvService")
    private ICsvService<Order> orderCsvService;

    @Inject
    @Qualifier("customerCsvService")
    private ICsvService<Customer> customerCsvService;

    @Inject
    @Qualifier("requestBookCsvService")
    private ICsvService<RequestBook> requestBookCsvService;

    @Inject
    private LibraryConfig libraryConfig;

    // отладочный метод
    @PostConstruct
    public void postConstruct() {
        System.out.println("Дата менеджер создался!");
    }

    public void writeOffBook(int bookId) {
        bookService.writeOffBook(bookId);
    }

    public Book findBook(int id) {
        return bookService.getById(id);
    }


    public void createOrder(Order order) {
        try {
            DBConnection.getInstance().beginTransaction();

            Book findBook = bookService.getById(order.getBook().getBookId());
            customerService.add(order.getCustomer());

            if (findBook != null) {
                orderService.createOrder(order.getBook(), order.getCustomer(), order.getOrderDate());
            } else {
                requestService.createRequest(order.getBook(), order.getCustomer());
                orderService.createOrderWithStatus(order.getBook(), order.getCustomer(), order.getOrderDate(), OrderStatus.WAITING_FOR_BOOK);
            }


            orderService.add(order);


            DBConnection.getInstance().commit();

        } catch (SQLException e) {
            DBConnection.getInstance().rollback();
            throw new RuntimeException("Ошибка при создании заказа: " + e.getMessage(), e);
        }
    }

    public void cancelOrder(int orderId) {
        orderService.cancelOrder(orderId);
    }

    public void changeStatusOrder(int orderId, OrderStatus status) {
        orderService.changeOrderStatus(orderId, status);
    }

    public void addBookToWareHouse(Book book) {
        bookService.add(book);

        // Включение/отключение возможности помечать заявки как выполненные при добавлении книги на склад.
        if (libraryConfig.isAutoCloseRequests()) {
            requestService.closeRequest(book);
        }
        //requestService.closeRequest(book);

        // Меняем статус всех заказов, ожидавших эту книгу
        for (Order order : orderService.getAll()) {
            if (order.getBook().getBookId() == book.getBookId()
                    && order.getStatus() == OrderStatus.WAITING_FOR_BOOK) {
                orderService.changeOrderStatus(order.getOrderId(), OrderStatus.NEW);
                System.out.println("Заказ #" + order.getOrderId() + " теперь активен — книга поступила.");
            }
        }
    }

    public void addRequest(RequestBook requestBook) {
        try {
            DBConnection.getInstance().beginTransaction();

            //не нужно так как мы всегда берем в заказ книги которые есть уже в базе
            //wareHouseService.add(requestBook.getBook());

            customerService.add(requestBook.getCustomer());
            requestService.add(requestBook);

            DBConnection.getInstance().commit();
        } catch (Exception e) {
            DBConnection.getInstance().rollback();
            throw new RuntimeException("Ошибка при добавлении заявки: " + e.getMessage(), e);
        }
    }

    public List<Book> getAllBooks() {
        return bookService.getAll();
    }

    public List<Customer> getAllCustomers() {
        return customerService.getAll();
    }

    public List<RequestBook> getAllRequestBook() {
        return requestService.getAll();
    }

    public List<Order> getAllOrder() {
        return orderService.getAll();
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


    public void exportBooksToCsv(String filePath) throws Exception {
        List<Book> books = getAllBooks();
        System.out.println("передано на экспорт " + books.size() + " книг");
        bookCsvService.exportToCsv(books, filePath);
    }

    public List<Book> importBooksFromCsv(String filePath) throws Exception {
        try {
            DBConnection.getInstance().beginTransaction();

            List<Book> imported = bookCsvService.importFromCsv(filePath);
            for (Book b : imported) {
                bookService.add(b);
            }

            DBConnection.getInstance().commit();
            return imported;
        } catch (Exception e) {
            DBConnection.getInstance().rollback();
            throw new RuntimeException("fail in data manager importBooksFromCsv " + e.getMessage());
        }
    }

    public void exportOrdersToCsv(String filePath) throws Exception {
        List<Order> orders = getAllOrder();
        orderCsvService.exportToCsv(orders, filePath);
    }

    public List<Order> importOrdersFromCsv(String filePath) throws Exception {
        try {
            DBConnection.getInstance().beginTransaction();

            List<Order> imported = orderCsvService.importFromCsv(filePath);
            for (Order b : imported) {
                orderService.add(b);
                bookService.add(b.getBook());
                customerService.add(b.getCustomer());
            }

            DBConnection.getInstance().commit();
            return imported;
        } catch (Exception e) {
            DBConnection.getInstance().rollback();
            throw new RuntimeException("fail in data manager importOrdersFromCsv " + e.getMessage());
        }
    }

    public void exportCustomersToCsv(String filePath) throws Exception {
        List<Customer> customers = getAllCustomers();
        System.out.println("передано на экспорт " + customers.size() + " клиентов");
        customerCsvService.exportToCsv(customers, filePath);
    }

    public List<Customer> importCustomersFromCsv(String filePath) throws Exception {
        List<Customer> imported = customerCsvService.importFromCsv(filePath);
        for (Customer b: imported) {
            customerService.add(b);
        }
        return imported;
    }

    public void exportRequestToCsv(String filePath) throws Exception {
        List<RequestBook> requestBooks = getAllRequestBook();
        requestBookCsvService.exportToCsv(requestBooks, filePath);
    }

    public List<RequestBook> importRequestFromCsv(String filePath) throws Exception {
        try {
            DBConnection.getInstance().beginTransaction();

            List<RequestBook> imported = requestBookCsvService.importFromCsv(filePath);
            for (RequestBook b : imported) {
                requestService.add(b);
                bookService.add(b.getBook());
                customerService.add(b.getCustomer());
            }

            DBConnection.getInstance().commit();
            return imported;
        } catch (Exception e) {
            DBConnection.getInstance().rollback();
            throw new RuntimeException("fail in data manager importRequestFromCsv " + e.getMessage());
        }
    }

    public List<Book> getStaleBooks(int staleMonths) {
        LocalDate staleDate = LocalDate.now().minusMonths(staleMonths);
        return getAllBooks().stream()
                .filter(book -> isBookStale(book, staleDate))
                .collect(Collectors.toList());
    }

    private boolean isBookStale(Book book, LocalDate staleDate) {
        Date staleDateAsDate = Date.from(staleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return getOrdersForBook(book.getBookId()).stream()
                .noneMatch(order -> {
                    Date orderDate = order.getOrderDate();
                    return orderDate.after(staleDateAsDate) || orderDate.equals(staleDateAsDate);
                });
    }

    public List<Order> getOrdersForBook(int bookId) {
        return orderService.getAll().stream()
                .filter(order -> order.getBook().getBookId() == bookId)
                .collect(Collectors.toList());
    }

    public void addCustomer(Customer customer) {
        customerService.add(customer);
    }


//    public void saveStateToJson(String filePath) {
//        AppState state = new AppState(
//                getAllBooks(),
//                getAllOrder(),
//                getAllCustomers(),
//                getAllRequestBook()
//        );
//        JsonUtil.saveState(state, filePath);
//    }
//
//    public void loadStateFromJson(String filePath) {
//        AppState state = JsonUtil.loadState(filePath);
//        if (state == null) return;
//
//        for (Book book : state.getBooks()) {
//            bookService.addBook(book);
//        }
//        for (Customer customer : state.getCustomers()) {
//            customerService.addCustomer(customer);
//        }
//        for (RequestBook request : state.getRequests()) {
//            requestService.addRequest(request);
//        }
//        for (Order order : state.getOrders()) {
//            orderService.addOrder(order);
//        }
//
//        int maxBookId = state.getBooks().stream()
//                .mapToInt(Book::getBookId)
//                .max()
//                .orElse(0);
//        Book.setCount(maxBookId + 1);
//
//        int maxCustomerId = state.getCustomers().stream()
//                .mapToInt(Customer::getCustomerID)
//                .max().orElse(0);
//        Customer.setCount(maxCustomerId + 1);
//
//        int maxOrderId = state.getOrders().stream()
//                .mapToInt(Order::getOrderId)
//                .max().orElse(0);
//        Order.setCount(maxOrderId + 1);
//
//        int maxRequestId = state.getRequests().stream()
//                .mapToInt(RequestBook::getRequestId)
//                .max().orElse(0);
//        RequestBook.setCount(maxRequestId + 1);
//
//    }
}
