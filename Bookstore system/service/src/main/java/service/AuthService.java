package service;

import domain.model.impl.User;
import dto.request.LoginRequestDto;
import dto.request.RegisterRequestDto;
import dto.response.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.UserRepository;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final UserServiceInterface userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       UserServiceInterface userService,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequestDto req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        UserDetails userDetails = userService.loadUserByUsername(req.getEmail());
        User user = userRepository.findByEmail(userDetails.getUsername()).get();

        String accessToken = jwtService.generateToken(userDetails);

        return new AuthResponse(accessToken, user.getId(), user.getUsername());
    }

    public AuthResponse register(RegisterRequestDto req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new UsernameNotFoundException("Email already exists");
        }

        User user = new User();
        user.setUsername(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole());

        userService.save(user);



        return new AuthResponse(null, user.getId(), user.getUsername());
    }
}
