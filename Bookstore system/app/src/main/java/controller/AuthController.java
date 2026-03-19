package controller;

import dto.request.LoginRequestDto;
import dto.request.RegisterRequestDto;
import dto.response.AuthResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.AuthService;
import service.JwtService;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDto loginRequestDto) {
        AuthResponse authResponse = this.authService.login(loginRequestDto);
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(authResponse.getToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {
        AuthResponse authResponse = this.authService.register(registerRequestDto);
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(authResponse.getToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body("User registered successfully!");
    }

}
