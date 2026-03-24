package bookstore.controller;

import bookstore.dto.RequestBookRequest;
import bookstore.dto.RequestBookResponse;
import bookstore.enums.RequestStatus;
import bookstore.exception.ServiceException;
import bookstore.service.entityService.RequestBookService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RequestBookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RequestBookService requestBookService;

    @InjectMocks
    private RequestBookController requestBookController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(requestBookController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllRequestBook_shouldReturnList() throws Exception {
        List<RequestBookResponse> list = Arrays.asList(
                new RequestBookResponse(1, 1, "book1", "tolstoy", 1,
                        "Dima", "Открыт"),
                new RequestBookResponse(2, 2, "book2", "tolstoy", 2,
                        "Dima", "Открыт")
        );

        when(requestBookService.getAll()).thenReturn(list);

        mockMvc.perform(get("/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(requestBookService).getAll();
    }

    @Test
    void getAllRequestBook_shouldReturnError() throws Exception {
        when(requestBookService.getAll())
                .thenThrow(new ServiceException("getAllRequestBook_shouldReturnError"));

        mockMvc.perform(get("/requests"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addRequest_shouldWork() throws Exception {
        String json = """
                {
                    "customerId": 1,
                    "bookId": 2
                }
                """;

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(requestBookService).createRequest(any(RequestBookRequest.class));
    }

    @Test
    void addRequest_shouldReturnError() throws Exception {
        doThrow(new ServiceException("addRequest_shouldReturnError"))
                .when(requestBookService)
                .createRequest(any());

        String json = """
                {
                    "customerId": 1,
                    "bookId": 2
                }
                """;

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sortRequest_shouldReturnSortedList() throws Exception {
        List<RequestBookResponse> list = Arrays.asList(
                new RequestBookResponse(1, 1, "book1", "tolstoy", 1,
                        "Dima", "Открыт")
        );

        when(requestBookService.sortRequest("date")).thenReturn(list);

        mockMvc.perform(get("/requests/sort")
                        .param("criteria", "date"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(requestBookService).sortRequest("date");
    }

    @Test
    void sortRequest_shouldReturnError() throws Exception {
        when(requestBookService.sortRequest("date"))
                .thenThrow(new ServiceException("sortRequest_shouldReturnError"));

        mockMvc.perform(get("/requests/sort")
                        .param("criteria", "date"))
                .andExpect(status().isBadRequest());
    }
}
