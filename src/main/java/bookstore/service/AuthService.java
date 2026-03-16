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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserDAO userDAO;
    private final CustomerDAO customerDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(UserDAO userRepository,
                       CustomerDAO customerDAO,
                       PasswordEncoder passwordEncoder, JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService) {
        this.userDAO = userRepository;
        this.customerDAO = customerDAO;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Transactional
    public void register(RegisterRequest request) {

        User user = new User();
        user.setLogin(request.login());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        userDAO.create(user);

        Customer customer = new Customer(
                request.fullName(),
                request.age(),
                request.phoneNumber(),
                request.email(),
                request.address(),
                user
        );

        customerDAO.create(customer);
    }

    public JwtResponse login(LoginRequest request) {
        var user = customUserDetailsService.loadUserByUsername(request.login());

        if (user == null || !checkPassword(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid login or password");
        }

        String token = jwtUtils.generateJwtToken(user);
        return new JwtResponse(token);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
