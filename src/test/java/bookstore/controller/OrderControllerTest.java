package bookstore.controller;

import bookstore.dto.OrderRequest;
import bookstore.dto.OrderResponse;
import bookstore.exception.ServiceException;
import bookstore.service.entityService.OrderService;
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
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createOrder_shouldWork() throws Exception {
        OrderRequest request = new OrderRequest(1, 1);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(orderService).createOrder(any());
    }

    @Test
    void createOrder_shouldReturnError() throws Exception {
        doThrow(new ServiceException())
                .when(orderService).createOrder(any());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelOrder_shouldWork() throws Exception {
        mockMvc.perform(patch("/orders/1/cancel"))
                .andExpect(status().isOk());

        verify(orderService).cancelOrder(1);
    }

    @Test
    void cancelOrder_shouldFail() throws Exception {
        doThrow(new ServiceException())
                .when(orderService).cancelOrder(1);

        mockMvc.perform(patch("/orders/1/cancel"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeStatus_shouldWork() throws Exception {
        mockMvc.perform(patch("/orders/1/status")
                        .param("status", "NEW"))
                .andExpect(status().isOk());

        verify(orderService).changeOrderStatus(1, "NEW");
    }

    @Test
    void changeStatus_shouldFail() throws Exception {
        doThrow(new ServiceException())
                .when(orderService).changeOrderStatus(anyInt(), anyString());

        mockMvc.perform(patch("/orders/1/status")
                        .param("status", "WRONG"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        List<OrderResponse> list = Arrays.asList(
                new OrderResponse(
                        1, 1, "Test Book", "Author",1, "Test Customer",
                        new Date(), 100.0, "WAITING_FOR_BOOK"
                ),
                new OrderResponse(
                        2, 1, "Test Book", "Author",1, "Test Customer",
                        new Date(), 100.0, "WAITING_FOR_BOOK"
                )
        );

        when(orderService.getAll()).thenReturn(list);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAll_shouldFail() throws Exception {
        when(orderService.getAll())
                .thenThrow(new ServiceException());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sortOrders_shouldWork() throws Exception {
        when(orderService.sortOrders("price"))
                .thenReturn(Arrays.asList(new OrderResponse(
                        1, 1, "Test Book", "Author",1, "Test Customer",
                        new Date(), 100.0, "WAITING_FOR_BOOK"
                )));

        mockMvc.perform(get("/orders/sort")
                        .param("criteria", "price"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void sortOrders_shouldFail() throws Exception {
        when(orderService.sortOrders(any()))
                .thenThrow(new ServiceException());

        mockMvc.perform(get("/orders/sort")
                        .param("criteria", "wrong"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateIncome_shouldWork() throws Exception {
        when(orderService.calculateIncomeForPeriod(any(), any()))
                .thenReturn(1000.0);

        mockMvc.perform(get("/orders/income")
                        .param("from", "01.01.2024")
                        .param("to", "10.01.2024"))
                .andExpect(status().isOk())
                .andExpect(content().string("1000.0"));
    }

    @Test
    void calculateIncome_shouldFail() throws Exception {
        when(orderService.calculateIncomeForPeriod(any(), any()))
                .thenThrow(new ServiceException());

        mockMvc.perform(get("/orders/income")
                        .param("from", "01.01.2024")
                        .param("to", "10.01.2024"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void countOrders_shouldWork() throws Exception {
        when(orderService.getCountPerformedOrder(any(), any()))
                .thenReturn(5);

        mockMvc.perform(get("/orders/count")
                        .param("from", "01.01.2024")
                        .param("to", "10.01.2024"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void countOrders_shouldFail() throws Exception {
        when(orderService.getCountPerformedOrder(any(), any()))
                .thenThrow(new ServiceException());

        mockMvc.perform(get("/orders/count")
                        .param("from", "01.01.2024")
                        .param("to", "10.01.2024"))
                .andExpect(status().isBadRequest());
    }
}
