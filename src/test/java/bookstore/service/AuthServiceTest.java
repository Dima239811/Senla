package bookstore.service;

import bookstore.dto.JwtResponse;
import bookstore.dto.LoginRequest;
import bookstore.dto.RegisterRequest;
import bookstore.enums.Role;
import bookstore.model.entity.Customer;
import bookstore.model.entity.User;
import bookstore.repo.CustomerDAO;
import bookstore.repo.UserDAO;
import bookstore.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserDAO userDAO;

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldCreateUserAndCustomerSuccessfully() {
        RegisterRequest request = new RegisterRequest(
                "testuser",
                "password123",
                "Дмитрий",
                20,
                "+7 (999) 123-45-67",
                "dima@mail.ru",
                "г. Москва, ул. Ленина, д. 1"
        );

        User expectedUser = new User();
        expectedUser.setLogin("testuser");
        expectedUser.setPassword("encoded_password");
        expectedUser.setRole(Role.USER);

        Customer expectedCustomer = new Customer(
                "Дмитрий",
                20,
                "+7 (999) 123-45-67",
                "dima@mail.ru",
                "г. Москва, ул. Ленина, д. 1",
                expectedUser
        );

        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");

        authService.register(request);

        verify(userDAO, times(1)).create(expectedUser);
        verify(customerDAO, times(1)).create(expectedCustomer);
    }

    @Test
    void login_shouldReturnJwtResponseWhenCredentialsValid() {
        LoginRequest request = new LoginRequest("testuser", "password123");

        UserDetails userDetails = mock(UserDetails.class);

        when(userDetails.getPassword()).thenReturn("encoded_password");

        when(customUserDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(passwordEncoder.matches(eq("password123"), eq("encoded_password"))).thenReturn(true);
        when(jwtUtils.generateJwtToken(userDetails)).thenReturn("jwt_token");

        JwtResponse result = authService.login(request);

        assertNotNull(result);
        assertEquals("jwt_token", result.token());

        verify(customUserDetailsService, times(1)).loadUserByUsername("testuser");
        verify(passwordEncoder, times(1)).matches(eq("password123"), eq("encoded_password"));
        verify(jwtUtils, times(1)).generateJwtToken(userDetails);
    }



    @Test
    void login_shouldThrowExceptionWhenUserNotFound() {
        LoginRequest request = new LoginRequest("nonexistent", "password");

        when(customUserDetailsService.loadUserByUsername("nonexistent")).thenReturn(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        assertEquals("Invalid login or password", exception.getMessage());
        verify(customUserDetailsService, times(1)).loadUserByUsername("nonexistent");
    }

    @Test
    void login_shouldThrowExceptionWhenPasswordIncorrect() {
        LoginRequest request = new LoginRequest("testuser", "wrong_password");

        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(passwordEncoder.matches("wrong_password", "encoded_password")).thenReturn(false);

        assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        verify(customUserDetailsService).loadUserByUsername("testuser");
    }


    @Test
    void checkPassword_shouldReturnTrueWhenPasswordMatches() {
        String rawPassword = "password123";
        String encodedPassword = "encoded_password";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = authService.checkPassword(rawPassword, encodedPassword);

        assertTrue(result);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }

    @Test
    void checkPassword_shouldReturnFalseWhenPasswordDoesNotMatch() {
        String rawPassword = "wrong_password";
        String encodedPassword = "encoded_password";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        boolean result = authService.checkPassword(rawPassword, encodedPassword);

        assertFalse(result);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }
}
