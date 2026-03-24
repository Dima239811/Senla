package bookstore.controller;

import bookstore.dto.BookRequest;
import bookstore.dto.BookResponse;
import bookstore.service.entityService.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void getBookById_shouldReturnBookWhenFound() throws Exception {
        int bookId = 1;
        BookResponse expectedBook = new BookResponse(bookId, "Война и мир", "Лев Толстой", 1869, 999.99,
                "в наличии");

        when(bookService.getById(bookId)).thenReturn(expectedBook);

        mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(bookId))
                .andExpect(jsonPath("$.name").value("Война и мир"))
                .andExpect(jsonPath("$.author").value("Лев Толстой"))
                .andExpect(jsonPath("$.year").value(1869))
                .andExpect(jsonPath("$.price").value(999.99))
                .andExpect(jsonPath("$.status").value("в наличии"));

        verify(bookService).getById(bookId);
    }

    @Test
    void getBookById_shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        int nonExistentId = 999;

        when(bookService.getById(nonExistentId)).thenReturn(null);

        mockMvc.perform(get("/books/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(""));

        verify(bookService).getById(nonExistentId);
    }


    @Test
    void getAllBooks_shouldReturnAllBooksWhenListIsNotEmpty() throws Exception {
        List<BookResponse> expectedBooks = Arrays.asList(
                new BookResponse(1, "Book One", "Author A", 2020, 500.00, "в наличии"),
                new BookResponse(2, "Book Two", "Author B", 2021, 600.00, "отсутствует"));

        when(bookService.getAll()).thenReturn(expectedBooks);

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].bookId").value(1))
                .andExpect(jsonPath("$[0].name").value("Book One"))
                .andExpect(jsonPath("$[1].bookId").value(2))
                .andExpect(jsonPath("$[1].name").value("Book Two"));

        verify(bookService).getAll();
    }

    @Test
    void getAllBooks_shouldReturnEmptyListWhenNoBooksExist() throws Exception {
        List<BookResponse> emptyList = List.of();

        when(bookService.getAll()).thenReturn(emptyList);

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(bookService).getAll();
    }

    @Test
    void addBook_shouldAddBookSuccessfully() throws Exception {
        BookRequest bookRequest = new BookRequest("New Book", "New Author", 2024, 750.00);
        ObjectMapper objectMapper = new ObjectMapper();
        String bookRequestJson = objectMapper.writeValueAsString(bookRequest);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRequestJson));

        verify(bookService).addBookToWarehouse(bookRequest);
    }

    @Test
    void sortBooks_shouldReturnSortedBooks() throws Exception {
        List<BookResponse> sortedBooks = Arrays.asList(
                new BookResponse(3, "C Book", "Author C", 2019, 400.00, "AVAILABLE"),
                new BookResponse(1, "A Book", "Author A", 2020, 500.00, "AVAILABLE"));

        when(bookService.sortBooks("name")).thenReturn(sortedBooks);

        mockMvc.perform(get("/books/sort")
                        .param("criteria", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("C Book"))
                .andExpect(jsonPath("$[1].name").value("A Book"));

        verify(bookService).sortBooks("name");
    }


    @Test
    void writeOffBook_shouldWriteOffBookSuccessfully() throws Exception {
        int bookId = 5;

        mockMvc.perform(patch("/books/{id}/write-off", bookId)
                        .contentType(MediaType.APPLICATION_JSON));

        verify(bookService).writeOffBook(bookId);
    }

    @Test
    void getStaleBooks_shouldReturnStaleBooks() throws Exception {
        List<BookResponse> staleBooks = Arrays.asList(
                new BookResponse(4, "Old Book", "Old Author", 1990, 200.00, "в наличии"));
        int staleMonths = 12;

        when(bookService.getStaleBooks(staleMonths)).thenReturn(staleBooks);

        mockMvc.perform(get("/books/stale")
                        .param("staleMonths", String.valueOf(staleMonths))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Old Book"));

        verify(bookService).getStaleBooks(staleMonths);
    }
}
