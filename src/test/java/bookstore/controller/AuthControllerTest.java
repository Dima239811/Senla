package bookstore.controller;

import bookstore.dto.JwtResponse;
import bookstore.dto.LoginRequest;
import bookstore.dto.RegisterRequest;
import bookstore.exception.ServiceException;
import bookstore.service.AuthService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void register_shouldWork() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "password123",
                "Дмитрий",
                20,
                "+7 (999) 123-45-67",
                "dima@mail.ru",
                "г. Москва, ул. Ленина, д. 1"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    void register_shouldReturnError() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "password123",
                "Дмитрий",
                20,
                "+7 (999) 123-45-67",
                "dima@mail.ru",
                "г. Москва, ул. Ленина, д. 1"
        );

        doThrow(new ServiceException())
                .when(authService)
                .register(any());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturnJwt() throws Exception {
        LoginRequest request = new LoginRequest("user", "password");

        JwtResponse response = new JwtResponse("test-token");

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void login_shouldReturnError() throws Exception {
        LoginRequest request = new LoginRequest("user", "password");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new ServiceException());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
