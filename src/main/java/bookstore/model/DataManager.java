package bookstore.model;

import bookstore.dependesies.annotation.Inject;
import bookstore.dependesies.annotation.PostConstruct;
import bookstore.dependesies.annotation.Qualifier;
import bookstore.enums.OrderStatus;
import bookstore.repo.util.DBConnection;
import bookstore.service.entityService.BookService;
import bookstore.service.entityService.CustomerService;
import bookstore.service.entityService.OrderService;
import bookstore.service.entityService.RequestBookService;
import bookstore.service.csv.ICsvService;
import bookstore.util.LibraryConfig;

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


    // транзакция
    public void createOrder(Order order) {
        try {
            DBConnection.getInstance().beginTransaction();
            customerService.add(order.getCustomer());

            Book findBook = bookService.getById(order.getBook().getBookId());

            if (findBook == null) {
                requestService.createRequest(order.getBook(), order.getCustomer());
                order.setStatus(OrderStatus.WAITING_FOR_BOOK);
            } else {
                order.setStatus(OrderStatus.NEW);
            }

            orderService.add(order);

            DBConnection.getInstance().commit();
        } catch (Exception e) {
            DBConnection.getInstance().rollback();
            throw new RuntimeException("Ошибка при создании заказа: " + e.getMessage(), e);
        }
    }

    // транзакция
    public void cancelOrder(int orderId) {
        try {
            DBConnection.getInstance().beginTransaction();

            orderService.cancelOrder(orderId);

            DBConnection.getInstance().commit();
        } catch (Exception e) {
            DBConnection.getInstance().rollback();
            throw new RuntimeException("Ошибка при отмене заказа: " + e.getMessage(), e);
        }
    }

    // транзакция
    public void changeStatusOrder(int orderId, OrderStatus status) {
        try {
            DBConnection.getInstance().beginTransaction();

            orderService.changeOrderStatus(orderId, status);

            DBConnection.getInstance().commit();
        } catch (Exception e) {
            DBConnection.getInstance().rollback();
            throw new RuntimeException("Ошибка при изменении статуса заказа: " + e.getMessage(), e);
        }
    }

    // транзакция
    public void addBookToWareHouse(Book book) {
        try {
            DBConnection.getInstance().beginTransaction();
            bookService.add(book);

            // Включение/отключение возможности помечать заявки как выполненные при добавлении книги на склад.
            if (libraryConfig.isAutoCloseRequests()) {
                requestService.closeRequest(book);
            }

            // Меняем статус всех заказов, ожидавших эту книгу
            for (Order order : orderService.getAll()) {
                if (order.getBook().getBookId() == book.getBookId()
                        && order.getStatus() == OrderStatus.WAITING_FOR_BOOK) {
                    orderService.changeOrderStatus(order.getOrderId(), OrderStatus.NEW);
                    System.out.println("Заказ #" + order.getOrderId() + " теперь активен — книга поступила.");
                }
            }

            DBConnection.getInstance().commit();
        } catch (Exception e) {
            DBConnection.getInstance().rollback();
            throw new RuntimeException("Ошибка при добавлении книги на склад: " + e.getMessage(), e);
        }
    }

    // транзакция
    public void addRequest(RequestBook requestBook) {
        try {
            DBConnection.getInstance().beginTransaction();


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
        return orderService.getCountPerformedOrder(from, to);
    }

    public void exportBooksToCsv(String filePath) throws Exception {
        List<Book> books = getAllBooks();
        System.out.println("передано на экспорт " + books.size() + " книг");
        bookCsvService.exportToCsv(books, filePath);
    }

    // транзакция
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

    // транзакция
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

    // транзакция
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

    // транзакция
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
