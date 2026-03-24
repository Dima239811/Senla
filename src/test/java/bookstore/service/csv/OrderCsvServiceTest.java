package bookstore.service.csv;

import bookstore.enums.OrderStatus;
import bookstore.exception.DataExportException;
import bookstore.exception.DataImportException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderCsvServiceTest {
    private OrderCsvService orderCsvService;
    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() {
        orderCsvService = new OrderCsvService();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    void exportToCsv_shouldWriteOrdersToFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Date orderDate = dateFormat.parse("2024-01-15 14:30:00");

        Book book = new Book("Война и мир", "Лев Толстой", 1869, 899.99,
                bookstore.enums.StatusBook.IN_STOCK, 1);
        Customer customer = new Customer("Иван Иванов", 30, "+7 (999) 123-45-67",
                "ivan@mail.ru", "г. Москва, ул. Ленина, д. 1", 1);

        Order order = new Order(book, customer, orderDate, 899.99, OrderStatus.COMPLETED, 1);
        List<Order> orders = List.of(order);

        Path testFile = tempDir.resolve("orders.csv");
        String filePath = testFile.toString();

        orderCsvService.exportToCsv(orders, filePath);

        assertTrue(testFile.toFile().exists());

        List<String> lines = java.nio.file.Files.readAllLines(testFile);
        assertEquals(2, lines.size());

        String expectedHeader = "orderId,orderDate,finalPrice,orderStatus," +
                "bookId,bookName,bookAuthor,bookYear,bookPrice,bookStatus," +
                "customerId,customerFullName,customerAge,customerPhone,customerEmail,customerAddress";
        assertEquals(expectedHeader, lines.get(0));

        String orderLine = lines.get(1);
        assertTrue(orderLine.contains("1,2024-01-15 14:30:00,899.99,выполнен"));
        assertTrue(orderLine.contains(",1,\"Война и мир\",\"Лев Толстой\",1869,899.99,в наличии"));
        assertTrue(orderLine.contains(",1,\"Иван Иванов\",30,\"+7 (999) 123-45-67\",\"ivan@mail.ru\",\"г. Москва, ул. Ленина, д. 1\""));
    }

    @Test
    void exportToCsv_shouldThrowDataExportExceptionWhenItemsIsNull() {
        String filePath = "test.csv";

        DataExportException exception = assertThrows(
                DataExportException.class,
                () -> orderCsvService.exportToCsv(null, filePath)
        );

        assertEquals("Список заказов не может быть пустым!", exception.getMessage());
    }

    @Test
    void importFromCsv_shouldReadOrdersFromFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("valid_orders.csv");

        String csvContent = "orderId,orderDate,finalPrice,orderStatus,bookId,bookName,bookAuthor,bookYear,bookPrice,bookStatus,customerId,customerFullName,customerAge,customerPhone,customerEmail,customerAddress\n" +
                "1,2024-01-15 14:30:00,899.99,выполнен,1,\"Война и мир\",\"Лев Толстой\",1869,899.99,в наличии,1,\"Иван Иванов\",30,\"+7 (999) 123-45-67\",\"ivan@mail.ru\",\"г. Москва, ул. Ленина, д. 1\"\n" +
                "2,2024-01-16 10:15:00,599.50,ожидание книги,2,\"Преступление и наказание\",\"Фёдор Достоевский\",1866,599.50,отсутствует,2,\"Мария Петрова\",25,\"+7 (999) 765-43-21\",\"maria@mail.ru\",\"г. Санкт-Петербург, Невский пр., д. 10\"";
        java.nio.file.Files.writeString(testFile, csvContent);

        List<Order> result = orderCsvService.importFromCsv(testFile.toString());

        assertEquals(2, result.size());

        Order order1 = result.get(0);
        assertEquals(1, order1.getOrderId());
        assertEquals(dateFormat.parse("2024-01-15 14:30:00"), order1.getOrderDate());
        assertEquals(899.99, order1.getFinalPrice());
        assertEquals(OrderStatus.COMPLETED, order1.getStatus());


        Book book1 = order1.getBook();
        assertEquals("Война и мир", book1.getName());
        assertEquals("Лев Толстой", book1.getAuthor());
        assertEquals(1869, book1.getYear());
        assertEquals(899.99, book1.getPrice());
        assertEquals(bookstore.enums.StatusBook.IN_STOCK, book1.getStatus());

        Customer customer1 = order1.getCustomer();
        assertEquals("Иван Иванов", customer1.getFullName());
        assertEquals(30, customer1.getAge());
        assertEquals("+7 (999) 123-45-67", customer1.getPhoneNumber());
        assertEquals("ivan@mail.ru", customer1.getEmail());
        assertEquals("г. Москва, ул. Ленина, д. 1", customer1.getAddress());

        Order order2 = result.get(1);
        assertEquals(2, order2.getOrderId());
        assertEquals(dateFormat.parse("2024-01-16 10:15:00"), order2.getOrderDate());
        assertEquals(599.50, order2.getFinalPrice());
        assertEquals(OrderStatus.WAITING_FOR_BOOK, order2.getStatus());
    }

    @Test
    void importFromCsv_shouldThrowDataImportExceptionWhenFileNotFound() {
        String nonExistentFilePath = "non_existent_file.csv";

        DataImportException exception = assertThrows(
                DataImportException.class,
                () -> orderCsvService.importFromCsv(nonExistentFilePath)
        );

        assertTrue(exception.getMessage().contains("Ошибка чтения файла"));
    }
}
