package bookstore.controller;

import bookstore.enums.OrderStatus;
import bookstore.enums.RequestStatus;
import bookstore.enums.StatusBook;
import bookstore.exception.DataExportException;
import bookstore.exception.DataImportException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.model.entity.RequestBook;
import bookstore.service.DataTransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DataTransferControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private DataTransferController dataTransferController;

    @Mock
    private DataTransferService dataTransferService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(dataTransferController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void exportBooksToCsv_shouldExportSuccessfully() throws Exception {
        String filePath = "/tmp/books.csv";

        mockMvc.perform(get("/data/export/books")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON));

        verify(dataTransferService).exportBooksToCsv(filePath);
    }

    @Test
    void exportBooksToCsv_shouldReturnErrorWhenExportFails() throws Exception {
        String filePath = "/tmp/books.csv";
        doThrow(new DataExportException("Ошибка экспорта книг"))
                .when(dataTransferService).exportBooksToCsv(filePath);

        mockMvc.perform(get("/data/export/books")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON));

        verify(dataTransferService).exportBooksToCsv(filePath);
    }

    @Test
    void importBooksFromCsv_shouldImportSuccessfully() throws Exception {
        String filePath = "/tmp/books.csv";
        List<Book> importedBooks = Arrays.asList(
                new Book("Book One", "Author A", 2020, 500.00, StatusBook.IN_STOCK, 1),
                new Book("Book Two", "Author B", 2021, 600.00, StatusBook.IN_STOCK,2));

        when(dataTransferService.importBooksFromCsv(filePath)).thenReturn(importedBooks);

        mockMvc.perform(post("/data/import/books")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].bookId").value(1))
                .andExpect(jsonPath("$[0].name").value("Book One"))
                .andExpect(jsonPath("$[1].bookId").value(2))
                .andExpect(jsonPath("$[1].name").value("Book Two"));

        verify(dataTransferService, times(1)).importBooksFromCsv(filePath);
    }

    @Test
    void importBooksFromCsv_shouldReturnErrorWhenImportFails() throws Exception {
        String filePath = "/tmp/books.csv";
        doThrow(new DataImportException("Ошибка импорта книг"))
                .when(dataTransferService).importBooksFromCsv(filePath);

        mockMvc.perform(post("/data/import/books")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ошибка импорта книг"));

        verify(dataTransferService).importBooksFromCsv(filePath);
    }

    @Test
    void exportOrdersToCsv_shouldExportSuccessfully() throws Exception {
        String filePath = "/tmp/orders.csv";

        mockMvc.perform(get("/data/export/orders")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON));

        verify(dataTransferService, times(1)).exportOrdersToCsv(filePath);
    }

    @Test
    void exportOrdersToCsv_shouldReturnErrorWhenExportFails() throws Exception {
        String filePath = "/tmp/orders.csv";
        doThrow(new DataExportException("Ошибка экспорта заказов"))
                .when(dataTransferService).exportOrdersToCsv(filePath);

        mockMvc.perform(get("/data/export/orders")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(dataTransferService, times(1)).exportOrdersToCsv(filePath);
    }

    @Test
    void importOrdersFromCsv_shouldImportSuccessfully() throws Exception {
        String filePath = "/tmp/orders.csv";
        List<Order> importedOrders = Arrays.asList(
                new Order(new Book(), new Customer(), new Date(), 1000.00, OrderStatus.NEW, 1),
                new Order(new Book(), new Customer(), new Date(), 1000.00, OrderStatus.NEW, 2));

        when(dataTransferService.importOrdersFromCsv(filePath)).thenReturn(importedOrders);

        mockMvc.perform(post("/data/import/orders")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].orderId").value(1))
                .andExpect(jsonPath("$[1].orderId").value(2));

        verify(dataTransferService).importOrdersFromCsv(filePath);
    }


    @Test
    void exportCustomersToCsv_shouldExportSuccessfully() throws Exception {
        String filePath = "/tmp/customers.csv";

        mockMvc.perform(get("/data/export/customers")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON));

        verify(dataTransferService).exportCustomersToCsv(filePath);
    }

    @Test
    void exportCustomersToCsv_shouldReturnErrorWhenExportFails() throws Exception {
        String filePath = "/tmp/customers.csv";
        doThrow(new DataExportException("Ошибка экспорта клиентов"))
                .when(dataTransferService).exportCustomersToCsv(filePath);

        mockMvc.perform(get("/data/export/customers")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(dataTransferService).exportCustomersToCsv(filePath);
    }

    @Test
    void importCustomersFromCsv_shouldImportSuccessfully() throws Exception {
        String filePath = "/tmp/customers.csv";
        List<Customer> importedCustomers = Arrays.asList(
                new Customer("John", 19, "+790555", "john@example.com", "address", 1),
                new Customer("Jane", 19, "+790555", "jane@example.com", "address", 2));

        when(dataTransferService.importCustomersFromCsv(filePath)).thenReturn(importedCustomers);

        mockMvc.perform(post("/data/import/customers")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customerID").value(1))
                .andExpect(jsonPath("$[0].fullName").value("John"))
                .andExpect(jsonPath("$[1].customerID").value(2))
                .andExpect(jsonPath("$[1].fullName").value("Jane"));

        verify(dataTransferService).importCustomersFromCsv(filePath);
    }

    @Test
    void importCustomersFromCsv_shouldReturnErrorWhenImportFails() throws Exception {
        String filePath = "/tmp/customers.csv";
        doThrow(new DataImportException("Ошибка импорта клиентов"))
                .when(dataTransferService).importCustomersFromCsv(filePath);

        mockMvc.perform(post("/data/import/customers")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(dataTransferService).importCustomersFromCsv(filePath);
    }

    @Test
    void exportRequestToCsv_shouldExportSuccessfully() throws Exception {
        String filePath = "/tmp/requests.csv";

        mockMvc.perform(get("/data/export/requests")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON));

        verify(dataTransferService, times(1)).exportRequestToCsv(filePath);
    }

    @Test
    void exportRequestToCsv_shouldReturnErrorWhenExportFails() throws Exception {
        String filePath = "/tmp/requests.csv";
        doThrow(new DataExportException("Ошибка экспорта запросов"))
                .when(dataTransferService).exportRequestToCsv(filePath);

        mockMvc.perform(get("/data/export/requests")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(dataTransferService).exportRequestToCsv(filePath);
    }

    @Test
    void importRequestFromCsv_shouldImportSuccessfully() throws Exception {
        String filePath = "/tmp/requests.csv";
        Book book1 = new Book();
        book1.setBookId(1);
        Book book2 = new Book();
        book2.setBookId(2);
        List<RequestBook> importedRequests = Arrays.asList(
                new RequestBook(new Customer(), book1, RequestStatus.OPEN, 1),
                new RequestBook(new Customer(), book2, RequestStatus.OPEN, 2));

        when(dataTransferService.importRequestFromCsv(filePath)).thenReturn(importedRequests);

        mockMvc.perform(post("/data/import/requests")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].requestId").value(1))
                .andExpect(jsonPath("$[0].book.bookId").value(1))
                .andExpect(jsonPath("$[1].requestId").value(2))
                .andExpect(jsonPath("$[1].book.bookId").value(2));

        verify(dataTransferService, times(1)).importRequestFromCsv(filePath);
    }

    @Test
    void importRequestFromCsv_shouldReturnErrorWhenImportFails() throws Exception {
        String filePath = "/tmp/requests.csv";
        doThrow(new DataImportException("Ошибка импорта запросов"))
                .when(dataTransferService).importRequestFromCsv(filePath);

        mockMvc.perform(post("/data/import/requests")
                        .param("filePath", filePath)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(dataTransferService, times(1)).importRequestFromCsv(filePath);
    }
}
