package bookstore.service.entity;

import bookstore.config.LibraryConfig;
import bookstore.dto.BookRequest;
import bookstore.dto.BookResponse;
import bookstore.enums.OrderStatus;
import bookstore.enums.StatusBook;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Order;
import bookstore.model.mapper.BookMapper;
import bookstore.repo.BookDAO;
import bookstore.service.entityService.BookService;
import bookstore.service.entityService.OrderService;
import bookstore.service.entityService.RequestBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookDAO bookDAO;

    @Mock
    private OrderService orderService;

    @Mock
    private RequestBookService requestBookService;

    @Mock
    private LibraryConfig libraryConfig;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private List<Book> books;
    private List<BookResponse> bookResponses;
    private Book book;
    private Book book1;
    private BookResponse bookResponse;
    private BookResponse bookResponse1;

    @BeforeEach
    void setUp() {
        book = new Book("Test Book", "Author", 2020, 100.0, StatusBook.IN_STOCK);
        book.setBookId(1);
        books = new ArrayList<>();
        bookResponses = new ArrayList<>();
        books.add(book);
        book1 = new Book("Test Book1", "Author", 1950, 500.0, StatusBook.OUT_OF_STOCK);
        book1.setBookId(2);
        books.add(book1);

        bookResponse = new BookResponse(1, "Test Book", "Author", 2020, 100.0, "IN_STOCK");
        bookResponse1 = new BookResponse(2, "Test Book1", "Author", 1950, 500.0, "OUT_OF_STOCK");
        bookResponses.add(bookResponse);
    }

    @Test
    void getAll_shouldReturnBookResponseList() {
        when(bookDAO.getAll()).thenReturn(books);
        when(bookMapper.toBookResponseList(books)).thenReturn(bookResponses);

        List<BookResponse> result = bookService.getAll();

        assertEquals(bookResponses, result);
        verify(bookDAO).getAll();
        verify(bookMapper).toBookResponseList(books);
    }

    @Test
    void getAll_shouldThrowException() {
        when(bookDAO.getAll()).thenThrow(new DaoException());

        assertThrows(ServiceException.class, () -> bookService.getAll());
        verify(bookDAO).getAll();
    }

    @Test
    void getById_shouldReturnBookResponse() {
        when(bookDAO.findById(1)).thenReturn(book);
        when(bookMapper.toBookResponse(book)).thenReturn(bookResponse);

        BookResponse result = bookService.getById(1);

        assertEquals(bookResponse, result);
        verify(bookDAO).findById(1);
        verify(bookMapper).toBookResponse(book);
    }

    @Test
    void getById_shouldThrowException() {
        when(bookDAO.findById(1)).thenThrow(new DaoException());

        assertThrows(ServiceException.class, () -> bookService.getById(1));
        verify(bookDAO).findById(1);
    }

    @Test
    void add_shouldCreateNewBook() {
        Book newBook = new Book("New Book", "New Author", 2023, 80.0, StatusBook.IN_STOCK);
        newBook.setBookId(1);

        when(bookDAO.findById(1)).thenReturn(null);

        bookService.add(newBook);

        verify(bookDAO).create(newBook);
        verify(bookDAO, never()).update(any());
    }


    @Test
    void add_shouldUpdateExistingBook() {
        Book existingBook = new Book("Existing Book", "Author", 2020, 100.0, StatusBook.IN_STOCK);
        existingBook.setBookId(1);

        when(bookDAO.findById(1)).thenReturn(existingBook);

        bookService.add(existingBook);

        verify(bookDAO).update(existingBook);
        verify(bookDAO, never()).create(any());
    }

    @Test
    void add_shouldThrowExceptionOnUpdateError() {
        when(bookDAO.findById(1)).thenReturn(book);
        doThrow(new DaoException()).when(bookDAO).update(book);

        assertThrows(ServiceException.class, () -> bookService.add(book));
        verify(bookDAO).update(book);
    }

    @Test
    void update_shouldUpdateBookSuccessfully() {
        bookService.update(book);

        verify(bookDAO).update(book);
    }

    @Test
    void update_shouldThrowExceptionOnDaoError() {
        doThrow(new DaoException()).when(bookDAO).update(book);

        assertThrows(ServiceException.class, () -> bookService.update(book));
        verify(bookDAO).update(book);
    }

    @Test
    void sortBooks_shouldSortByAlphabet() {
        when(bookDAO.getAll()).thenReturn(books);

        ArgumentCaptor<List<Book>> captor = ArgumentCaptor.forClass(List.class);

        when(bookMapper.toBookResponseList(captor.capture()))
                .thenReturn(bookResponses);

        bookService.sortBooks("по алфавиту");

        List<Book> sorted = captor.getValue();

        assertEquals("Test Book", sorted.get(0).getName());
        assertEquals("Test Book1", sorted.get(1).getName());
    }

    @Test
    void sortBooks_shouldSortByPrice() {
        when(bookDAO.getAll()).thenReturn(books);

        ArgumentCaptor<List<Book>> captor = ArgumentCaptor.forClass(List.class);

        when(bookMapper.toBookResponseList(captor.capture()))
                .thenReturn(bookResponses);

        bookService.sortBooks("по цене");

        List<Book> sorted = captor.getValue();

        assertEquals(100.0, sorted.get(0).getPrice());
        assertEquals(500.0, sorted.get(1).getPrice());
    }

    @Test
    void sortBooks_shouldReturnUnsortedOnUnknownCriteria() {
        when(bookDAO.getAll()).thenReturn(books);
        when(bookMapper.toBookResponseList(books)).thenReturn(bookResponses);

        List<BookResponse> result = bookService.sortBooks("неизвестный критерий");

        assertEquals(bookResponses, result);
        verify(bookDAO).getAll();
        verify(bookMapper).toBookResponseList(books);
    }

    @Test
    void sortBooks_shouldThrowExceptionOnDaoError() {
        when(bookDAO.getAll()).thenThrow(new DaoException());

        assertThrows(ServiceException.class, () -> bookService.sortBooks("по алфавиту"));
        verify(bookDAO).getAll();
    }

    @Test
    void sortBooks_shouldSortByYear() {
        when(bookDAO.getAll()).thenReturn(books);

        ArgumentCaptor<List<Book>> captor = ArgumentCaptor.forClass(List.class);

        when(bookMapper.toBookResponseList(captor.capture()))
                .thenReturn(bookResponses);

        bookService.sortBooks("по году издания");

        List<Book> sorted = captor.getValue();

        assertTrue(sorted.get(0).getYear() <= sorted.get(1).getYear());
    }

    @Test
    void sortBooks_shouldSortByAvailable() {
        when(bookDAO.getAll()).thenReturn(books);

        ArgumentCaptor<List<Book>> captor = ArgumentCaptor.forClass(List.class);

        when(bookMapper.toBookResponseList(captor.capture()))
                .thenReturn(bookResponses);

        bookService.sortBooks("по наличию на складе");

        List<Book> sorted = captor.getValue();

        assertTrue(sorted.get(0).getStatus() == StatusBook.IN_STOCK);
        assertTrue(sorted.get(1).getStatus() == StatusBook.OUT_OF_STOCK);
    }


    @Test
    void writeOffBook_shouldChangeStatusAndCallUpdate() {
        book.setBookId(1);
        book.setStatus(StatusBook.IN_STOCK);

        when(bookDAO.getAll()).thenReturn(List.of(book));

        bookService.writeOffBook(1);
        assertEquals(StatusBook.OUT_OF_STOCK, book.getStatus());
        verify(bookDAO).update(book);
    }

    @Test
    void writeOffBook_shouldDoNothing_ifBookNotFound() {
        when(bookDAO.getAll()).thenReturn(List.of());

        bookService.writeOffBook(1);

        verify(bookDAO, never()).update(any());
    }

    @Test
    void writeOffBook_shouldThrowException_onDaoError() {
        when(bookDAO.getAll()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> bookService.writeOffBook(1));
    }

    @Test
    void getEntityById_shouldReturnBook() {
        when(bookDAO.findById(1)).thenReturn(book);

        Book result = bookService.getEntityById(1);

        assertEquals(book, result);
        verify(bookDAO).findById(1);
    }

    @Test
    void getEntityById_shouldThrowException() {
        when(bookDAO.findById(1)).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> bookService.getEntityById(1));
    }

    @Test
    void getAllEntities_shouldReturnList() {
        when(bookDAO.getAll()).thenReturn(books);

        List<Book> result = bookService.getAllEntities();

        assertEquals(books, result);
        verify(bookDAO).getAll();
    }

    @Test
    void getAllEntities_shouldThrowException() {
        when(bookDAO.getAll()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> bookService.getAllEntities());
    }

    @Test
    void addBookToWarehouse_shouldAddBook() {
        BookRequest request = mock(BookRequest.class);

        when(bookMapper.toEntity(request)).thenReturn(book);
        when(libraryConfig.isAutoCloseRequests()).thenReturn(false);
        when(orderService.getAllOrder()).thenReturn(List.of());

        bookService.addBookToWarehouse(request);

        verify(bookMapper).toEntity(request);
    }

    @Test
    void addBookToWarehouse_shouldCloseRequests() {
        BookRequest request = mock(BookRequest.class);

        when(bookMapper.toEntity(request)).thenReturn(book);
        when(libraryConfig.isAutoCloseRequests()).thenReturn(true);
        when(orderService.getAllOrder()).thenReturn(List.of());

        bookService.addBookToWarehouse(request);

        verify(requestBookService)
                .closeRequest(book.getName(), book.getAuthor());
    }

    @Test
    void addBookToWarehouse_shouldUpdateOrderStatus() {
        BookRequest request = mock(BookRequest.class);

        Order order = mock(Order.class);

        when(bookMapper.toEntity(request)).thenReturn(book);
        when(libraryConfig.isAutoCloseRequests()).thenReturn(false);

        when(order.getBook()).thenReturn(book);
        when(order.getStatus()).thenReturn(OrderStatus.WAITING_FOR_BOOK);
        when(order.getOrderId()).thenReturn(1);

        when(orderService.getAllOrder()).thenReturn(List.of(order));

        bookService.addBookToWarehouse(request);

        verify(orderService).changeOrderStatus(1, "новый");
    }

    @Test
    void addBookToWarehouse_shouldThrowException() {
        BookRequest request = mock(BookRequest.class);

        when(bookMapper.toEntity(request))
                .thenThrow(new RuntimeException());

        assertThrows(ServiceException.class,
                () -> bookService.addBookToWarehouse(request));
    }

    @Test
    void getStaleBooks_shouldReturnList() {
        when(bookDAO.getAll()).thenReturn(books);
        when(orderService.getAllOrder()).thenReturn(List.of());

        when(bookMapper.toBookResponseList(anyList()))
                .thenReturn(bookResponses);

        List<BookResponse> result = bookService.getStaleBooks(6);

        assertNotNull(result);
    }

    @Test
    void getStaleBooks_shouldThrowException() {
        when(bookDAO.getAll()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> bookService.getStaleBooks(6));
    }

    @Test
    void getOrdersForBook_shouldReturnFilteredOrders() {
        Order order = mock(Order.class);

        when(order.getBook()).thenReturn(book);

        when(orderService.getAllOrder()).thenReturn(List.of(order));

        List<Order> result = bookService.getOrdersForBook(1);

        assertEquals(1, result.size());
    }

    @Test
    void getOrdersForBook_shouldThrowException() {
        when(orderService.getAllOrder())
                .thenThrow(new ServiceException());

        assertThrows(ServiceException.class,
                () -> bookService.getOrdersForBook(1));
    }

    @Test
    void getAllBooks_shouldReturnList() {
        when(bookDAO.getAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(books, result);
    }

    @Test
    void getAllBooks_shouldThrowException() {
        when(bookDAO.getAll()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> bookService.getAllBooks());
    }
}
