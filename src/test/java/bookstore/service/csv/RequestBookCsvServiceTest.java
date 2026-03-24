package bookstore.service.csv;

import bookstore.enums.RequestStatus;
import bookstore.enums.StatusBook;
import bookstore.exception.DataExportException;
import bookstore.exception.DataImportException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.RequestBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RequestBookCsvServiceTest {
    private RequestBookCsvService requestBookCsvService;

    @BeforeEach
    void setUp() {
        requestBookCsvService = new RequestBookCsvService();
    }

    @Test
    void exportToCsv_shouldWriteRequestsToFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Customer customer = new Customer("Иван Иванов", 30, "+7 (999) 123-45-67",
                "ivan@mail.ru", "г. Москва, ул. Ленина, д. 1", 1);
        Book book = new Book("Война и мир", "Лев Толстой", 1869, 899.99,
                StatusBook.IN_STOCK, 1);

        RequestBook request = new RequestBook(customer, book, RequestStatus.OPEN, 1);
        List<RequestBook> requests = List.of(request);

        Path testFile = tempDir.resolve("requests.csv");
        String filePath = testFile.toString();

        requestBookCsvService.exportToCsv(requests, filePath);

        assertTrue(testFile.toFile().exists());

        List<String> lines = java.nio.file.Files.readAllLines(testFile);
        assertEquals(2, lines.size());

        String expectedHeader = "requestId,customerId,customerName,bookId,bookName,bookStatus,requestStatus";
        assertEquals(expectedHeader, lines.get(0));


        String requestLine = lines.get(1);

        assertTrue(requestLine.contains("1,1,Иван Иванов,1,Война и мир,в наличии,Открыт"));
    }

    @Test
    void exportToCsv_shouldThrowDataExportExceptionWhenItemsIsNull() {
        String filePath = "test.csv";

        DataExportException exception = assertThrows(
                DataExportException.class,
                () -> requestBookCsvService.exportToCsv(null, filePath)
        );

        assertEquals("Список запросов не может быть пустым!", exception.getMessage());
    }

    // Тесты для importFromCsv

    @Test
    void importFromCsv_shouldReadRequestsFromFileSuccessfully(@TempDir Path tempDir) throws Exception {
        // Arrange
        Path testFile = tempDir.resolve("valid_requests.csv");

        String csvContent = "requestId,customerId,customerName,bookId,bookName,bookStatus,requestStatus\n" +
                "1,1,\"Иван Иванов\",1,\"Война и мир\",в наличии,Открыт\n" +
                "2,2,\"Мария Петрова\",2,\"Преступление и наказание\",отсутствует,Закрыт";
        java.nio.file.Files.writeString(testFile, csvContent);

        List<RequestBook> result = requestBookCsvService.importFromCsv(testFile.toString());

        assertEquals(2, result.size());

        RequestBook request1 = result.get(0);
        assertEquals(1, request1.getRequestId());
        assertEquals(RequestStatus.OPEN, request1.getStatus());

        Customer customer1 = request1.getCustomer();
        assertEquals(1, customer1.getCustomerID());
        assertEquals("Иван Иванов", customer1.getFullName());

        Book book1 = request1.getBook();
        assertEquals(1, book1.getBookId());
        assertEquals("Война и мир", book1.getName());
        assertEquals(StatusBook.IN_STOCK, book1.getStatus());

        RequestBook request2 = result.get(1);
        assertEquals(2, request2.getRequestId());
        assertEquals(RequestStatus.CLOSED, request2.getStatus());

        Customer customer2 = request2.getCustomer();
        assertEquals(2, customer2.getCustomerID());
        assertEquals("Мария Петрова", customer2.getFullName());

        Book book2 = request2.getBook();
        assertEquals(2, book2.getBookId());
        assertEquals("Преступление и наказание", book2.getName());
        assertEquals(StatusBook.OUT_OF_STOCK, book2.getStatus());
    }

    @Test
    void importFromCsv_shouldThrowDataImportExceptionWhenFileNotFound() {
        String nonExistentFilePath = "non_existent_file.csv";

        DataImportException exception = assertThrows(
                DataImportException.class,
                () -> requestBookCsvService.importFromCsv(nonExistentFilePath)
        );

        assertTrue(exception.getMessage().contains("Ошибка чтения файла"));
    }
}
