package controller;

import dto.request.LoginRequestDto;
import dto.request.RefreshTokenRequestDto;
import dto.request.RegisterRequestDto;
import dto.response.AuthResponse;
import dto.response.RefreshTokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.AuthService;
import service.JwtService;
import service.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestDto loginRequestDto) {
        AuthResponse authResponse = this.authService.login(loginRequestDto);
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(authResponse.getAccessToken());
        ResponseCookie refreshCookie = refreshTokenService.generateRefreshTokenCookie(authResponse.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {
        AuthResponse authResponse = this.authService.register(registerRequestDto);
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(authResponse.getAccessToken());
        ResponseCookie refreshCookie = refreshTokenService.generateRefreshTokenCookie(authResponse.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body("User registered successfully!");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request) {
        String refreshToken = refreshTokenService.getRefreshTokenFromCookie(request);
        RefreshTokenResponseDto refreshTokenResponse = refreshTokenService
                .generateNewToken(new RefreshTokenRequestDto(refreshToken));
        System.out.println("Refresh Token:" + refreshTokenResponse.getToken());
        ResponseCookie newJwtCookie = jwtService.generateJwtCookie(refreshTokenResponse.getToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newJwtCookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String refreshToken = refreshTokenService.getRefreshTokenFromCookie(request);
        if(refreshToken != null) {
            refreshTokenService.deleteByToken(refreshToken);
        }
        ResponseCookie jwtCookie = jwtService.getCleanCookie();
        ResponseCookie refreshCookie = refreshTokenService.generateRefreshTokenCookie(refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .build();
    }

}
