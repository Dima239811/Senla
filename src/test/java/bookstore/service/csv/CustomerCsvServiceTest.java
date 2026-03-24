package bookstore.service.csv;

import bookstore.exception.DataExportException;
import bookstore.exception.DataImportException;
import bookstore.model.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerCsvServiceTest {
    private CustomerCsvService customerCsvService;

    @BeforeEach
    void setUp() {
        customerCsvService = new CustomerCsvService();
    }

    @Test
    void exportToCsv_shouldWriteCustomersToFileSuccessfully(@TempDir Path tempDir) throws Exception {
        List<Customer> customers = List.of(
                new Customer("Иван Иванов", 30, "+7 (999) 123-45-67", "ivan@mail.ru", "г. Москва, ул. Ленина, д. 1", 1),
                new Customer("Мария Петрова", 25, "+7 (999) 765-43-21", "maria@mail.ru", "г. Санкт-Петербург, Невский пр., д. 10", 2)
        );

        Path testFile = tempDir.resolve("customers.csv");
        String filePath = testFile.toString();

        customerCsvService.exportToCsv(customers, filePath);

        assertTrue(testFile.toFile().exists());

        List<String> lines = java.nio.file.Files.readAllLines(testFile);
        assertEquals(3, lines.size());
        assertEquals("id,fullName,age,phoneNumber,email,address", lines.get(0));
        assertTrue(lines.get(1).contains("Иван Иванов"));
        assertTrue(lines.get(2).contains("Мария Петрова"));
    }

    @Test
    void exportToCsv_shouldThrowDataExportExceptionWhenItemsIsNull() {
        String filePath = "test.csv";

        DataExportException exception = assertThrows(
                DataExportException.class,
                () -> customerCsvService.exportToCsv(null, filePath)
        );

        assertEquals("Список клиентов не может быть пустым!", exception.getMessage());
    }

    @Test
    void importFromCsv_shouldReadCustomersFromFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("valid_customers.csv");

        String csvContent = "id,fullName,age,phoneNumber,email,address\n" +
                "1,\"Иван Иванов\",30,\"+7 (999) 123-45-67\",\"ivan@mail.ru\",\"г. Москва, ул. Ленина, д. 1\"\n" +
                "2,\"Мария Петрова\",25,\"+7 (999) 765-43-21\",\"maria@mail.ru\",\"г. Санкт-Петербург, Невский пр., д. 10\"";
        java.nio.file.Files.writeString(testFile, csvContent);

        List<Customer> result = customerCsvService.importFromCsv(testFile.toString());

        assertEquals(2, result.size());
        assertEquals("Иван Иванов", result.get(0).getFullName());
        assertEquals(30, result.get(0).getAge());
        assertEquals("+7 (999) 123-45-67", result.get(0).getPhoneNumber());
        assertEquals("ivan@mail.ru", result.get(0).getEmail());
        assertEquals("г. Москва, ул. Ленина, д. 1", result.get(0).getAddress());

        assertEquals("Мария Петрова", result.get(1).getFullName());
        assertEquals(25, result.get(1).getAge());
    }

    @Test
    void importFromCsv_shouldThrowDataImportExceptionWhenFileNotFound() {
        String nonExistentFilePath = "non_existent_file.csv";

        DataImportException exception = assertThrows(
                DataImportException.class,
                () -> customerCsvService.importFromCsv(nonExistentFilePath)
        );

        assertTrue(exception.getMessage().contains("Ошибка чтения файла"));
    }
}
