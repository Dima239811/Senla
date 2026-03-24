package bookstore.service;

import bookstore.enums.Role;
import bookstore.model.entity.User;
import bookstore.repo.UserDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        User user = new User(1, "testuser", "password123", Role.USER);

        Mockito.when(userDAO.findByLogin("testuser")).thenReturn(user);

        UserDetails result = customUserDetailsService.loadUserByUsername("testuser");

        assertEquals("testuser", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertEquals(1, result.getAuthorities().size());
        assertEquals("ROLE_USER", result.getAuthorities().iterator().next().getAuthority());
        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isCredentialsNonExpired());
        assertTrue(result.isEnabled());
    }

    @Test
    void mapRolesToAuthorities_shouldReturnCorrectAuthority() {
        Collection<? extends GrantedAuthority> authorities = customUserDetailsService.mapRolesToAuthorities(Role.ADMIN);

        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserDoesNotExist() {
        Mockito.when(userDAO.findByLogin("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonexistent");
        });
    }
}
