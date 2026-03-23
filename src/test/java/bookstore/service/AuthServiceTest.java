package bookstore.service;

import bookstore.dto.JwtResponse;
import bookstore.dto.LoginRequest;
import bookstore.enums.Role;
import bookstore.repo.CustomerDAO;
import bookstore.repo.UserDAO;
import bookstore.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertEquals;


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

//    @Test
//    void testRegister() {
//        LoginRequest request = new LoginRequest("john", "1234");
//
//        UserDetails userDetails = new User(
//                1,
//                "Dima",
//                "encodedPassword",
//                Role.USER
//        );
//
//        Mockito.when(customUserDetailsService.loadUserByUsername("john"))
//                .thenReturn(userDetails);
//
//        Mockito.when(passwordEncoder.matches("1234", "encodedPassword"))
//                .thenReturn(true);
//
//        Mockito.when(jwtUtils.generateJwtToken(userDetails))
//                .thenReturn("fake-jwt-token");
//
//        JwtResponse response = authService.login(request);
//
//        assertNotNull(response);
//        assertEquals("fake-jwt-token", response.token());
//    }


}
