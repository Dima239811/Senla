package bookstore.service;

import bookstore.enums.Role;
import bookstore.model.entity.User;
import bookstore.repo.UserDAO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;

    public CustomUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByLogin(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with login: " + username);
        }


        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities(mapRolesToAuthorities(user.getRole()))
                .disabled(false)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }

    Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role role) {
        return Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }
}
