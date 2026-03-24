package bookstore.service;

import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.model.entity.RequestBook;
import bookstore.service.csv.ICsvService;
import bookstore.service.entityService.BookService;
import bookstore.service.entityService.CustomerService;
import bookstore.service.entityService.OrderService;
import bookstore.service.entityService.RequestBookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataTransferServiceTest {
    @Mock
    private BookService bookService;

    @Mock
    private CustomerService customerService;

    @Mock
    private OrderService orderService;

    @Mock
    private RequestBookService requestBookService;

    @Mock
    private ICsvService<Book> bookCsvService;

    @Mock
    private ICsvService<Order> orderCsvService;

    @Mock
    private ICsvService<Customer> customerCsvService;

    @Mock
    private ICsvService<RequestBook> requestBookCsvService;

    @InjectMocks
    private DataTransferService dataTransferService;

    @Test
    void exportBooksToCsv_shouldCallServicesSuccessfully(@TempDir Path tempDir) throws Exception {
        List<Book> books = List.of(new Book());
        String filePath = tempDir.resolve("books.csv").toString();

        when(bookService.getAllBooks()).thenReturn(books);

        dataTransferService.exportBooksToCsv(filePath);

        verify(bookService).getAllBooks();
        verify(bookCsvService).exportToCsv(books, filePath);
    }

    @Test
    void exportOrdersToCsv_shouldCallServicesSuccessfully(@TempDir Path tempDir) throws Exception {
        List<Order> orders = List.of(new Order());
        String filePath = tempDir.resolve("orders.csv").toString();

        when(orderService.getAllOrder()).thenReturn(orders);

        dataTransferService.exportOrdersToCsv(filePath);

        verify(orderService).getAllOrder();
        verify(orderCsvService).exportToCsv(orders, filePath);
    }

    @Test
    void exportCustomersToCsv_shouldCallServicesSuccessfully(@TempDir Path tempDir) throws Exception {
        List<Customer> customers = List.of(new Customer());
        String filePath = tempDir.resolve("customers.csv").toString();

        when(customerService.getAllCustomer()).thenReturn(customers);

        dataTransferService.exportCustomersToCsv(filePath);

        verify(customerService).getAllCustomer();
        verify(customerCsvService).exportToCsv(customers, filePath);
    }

    @Test
    void exportRequestToCsv_shouldCallServicesSuccessfully(@TempDir Path tempDir) throws Exception {
        List<RequestBook> requests = List.of(new RequestBook());
        String filePath = tempDir.resolve("requests.csv").toString();

        when(requestBookService.getAllRequestBook()).thenReturn(requests);

        dataTransferService.exportRequestToCsv(filePath);

        verify(requestBookService).getAllRequestBook();
        verify(requestBookCsvService).exportToCsv(requests, filePath);
    }

    @Test
    void importBooksFromCsv_shouldImportSuccessfully() throws Exception {
        List<Book> importedBooks = List.of(new Book());
        String filePath = "books.csv";

        when(bookCsvService.importFromCsv(filePath)).thenReturn(importedBooks);

        List<Book> result = dataTransferService.importBooksFromCsv(filePath);

        assertEquals(importedBooks, result);
        verify(bookCsvService).importFromCsv(filePath);
        verify(bookService).add(any(Book.class));
    }

    @Test
    void importOrdersFromCsv_shouldImportSuccessfully() throws Exception {
        Order order = new Order();
        order.setBook(new Book());
        order.setCustomer(new Customer());

        List<Order> importedOrders = List.of(order);
        String filePath = "orders.csv";

        when(orderCsvService.importFromCsv(filePath)).thenReturn(importedOrders);

        List<Order> result = dataTransferService.importOrdersFromCsv(filePath);

        assertEquals(importedOrders, result);
        verify(orderCsvService).importFromCsv(filePath);
        verify(orderService).add(any(Order.class));
        verify(bookService).add(any(Book.class));
        verify(customerService).addCustomerEntity(any(Customer.class));
    }

    @Test
    void importCustomersFromCsv_shouldImportSuccessfully() throws Exception {
        List<Customer> importedCustomers = List.of(new Customer());
        String filePath = "customers.csv";

        when(customerCsvService.importFromCsv(filePath)).thenReturn(importedCustomers);

        List<Customer> result = dataTransferService.importCustomersFromCsv(filePath);

        assertEquals(importedCustomers, result);
        verify(customerCsvService).importFromCsv(filePath);
        verify(customerService).addCustomerEntity(any(Customer.class));
    }

    @Test
    void importRequestFromCsv_shouldImportSuccessfully() throws Exception {
        RequestBook request = new RequestBook();
        request.setBook(new Book());
        request.setCustomer(new Customer());

        List<RequestBook> importedRequests = List.of(request);
        String filePath = "requests.csv";

        when(requestBookCsvService.importFromCsv(filePath)).thenReturn(importedRequests);

        List<RequestBook> result = dataTransferService.importRequestFromCsv(filePath);

        assertEquals(importedRequests, result);
        verify(requestBookCsvService).importFromCsv(filePath);
        verify(requestBookService).add(any(RequestBook.class));
        verify(bookService).add(any(Book.class));
        verify(customerService).addCustomerEntity(any(Customer.class));
    }
}
