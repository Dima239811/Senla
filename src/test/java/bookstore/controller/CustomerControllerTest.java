package bookstore.controller;

import bookstore.dto.CustomerResponse;
import bookstore.service.entityService.CustomerService;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void getCustomerById_shouldReturnCustomerWhenFound() throws Exception {
        int customerId = 1;
        CustomerResponse expectedCustomer = new CustomerResponse(customerId, "Dima");

        when(customerService.getById(customerId)).thenReturn(expectedCustomer);

        mockMvc.perform(get("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerID").value(customerId))
                .andExpect(jsonPath("$.fullName").value("Dima"));

        verify(customerService).getById(customerId);
    }

    @Test
    void getCustomerById_shouldReturnNotFoundWhenCustomerDoesNotExist() throws Exception {
        int nonExistentId = 999;

        when(customerService.getById(nonExistentId)).thenReturn(null);

        mockMvc.perform(get("/customers/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(""));

        verify(customerService).getById(nonExistentId);
    }

    @Test
    void getAllCustomers_shouldReturnAllCustomersWhenListIsNotEmpty() throws Exception {
        List<CustomerResponse> expectedCustomers = Arrays.asList(
                new CustomerResponse(1, "Dima"),
                new CustomerResponse(2, "Jane"));

        when(customerService.getAll()).thenReturn(expectedCustomers);

        mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customerID").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Dima"))
                .andExpect(jsonPath("$[1].customerID").value(2))
                .andExpect(jsonPath("$[1].fullName").value("Jane"));

        verify(customerService).getAll();
    }

    @Test
    void getAllCustomers_shouldReturnEmptyListWhenNoCustomersExist() throws Exception {
        List<CustomerResponse> emptyList = List.of();

        when(customerService.getAll()).thenReturn(emptyList);

        mockMvc.perform(get("/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(customerService).getAll();
    }
}
