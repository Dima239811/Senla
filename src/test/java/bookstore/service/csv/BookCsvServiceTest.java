package bookstore.service.csv;

import bookstore.enums.StatusBook;
import bookstore.exception.DataExportException;
import bookstore.exception.DataImportException;
import bookstore.exception.DataValidationException;
import bookstore.model.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookCsvServiceTest {
    private BookCsvService bookCsvService;

    @BeforeEach
    void setUp() {
        bookCsvService = new BookCsvService();
    }


    @Test
    void exportToCsv_shouldWriteBooksToFileSuccessfully(@TempDir Path tempDir) throws Exception {
        List<Book> books = List.of(
                new Book("Test Book 1", "Author 1", 2020, 100.0, StatusBook.IN_STOCK, 1),
                new Book("Test Book 2", "Author 2", 2021, 150.0, StatusBook.OUT_OF_STOCK, 2)
        );

        Path testFile = tempDir.resolve("books.csv");
        String filePath = testFile.toString();

        bookCsvService.exportToCsv(books, filePath);

        assertTrue(testFile.toFile().exists());

        List<String> lines = java.nio.file.Files.readAllLines(testFile);
        assertEquals(3, lines.size());
        assertEquals("id,name,author,year,price,status", lines.get(0));
        assertTrue(lines.get(1).contains("Test Book 1"));
        assertTrue(lines.get(2).contains("Test Book 2"));
    }

    @Test
    void exportToCsv_shouldThrowDataExportExceptionWhenItemsIsNull() {
        String filePath = "test.csv";

        DataExportException exception = assertThrows(
                DataExportException.class,
                () -> bookCsvService.exportToCsv(null, filePath)
        );

        assertEquals("Список книг не может быть пустым!", exception.getMessage());
    }


    @Test
    void importFromCsv_shouldReadBooksFromFileSuccessfully(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("valid_books.csv");

        String csvContent = "id,name,author,year,price,status\n" +
                "1,\"Book 1\",\"Author 1\",2020,100.00,в наличии\n" +
                "2,\"Book 2\",\"Author 2\",2021,150.00,отсутствует";
        java.nio.file.Files.writeString(testFile, csvContent);

        List<Book> result = bookCsvService.importFromCsv(testFile.toString());

        assertEquals(2, result.size());
        assertEquals("\"Book 1\"", result.get(0).getName());
        assertEquals(100.0, result.get(0).getPrice());
        assertEquals(StatusBook.IN_STOCK, result.get(0).getStatus());
        assertEquals("\"Book 2\"", result.get(1).getName());
        assertEquals(150.0, result.get(1).getPrice());
        assertEquals(StatusBook.OUT_OF_STOCK, result.get(1).getStatus());
    }

    @Test
    void importFromCsv_shouldThrowDataImportExceptionOnInvalidLine(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("invalid_books.csv");

        String csvContent = "id,name,author,year,price,status\n" +
                "1,\"Book 1\",\"Author 1\",invalid_year,100.00,AVAILABLE";
        java.nio.file.Files.writeString(testFile, csvContent);

        assertThrows(
                DataImportException.class,
                () -> bookCsvService.importFromCsv(testFile.toString())
        );
    }

    @Test
    void importFromCsv_shouldThrowDataImportExceptionOnFileNotFound() {
        String nonExistentFilePath = "non_existent_file.csv";

        DataImportException exception = assertThrows(
                DataImportException.class,
                () -> bookCsvService.importFromCsv(nonExistentFilePath)
        );

        assertTrue(exception.getMessage().contains("Ошибка чтения файла"));
    }


    @Test
    void parseBookFromCsvLine_shouldParseValidLine() throws DataValidationException {
        String validLine = "1,\"Test Book\",\"Test Author\",2020,99.99,в наличии";

        Book book = bookCsvService.parseBookFromCsvLine(validLine);

        assertEquals("\"Test Book\"", book.getName());
        assertEquals("\"Test Author\"", book.getAuthor());
        assertEquals(2020, book.getYear());
        assertEquals(99.99, book.getPrice());
        assertEquals(StatusBook.IN_STOCK, book.getStatus());
        assertEquals(1, book.getBookId());
    }

    @Test
    void parseBookFromCsvLine_shouldThrowOnInsufficientData() {
        String incompleteLine = "1,\"Test Book\",\"Test Author\"";

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> bookCsvService.parseBookFromCsvLine(incompleteLine)
        );

        assertEquals("Недостаточно данных в строке", exception.getMessage());
    }

    @Test
    void validateBook_shouldNotThrowExceptionForValidBook() {
        Book validBook = new Book("Valid Book", "Author", 2020, 100.0, StatusBook.IN_STOCK, 1);

        assertDoesNotThrow(() -> bookCsvService.validateBook(validBook));
    }

    @Test
    void validateBook_shouldThrowExceptionWhenNameIsNull() {
        Book invalidBook = new Book(null, "Author", 2020, 100.0, StatusBook.IN_STOCK, 1);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> bookCsvService.validateBook(invalidBook)
        );

        assertEquals("Название книги не может быть пустым", exception.getMessage());
    }

    @Test
    void validateBook_shouldThrowExceptionWhenNameIsEmpty() {
        Book invalidBook = new Book("", "Author", 2020, 100.0, StatusBook.IN_STOCK, 1);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> bookCsvService.validateBook(invalidBook)
        );

        assertEquals("Название книги не может быть пустым", exception.getMessage());
    }

    @Test
    void validateBook_shouldThrowExceptionWhenNameIsBlank() {
        Book invalidBook = new Book("   ", "Author", 2020, 100.0, StatusBook.IN_STOCK, 1);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> bookCsvService.validateBook(invalidBook)
        );

        assertEquals("Название книги не может быть пустым", exception.getMessage());
    }

    @Test
    void validateBook_shouldThrowExceptionWhenPriceIsZero() {
        Book invalidBook = new Book("Book", "Author", 2020, 0.0, StatusBook.IN_STOCK, 1);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> bookCsvService.validateBook(invalidBook)
        );

        assertEquals("Цена должна быть положительной", exception.getMessage());
    }

    @Test
    void validateBook_shouldThrowExceptionWhenPriceIsNegative() {
        Book invalidBook = new Book("Book", "Author", 2020, -10.0, StatusBook.IN_STOCK, 1);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> bookCsvService.validateBook(invalidBook)
        );

        assertEquals("Цена должна быть положительной", exception.getMessage());
    }

    @Test
    void validateBook_shouldThrowExceptionWhenYearIsNegative() {
        Book invalidBook = new Book("Book", "Author", -2020, 100.0, StatusBook.IN_STOCK, 1);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> bookCsvService.validateBook(invalidBook)
        );

        assertEquals("Некорретный год", exception.getMessage());
    }
}
