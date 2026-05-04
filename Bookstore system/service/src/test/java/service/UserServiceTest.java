package service;

import domain.model.impl.User;
import enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void loadUserByUsername_returnsUserDetails_whenUserExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("a@b.c");
        user.setPassword("p");
        user.setRole(Role.USER);
        when(userRepository.findByEmail("a@b.c")).thenReturn(Optional.of(user));

        UserDetails details = userService.loadUserByUsername("a@b.c");

        assertSame(user, details);
        assertEquals("a@b.c", details.getUsername());
    }

    @Test
    void loadUserByUsername_throws_whenUserMissing() {
        when(userRepository.findByEmail("x@y.z")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("x@y.z"));
    }

    @Test
    void save_delegatesToRepository() {
        User user = new User();
        user.setUsername("u");

        userService.save(user);

        verify(userRepository).save(user);
    }
}
