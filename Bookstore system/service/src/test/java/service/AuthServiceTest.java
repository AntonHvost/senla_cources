package service;

import domain.model.impl.User;
import dto.request.LoginRequestDto;
import dto.request.RegisterRequestDto;
import dto.response.AuthResponse;
import enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserServiceInterface userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_returnsAuthResponse_whenAuthenticated() {
        LoginRequestDto req = new LoginRequestDto("a@b.c", "secret");
        User user = new User();
        user.setId(1L);
        user.setUsername("a@b.c");
        user.setRole(Role.USER);
        domain.model.impl.RefreshToken rt = new domain.model.impl.RefreshToken();
        rt.setToken("refresh");

        doNothing().when(authenticationManager).authenticate(any());
        when(userService.loadUserByUsername("a@b.c")).thenReturn(user);
        when(userRepository.findByEmail("a@b.c")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("access");
        when(refreshTokenService.createRefreshToken(1L)).thenReturn(rt);

        AuthResponse resp = authService.login(req);

        assertEquals("access", resp.getAccessToken());
        assertEquals("refresh", resp.getRefreshToken());
        assertEquals(1L, resp.getUserId());
    }

    @Test
    void login_throws_whenAuthenticationFails() {
        LoginRequestDto req = new LoginRequestDto("a@b.c", "bad");
        doThrow(new BadCredentialsException("bad")).when(authenticationManager).authenticate(any());

        assertThrows(BadCredentialsException.class, () -> authService.login(req));
    }

    @Test
    void register_returnsAuthResponse_whenEmailFree() {
        RegisterRequestDto req = new RegisterRequestDto("new@x.y", "pw", Role.USER);
        when(userRepository.findByEmail("new@x.y")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pw")).thenReturn("hash");
        doAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(99L);
            return null;
        }).when(userService).save(any(User.class));
        when(jwtService.generateToken(any(User.class))).thenReturn("access");
        domain.model.impl.RefreshToken rt = new domain.model.impl.RefreshToken();
        rt.setToken("refresh");
        when(refreshTokenService.createRefreshToken(99L)).thenReturn(rt);

        AuthResponse resp = authService.register(req);

        assertEquals("access", resp.getAccessToken());
        verify(userService).save(any(User.class));
    }

    @Test
    void register_throws_whenEmailExists() {
        RegisterRequestDto req = new RegisterRequestDto("dup@x.y", "pw", Role.USER);
        when(userRepository.findByEmail("dup@x.y")).thenReturn(Optional.of(new User()));

        assertThrows(UsernameNotFoundException.class, () -> authService.register(req));
    }
}
