package bookstore.service;

import bookstore.dto.OrderResponse;
import bookstore.enums.OrderStatus;
import bookstore.enums.StatusBook;
import bookstore.exception.DaoException;
import bookstore.exception.ServiceException;
import bookstore.model.entity.Book;
import bookstore.model.entity.Customer;
import bookstore.model.entity.Order;
import bookstore.model.entity.User;
import bookstore.model.mapper.OrderMapper;
import bookstore.repo.BookDAO;
import bookstore.repo.CustomerDAO;
import bookstore.repo.OrderDAO;
import bookstore.service.entityService.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderDAO orderDAO;

    @Mock
    private BookDAO bookDAO;

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private List<Order> orders;
    private List<OrderResponse> orderResponses;
    private Order order;
    private Book book;
    private Customer customer;

    @BeforeEach
    void setUp() {
        book = new Book("Test Book", "Author", 2020, 100.0, StatusBook.IN_STOCK);
        book.setBookId(1);

        customer = new Customer("Test Customer", 19, "+795055855", "test@email.com",
                "address", new User());
        customer.setCustomerID(1);

        order = new Order(book, customer, new Date(), 100.0);
        order.setOrderId(1);
        order.setStatus(OrderStatus.WAITING_FOR_BOOK);

        orders = new ArrayList<>();
        orders.add(order);

        OrderResponse orderResponse = new OrderResponse(
                1, 1, "Test Book", "Author",1, "Test Customer",
                new Date(), 100.0, "WAITING_FOR_BOOK"
        );
        orderResponses = new ArrayList<>();
        orderResponses.add(orderResponse);
    }

    @Test
    void cancelOrder_shouldCancelExistingOrder() {
        when(orderDAO.findById(1)).thenReturn(order);
        orderService.cancelOrder(1);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderDAO).update(order);
    }

    @Test
    void cancelOrder_shouldThrowExceptionWhenOrderNotFound() {
        when(orderDAO.findById(1)).thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> orderService.cancelOrder(1),
                "Должен выбрасывать RuntimeException при отсутствии заказа"
        );
        verify(orderDAO, never()).update(any());
    }

    @Test
    void cancelOrder_shouldThrowServiceExceptionOnDaoError() {
        when(orderDAO.findById(1)).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> orderService.cancelOrder(1),
                "Должен выбрасывать ServiceException при ошибке DAO"
        );
    }

    @Test
    void changeOrderStatus_shouldChangeStatusSuccessfully() {
        when(orderDAO.findById(1)).thenReturn(order);
        orderService.changeOrderStatus(1, "выполнен");

        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        verify(orderDAO).update(order);
    }

    @Test
    void changeOrderStatus_shouldThrowExceptionWhenOrderNotFound() {
        when(orderDAO.findById(1)).thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> orderService.changeOrderStatus(1, "выполнен")
        );
        verify(orderDAO, never()).update(any());
    }

    @Test
    void sortOrders_shouldSortByDate() {
        when(orderDAO.getAllWithBooksAndCustomers()).thenReturn(orders);
        when(orderMapper.toOrderResponseList(orders)).thenReturn(orderResponses);

        List<OrderResponse> result = orderService.sortOrders("по дате");

        assertNotNull(result);
        verify(orderDAO).getAllWithBooksAndCustomers();
        verify(orderMapper).toOrderResponseList(orders);
    }

    @Test
    void sortOrders_shouldSortByPrice() {
        when(orderDAO.getAllWithBooksAndCustomers()).thenReturn(orders);
        when(orderMapper.toOrderResponseList(orders)).thenReturn(orderResponses);
        List<OrderResponse> result = orderService.sortOrders("по цене");

        assertNotNull(result);
        verify(orderDAO).getAllWithBooksAndCustomers();
    }

    @Test
    void sortOrders_shouldSortByStatus() {
        when(orderDAO.getAllWithBooksAndCustomers()).thenReturn(orders);
        when(orderMapper.toOrderResponseList(orders)).thenReturn(orderResponses);

        List<OrderResponse> result = orderService.sortOrders("по статусу");

        assertNotNull(result);
        verify(orderDAO).getAllWithBooksAndCustomers();
    }

    @Test
    void sortOrders_shouldReturnUnsortedOnUnknownCriteria() {
        when(orderDAO.getAllWithBooksAndCustomers()).thenReturn(orders);
        when(orderMapper.toOrderResponseList(orders)).thenReturn(orderResponses);

        List<OrderResponse> result = orderService.sortOrders("неизвестный критерий");

        assertEquals(orderResponses, result);
        verify(orderDAO).getAllWithBooksAndCustomers();
    }

    @Test
    void sortOrders_shouldThrowExceptionOnDaoError() {
        when(orderDAO.getAllWithBooksAndCustomers()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> orderService.sortOrders("по дате")
        );
    }

    @Test
    void sortPerformOrders_shouldSortByDateForCompletedOrders() {
        Date from = new Date(System.currentTimeMillis() - 86400000);
        Date to = new Date();

        Order completedOrder = new Order(book, customer, new Date(), 150.0);
        completedOrder.setOrderId(2);
        completedOrder.setStatus(OrderStatus.COMPLETED);

        List<Order> completedOrders = List.of(completedOrder);

        when(orderDAO.getAllWithBooksAndCustomers()).thenReturn(List.of(order, completedOrder));
        when(orderMapper.toOrderResponseList(completedOrders)).thenReturn(orderResponses);

        List<OrderResponse> result = orderService.sortPerformOrders("по дате", from, to);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderDAO).getAllWithBooksAndCustomers();
    }

    @Test
    void sortPerformOrders_shouldReturnEmptyListWhenNoCompletedOrders() {
        Date from = new Date(System.currentTimeMillis() - 86400000);
        Date to = new Date();

        when(orderDAO.getAllWithBooksAndCustomers()).thenReturn(orders);
        when(orderMapper.toOrderResponseList(List.of())).thenReturn(List.of());

        List<OrderResponse> result = orderService.sortPerformOrders("по дате", from, to);

        assertTrue(result.isEmpty());
        verify(orderDAO).getAllWithBooksAndCustomers();
    }

    @Test
    void sortPerformOrders_shouldThrowExceptionOnDaoError() {
        Date from = new Date();
        Date to = new Date();

        when(orderDAO.getAllWithBooksAndCustomers()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> orderService.sortPerformOrders("по дате", from, to)
        );
    }

    @Test
    void calculateIncomeForPeriod_shouldCalculateCorrectIncome() {
        Date from = new Date(System.currentTimeMillis() - 86400000);
        Date to = new Date();

        Order completedOrder1 = new Order(book, customer, new Date(), 200.0);
        completedOrder1.setStatus(OrderStatus.COMPLETED);

        Order completedOrder2 = new Order(book, customer, new Date(), 300.0);
        completedOrder2.setStatus(OrderStatus.COMPLETED);

        List<Order> completedOrders = List.of(completedOrder1, completedOrder2);

        when(orderDAO.getAllWithBooksAndCustomers()).thenReturn(completedOrders);

        double income = orderService.calculateIncomeForPeriod(from, to);

        assertEquals(500.0, income);
        verify(orderDAO).getAllWithBooksAndCustomers();
    }

    @Test
    void calculateIncomeForPeriod_shouldReturnZeroWhenNoCompletedOrders() {
        Date from = new Date(System.currentTimeMillis() - 86400000);
        Date to = new Date();

        when(orderDAO.getAllWithBooksAndCustomers()).thenReturn(orders);

        double income = orderService.calculateIncomeForPeriod(from, to);

        assertEquals(0.0, income);
    }

    @Test
    void calculateIncomeForPeriod_shouldThrowExceptionOnDaoError() {
        Date from = new Date();
        Date to = new Date();

        when(orderDAO.getAllWithBooksAndCustomers()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> orderService.calculateIncomeForPeriod(from, to)
        );
    }

    @Test
    void getCountPerformedOrder_shouldCountCompletedOrdersCorrectly() {
        Date from = new Date(System.currentTimeMillis() - 86400000);
        Date to = new Date();

        Order completedOrder = new Order(book, customer, new Date(), 100.0);
        completedOrder.setStatus(OrderStatus.COMPLETED);

        when(orderDAO.getAllWithBooksAndCustomers()).thenReturn(List.of(order, completedOrder));

        int count = orderService.getCountPerformedOrder(from, to);

        assertEquals(1, count);
        verify(orderDAO).getAllWithBooksAndCustomers();
    }

    @Test
    void getCountPerformedOrder_shouldReturnZeroWhenNoCompletedOrders() {
        Date from = new Date(System.currentTimeMillis() - 86400000);
        Date to = new Date();

        when(orderDAO.getAllWithBooksAndCustomers()).thenReturn(orders);

        int count = orderService.getCountPerformedOrder(from, to);

        assertEquals(0, count);
    }

    @Test
    void getCountPerformedOrder_shouldThrowExceptionOnDaoError() {
        Date from = new Date();
        Date to = new Date();

        when(orderDAO.getAllWithBooksAndCustomers()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> orderService.getCountPerformedOrder(from, to)
        );
    }

    @Test
    void getAll_shouldReturnAllOrdersSuccessfully() {
        when(orderDAO.getAllWithBooksAndCustomers()).thenReturn(orders);
        when(orderMapper.toOrderResponseList(orders)).thenReturn(orderResponses);

        List<OrderResponse> result = orderService.getAll();

        assertEquals(orderResponses, result);
        verify(orderDAO).getAllWithBooksAndCustomers();
        verify(orderMapper).toOrderResponseList(orders);
    }

    @Test
    void getAll_shouldThrowExceptionWhenDaoFails() {
        when(orderDAO.getAllWithBooksAndCustomers()).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> orderService.getAll()
        );
    }

    @Test
    void getById_shouldReturnOrderResponse() {
        when(orderDAO.findById(1)).thenReturn(order);
        when(orderMapper.toOrderResponse(order)).thenReturn(orderResponses.get(0));
        OrderResponse result = orderService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.orderId());
        verify(orderDAO).findById(1);
        verify(orderMapper).toOrderResponse(order);
    }

    @Test
    void getById_shouldThrowExceptionWhenOrderNotFound() {
        when(orderDAO.findById(999)).thenThrow(new DaoException());

        assertThrows(ServiceException.class,
                () -> orderService.getById(999)
        );
    }

    @Test
    void add_shouldCreateNewOrder() {
        Order newOrder = new Order(book, customer, new Date(), 200.0);
        when(orderDAO.findById(anyInt())).thenReturn(null);

        orderService.add(newOrder);

        verify(orderDAO).create(newOrder);
        verify(orderDAO, never()).update(any());
    }

    @Test
    void add_shouldUpdateExistingOrder() {
        Order existingOrder = new Order(book, customer, new Date(), 200.0);
        existingOrder.setOrderId(1);
        when(orderDAO.findById(1)).thenReturn(existingOrder);

        orderService.add(existingOrder);

        verify(orderDAO).update(existingOrder);
        verify(orderDAO, never()).create(any());
    }

    @Test
    void add_shouldThrowExceptionWhenCustomerInvalid() {
        Order invalidOrder = new Order(null, null, new Date(), 100.0);

        assertThrows(IllegalArgumentException.class,
                () -> orderService.add(invalidOrder),
                "Должен выбрасывать IllegalArgumentException при отсутствии клиента"
        );
    }

    @Test
    void add_shouldThrowExceptionWhenBookInvalid() {
        Order invalidOrder = new Order(null, customer, new Date(), 100.0);

        assertThrows(IllegalArgumentException.class,
                () -> orderService.add(invalidOrder),
                "Должен выбрасывать IllegalArgumentException при отсутствии книги"
        );
    }

    @Test
    void add_shouldThrowServiceExceptionOnDaoError() {
        Order orderWithCustomerAndBook = new Order(book, customer, new Date(), 200.0);
        orderWithCustomerAndBook.setOrderId(1);

        when(orderDAO.findById(1)).thenReturn(orderWithCustomerAndBook);
        doThrow(new DaoException()).when(orderDAO).update(orderWithCustomerAndBook);

        assertThrows(ServiceException.class,
                () -> orderService.add(orderWithCustomerAndBook),
                "Должен выбрасывать ServiceException при ошибке DAO"
        );

        verify(orderDAO).findById(1);
        verify(orderDAO).update(orderWithCustomerAndBook);
    }
}
