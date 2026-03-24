package bookstore.service.entity;

import bookstore.dto.CustomerRequest;
import bookstore.dto.RequestBookRequest;
import bookstore.dto.RequestBookResponse;
import bookstore.enums.RequestStatus;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.RequestBook;
import bookstore.model.entity.User;
import bookstore.model.mapper.RequestBookMapper;
import bookstore.repo.BookDAO;
import bookstore.repo.RequestBookDAO;
import bookstore.service.entityService.CustomerService;
import bookstore.service.entityService.RequestBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestBookServiceTest {
    @Mock
    private RequestBookDAO requestBookDAO;

    @Mock
    private RequestBookMapper requestBookMapper;

    @Mock
    private CustomerService customerService;

    @Mock
    private BookDAO bookDAO;

    @InjectMocks
    @Spy
    private RequestBookService requestBookService;

    private List<RequestBook> requestBooks;
    private List<RequestBookResponse> requestBookResponses;
    private RequestBook requestBook;
    private Book book;
    private Customer customer;

    @BeforeEach
    void setUp() {
        book = new Book("Test Book", "Author", 2020, 100.0, null);
        book.setBookId(1);

        customer = new Customer("Test Customer", 19, "+795055855", "test@email.com",
                "address", new User());
        customer.setCustomerID(1);

        requestBook = new RequestBook(customer, book);
        requestBook.setRequestId(1);
        requestBook.setStatus(RequestStatus.OPEN);

        requestBooks = new ArrayList<>();
        requestBooks.add(requestBook);

        RequestBookResponse requestBookResponse = new RequestBookResponse(
                1, 1, "Test Book", "Author", 1, "Test Customer",
                "OPEN"
        );
        requestBookResponses = new ArrayList<>();
        requestBookResponses.add(requestBookResponse);
    }

    @Test
    void closeRequest_shouldCloseRequestSuccessfully() {
        when(requestBookDAO.getAll()).thenReturn(requestBooks);

        requestBookService.closeRequest("Test Book", "Author");

        assertEquals(RequestStatus.CLOSED, requestBook.getStatus());
        verify(requestBookDAO).getAll();
        verify(requestBookService).update(requestBook);
    }

    @Test
    void closeRequest_shouldNotCloseWhenNoMatchingRequest() {
        when(requestBookDAO.getAll()).thenReturn(requestBooks);
        requestBookService.closeRequest("Nonexistent Book", "Unknown Author");

        assertEquals(RequestStatus.OPEN, requestBook.getStatus());
        verify(requestBookDAO).getAll();
        verify(requestBookService, never()).update(any());
    }

    @Test
    void closeRequest_shouldThrowExceptionOnDaoError() {
        when(requestBookDAO.getAll()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> requestBookService.closeRequest("Test Book", "Author")
        );
    }

    @Test
    void sortRequest_shouldSortByAlphabet() {
        when(requestBookDAO.getAllWithBooksAndCustomers()).thenReturn(requestBooks);
        when(requestBookMapper.toRequestBookResponseList(requestBooks)).thenReturn(requestBookResponses);

        List<RequestBookResponse> result = requestBookService.sortRequest("по алфавиту");

        assertNotNull(result);
        assertEquals(requestBookResponses, result);
        verify(requestBookDAO).getAllWithBooksAndCustomers();
    }

    @Test
    void sortRequest_shouldSortByRequestCount() {
        RequestBook secondRequest = new RequestBook(customer, book);
        secondRequest.setRequestId(2);
        List<RequestBook> multipleRequests = List.of(requestBook, secondRequest);

        when(requestBookDAO.getAllWithBooksAndCustomers()).thenReturn(multipleRequests);
        when(requestBookMapper.toRequestBookResponseList(multipleRequests)).thenReturn(requestBookResponses);

        List<RequestBookResponse> result = requestBookService.sortRequest("по количеству запросов");

        assertNotNull(result);
        verify(requestBookDAO).getAllWithBooksAndCustomers();
    }

    @Test
    void sortRequest_shouldReturnUnsortedOnUnknownCriteria() {
        when(requestBookDAO.getAllWithBooksAndCustomers()).thenReturn(requestBooks);
        when(requestBookMapper.toRequestBookResponseList(requestBooks)).thenReturn(requestBookResponses);

        List<RequestBookResponse> result = requestBookService.sortRequest("неизвестный критерий");

        assertEquals(requestBookResponses, result);
        verify(requestBookDAO).getAllWithBooksAndCustomers();
    }

    @Test
    void sortRequest_shouldThrowExceptionOnDaoError() {
        when(requestBookDAO.getAllWithBooksAndCustomers()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> requestBookService.sortRequest("по алфавиту")
        );
    }

    @Test
    void getAll_shouldReturnAllRequests() {
        when(requestBookDAO.getAllWithBooksAndCustomers()).thenReturn(requestBooks);
        when(requestBookMapper.toRequestBookResponseList(requestBooks)).thenReturn(requestBookResponses);

        List<RequestBookResponse> result = requestBookService.getAll();

        assertEquals(requestBookResponses, result);
        verify(requestBookDAO).getAllWithBooksAndCustomers();
        verify(requestBookMapper).toRequestBookResponseList(requestBooks);
    }

    @Test
    void getAll_shouldThrowExceptionWhenDaoFails() {
        when(requestBookDAO.getAllWithBooksAndCustomers()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> requestBookService.getAll()
        );
    }

    @Test
    void getById_shouldReturnRequestResponse() {
        when(requestBookDAO.findById(1)).thenReturn(requestBook);
        when(requestBookMapper.toRequestBookResponse(requestBook)).thenReturn(requestBookResponses.get(0));

        RequestBookResponse result = requestBookService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.requestId());
        verify(requestBookDAO).findById(1);
        verify(requestBookMapper).toRequestBookResponse(requestBook);
    }

    @Test
    void getById_shouldThrowExceptionWhenRequestNotFound() {
        when(requestBookDAO.findById(999)).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> requestBookService.getById(999)
        );
    }

    @Test
    void add_shouldCreateNewRequestWhenIdDoesNotExist() {
        RequestBook newRequest = new RequestBook(customer, book);
        newRequest.setRequestId(999);
        when(requestBookDAO.findById(999)).thenReturn(null);

        requestBookService.add(newRequest);

        verify(requestBookDAO).findById(999);
        verify(requestBookDAO).create(newRequest);
        verify(requestBookService, never()).update(any());
    }

    @Test
    void add_shouldUpdateExistingRequestWhenIdExists() {
        RequestBook existingRequest = new RequestBook(customer, book);
        existingRequest.setRequestId(1);

        when(requestBookDAO.findById(1)).thenReturn(existingRequest);

        requestBookService.add(existingRequest);

        verify(requestBookDAO).findById(1);
        verify(requestBookService).update(existingRequest);
        verify(requestBookDAO, never()).create(any());
    }

    @Test
    void add_shouldThrowExceptionOnDaoError() {
        RequestBook requestWithId = new RequestBook(customer, book);
        requestWithId.setRequestId(1);

        when(requestBookDAO.findById(1)).thenReturn(null);
        doThrow(new DaoException()).when(requestBookDAO).create(requestWithId);

        assertThrows(ServiceException.class,
                () -> requestBookService.add(requestWithId),
                "Должен выбрасывать ServiceException при ошибке DAO"
        );

        verify(requestBookDAO).findById(1);
        verify(requestBookDAO).create(requestWithId);
    }

    @Test
    void update_shouldUpdateRequestSuccessfully() {
        doNothing().when(requestBookDAO).update(requestBook);
        requestBookService.update(requestBook);

        verify(requestBookDAO).update(requestBook);
    }

    @Test
    void update_shouldThrowExceptionOnDaoError() {
        doThrow(new DaoException()).when(requestBookDAO).update(requestBook);

        assertThrows(ServiceException.class,
                () -> requestBookService.update(requestBook),
                "Должен выбрасывать ServiceException при ошибке DAO"
        );

        verify(requestBookDAO).update(requestBook);
    }

    @Test
    void getAllRequest_shouldReturnAllRequestsSuccessfully() {
        when(requestBookDAO.getAll()).thenReturn(requestBooks);
        List<RequestBook> result = requestBookService.getAllRequest();

        assertNotNull(result);
        assertEquals(requestBooks, result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getBook().getName());
        verify(requestBookDAO).getAll();
    }

    @Test
    void getAllRequest_shouldThrowServiceExceptionWhenDaoFails() {
        when(requestBookDAO.getAll()).thenThrow(new DaoException());

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> requestBookService.getAllRequest(),
                "Должен выбрасывать ServiceException при ошибке DAO"
        );

        assertTrue(exception.getMessage().contains("Fail to get all requests in RequestBookService in getAllRequest()"));
        assertTrue(exception.getCause() instanceof DaoException);
        verify(requestBookDAO).getAll();
    }

    @Test
    void createRequest_shouldCreateRequestSuccessfully() {
        RequestBookRequest requestBookRequest = new RequestBookRequest(new CustomerRequest(
                "Test Customer", 19, "+795055855", "test@email.com",
                "address"
        ), book.getBookId());

        when(customerService.findByEmail("test@email.com")).thenReturn(customer);
        when(bookDAO.findById(1)).thenReturn(book);

        requestBookService.createRequest(requestBookRequest);

        verify(bookDAO).findById(1);
        verify(customerService).findByEmail("test@email.com");
        verify(requestBookService).add(any(RequestBook.class));
    }

    @Test
    void createRequest_shouldThrowServiceExceptionOnDaoError() {
        RequestBookRequest requestBookRequest = new RequestBookRequest(
                new CustomerRequest("Test Customer", 19, "+795055855", "test@email.com", "address"),
                book.getBookId()
        );

        when(customerService.findByEmail("test@email.com")).thenReturn(customer);
        when(bookDAO.findById(1)).thenReturn(book);

        doThrow(new DaoException())
                .when(requestBookService).add(any(RequestBook.class));

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> requestBookService.createRequest(requestBookRequest),
                "Должен выбрасывать ServiceException при ошибке в add()"
        );

        assertTrue(exception.getMessage().contains("Fail to create request in RequestBookService"));
        assertTrue(exception.getCause() instanceof DaoException);

        verify(bookDAO).findById(1);
        verify(customerService).findByEmail("test@email.com");
        verify(requestBookService).add(any(RequestBook.class));
    }


    @Test
    void getAllRequestBook_shouldReturnAllRequestBooksSuccessfully() {
        when(requestBookDAO.getAll()).thenReturn(requestBooks);

        List<RequestBook> result = requestBookService.getAllRequestBook();

        assertNotNull(result);
        assertEquals(requestBooks, result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getBook().getName());
        verify(requestBookDAO).getAll();
    }

    @Test
    void getAllRequestBook_shouldThrowServiceExceptionWhenDaoFails() {
        when(requestBookDAO.getAll()).thenThrow(new DaoException());

        ServiceException exception = assertThrows(
                ServiceException.class,
                () -> requestBookService.getAllRequestBook(),
                "Должен выбрасывать ServiceException при ошибке DAO"
        );

        assertTrue(exception.getMessage().contains("Fail to get all requests in RequestBookService in getAll()"));
        assertTrue(exception.getCause() instanceof DaoException);
        verify(requestBookDAO).getAll();
    }

}
