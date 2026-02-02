package bookstore.model;

import bookstore.enums.OrderStatus;
import bookstore.exception.DataExportException;
import bookstore.exception.DataImportException;
import bookstore.exception.DataManagerException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.model.entity.RequestBook;
import bookstore.repo.util.HibernateUtil;
import bookstore.service.entityService.BookService;
import bookstore.service.entityService.CustomerService;
import bookstore.service.entityService.OrderService;
import bookstore.service.entityService.RequestBookService;
import bookstore.service.csv.ICsvService;
import bookstore.util.LibraryConfig;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataManager {
    @Autowired
    private BookService bookService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RequestBookService requestService;

    @Autowired
    @Qualifier("bookCsvService")
    private ICsvService<Book> bookCsvService;

    @Autowired
    @Qualifier("orderCsvService")
    private ICsvService<Order> orderCsvService;

    @Autowired
    @Qualifier("customerCsvService")
    private ICsvService<Customer> customerCsvService;

    @Autowired
    @Qualifier("requestBookCsvService")
    private ICsvService<RequestBook> requestBookCsvService;

    @Autowired
    private LibraryConfig libraryConfig;

    // отладочный метод
    @PostConstruct
    public void postConstruct() {
        System.out.println("Дата менеджер создался!");
    }

    // транзакция
    public void writeOffBook(int bookId) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            try {
                bookService.writeOffBook(bookId);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new DataManagerException("Ошибка при добавлении книги на склад", e);
            }
        }
    }

    public Book findBook(int id) {
        try {
            return bookService.getById(id);
        } catch (ServiceException ex) {
            throw new DataManagerException("fail find book ", ex.getCause());
        }
    }

    // транзакция
    public void createOrder(Order order) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            try {
                customerService.add(order.getCustomer());

                Book findBook = bookService.getById(order.getBook().getBookId());

                if (findBook == null) {
                    requestService.createRequest(order.getBook(), order.getCustomer());
                    order.setStatus(OrderStatus.WAITING_FOR_BOOK);
                } else {
                    order.setStatus(OrderStatus.NEW);
                }

                orderService.add(order);
                tx.commit();
            } catch (Exception e) {
            tx.rollback();
            throw new DataManagerException("Ошибка при создании заказа", e);
        }
    }
    }

    // транзакция
    public void cancelOrder(int orderId) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            try {
                orderService.cancelOrder(orderId);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new DataManagerException("Ошибка при отмене заказа: " + e.getMessage(), e);
            }
        }
    }

    // транзакция
    public void changeStatusOrder(int orderId, OrderStatus status) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            try {
                orderService.changeOrderStatus(orderId, status);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new DataManagerException("Ошибка при изменении статуса заказа: " + e.getMessage(), e);
            }
        }
    }

    // транзакция
    public void addBookToWareHouse(Book book) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            try {
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
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new DataManagerException("Ошибка при добавлении книги на склад", e);
            }
        }
    }

    // транзакция
    public void addRequest(RequestBook requestBook) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            try {
                customerService.add(requestBook.getCustomer());
                requestService.add(requestBook);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new DataManagerException("Ошибка при добавлении заявки: " + e.getMessage(), e);
            }
        }
    }

    public List<Book> getAllBooks() {
        try {
            return bookService.getAll();
        } catch (ServiceException e) {
            throw new DataManagerException("Не удалось получить список книг", e);
        }
    }

    public List<Customer> getAllCustomers() {
        try {
            return customerService.getAll();
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to get all customers ", ex.getCause());
        }
    }

    public List<RequestBook> getAllRequestBook() {
        try {
            return requestService.getAll();
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to get all request book", ex.getCause());
        }
    }

    public List<Order> getAllOrder() {
        try {
            return orderService.getAll();
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to get all order ", ex.getCause());
        }
    }

    public List<Order> sortOrders(String criteria) {
        try {
            return orderService.sortOrders(criteria);
        } catch (ServiceException ex) {
            throw new DataManagerException("fail sort orders ", ex.getCause());
        }
    }

    public List<Book> sortBooks(String criteria) {
        try {
            return bookService.sortBooks(criteria);
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to sort books", ex.getCause());
        }
    }

    public List<RequestBook> sortRequest(String criteria) {
        try {
            return requestService.sortRequest(criteria);
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to sort requests", ex.getCause());
        }
    }

    public List<Order> sortPerformOrdersForPeriod(String criteria, Date from, Date to) {
        try {
            return orderService.sortPerformOrders(criteria, from, to);
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to sort PerformOrdersForPeriod", ex.getCause());
        }
    }

    public double calculateIncomeForPeriod(Date from, Date to) {
        try {
            return orderService.calculateIncomeForPeriod(from, to);
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to calculate income for period", ex.getCause());
        }
    }

    public int getCountPerformedOrder(Date from, Date to) {
        try {
            return orderService.getCountPerformedOrder(from, to);
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to get count performed order", ex.getCause());
        }
    }

    public void exportBooksToCsv(String filePath) throws DataExportException {
        List<Book> books = getAllBooks();
        System.out.println("передано на экспорт " + books.size() + " книг");
        bookCsvService.exportToCsv(books, filePath);
    }

    // транзакция
    public List<Book> importBooksFromCsv(String filePath) throws DataImportException {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            try {
                List<Book> imported = bookCsvService.importFromCsv(filePath);
                for (Book b : imported) {
                    bookService.add(b);
                }

                tx.commit();
                return imported;
            } catch (DataImportException e) {
                tx.rollback();
                throw e;
            } catch (Exception e) {
                tx.rollback();
                throw new DataManagerException("fail in data manager importBooksFromCsv", e);
            }
        }
    }

    public void exportOrdersToCsv(String filePath) throws DataExportException {
        List<Order> orders = getAllOrder();
        orderCsvService.exportToCsv(orders, filePath);
    }

    // транзакция
    public List<Order> importOrdersFromCsv(String filePath) throws DataImportException {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            try {
                List<Order> imported = orderCsvService.importFromCsv(filePath);
                for (Order b : imported) {
                    orderService.add(b);
                    bookService.add(b.getBook());
                    customerService.add(b.getCustomer());
                }

                tx.commit();
                return imported;
            } catch (DataImportException e) {
                tx.rollback();
                throw e;
            } catch (Exception e) {
                tx.rollback();
                throw new DataManagerException("fail in data manager importOrdersFromCsv ", e);
            }
        }
    }

    public void exportCustomersToCsv(String filePath) throws DataExportException {
        List<Customer> customers = getAllCustomers();
        System.out.println("передано на экспорт " + customers.size() + " клиентов");
        customerCsvService.exportToCsv(customers, filePath);
    }

    // транзакция
    public List<Customer> importCustomersFromCsv(String filePath) throws DataImportException {
        List<Customer> imported = customerCsvService.importFromCsv(filePath);
        for (Customer b: imported) {
            customerService.add(b);
        }
        return imported;
    }

    public void exportRequestToCsv(String filePath) throws DataExportException {
        List<RequestBook> requestBooks = getAllRequestBook();
        requestBookCsvService.exportToCsv(requestBooks, filePath);
    }

    // транзакция
    public List<RequestBook> importRequestFromCsv(String filePath) throws DataImportException {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();

            try {
                List<RequestBook> imported = requestBookCsvService.importFromCsv(filePath);
                for (RequestBook b : imported) {
                    requestService.add(b);
                    bookService.add(b.getBook());
                    customerService.add(b.getCustomer());
                }
                tx.commit();
                return imported;
            } catch (DataImportException e) {
                tx.rollback();
                throw e;
            } catch (Exception e) {
                tx.rollback();
                throw new DataManagerException("fail in data manager importRequestFromCsv ", e);
            }
        }
    }

    public List<Book> getStaleBooks(int staleMonths) {
        LocalDate staleDate = LocalDate.now().minusMonths(staleMonths);
        try {
            return getAllBooks().stream()
                    .filter(book -> isBookStale(book, staleDate))
                    .collect(Collectors.toList());
        } catch (ServiceException | DataManagerException ex) {
            throw new DataManagerException("fail to get stale books ", ex.getCause());
        }
    }

    private boolean isBookStale(Book book, LocalDate staleDate) {
        Date staleDateAsDate = Date.from(staleDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        try {
            return getOrdersForBook(book.getBookId()).stream()
                    .noneMatch(order -> {
                        Date orderDate = order.getOrderDate();
                        return orderDate.after(staleDateAsDate) || orderDate.equals(staleDateAsDate);
                    });
        } catch (ServiceException | DataManagerException ex) {
            throw new DataManagerException("fail to determinate is book stale ", ex.getCause());
        }
    }

    public List<Order> getOrdersForBook(int bookId) {
        try {
            return orderService.getAll().stream()
                    .filter(order -> order.getBook().getBookId() == bookId)
                    .collect(Collectors.toList());
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to get orders For Book ", ex.getCause());
        }
    }

    // транзакция
    public void addCustomer(Customer customer) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            try {
                customerService.add(customer);
                tx.commit();
            } catch (ServiceException ex) {
                tx.rollback();
                throw new DataManagerException("Fail add client", ex.getCause());
            }
        }
    }

    public Customer getCustomerById(int id) {
        try {
            return customerService.getById(id);
        } catch (ServiceException ex) {
            throw new DataManagerException("fail to get customer by id ", ex.getCause());
        }
    }
}
