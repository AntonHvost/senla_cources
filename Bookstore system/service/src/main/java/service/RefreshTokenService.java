package service;

import domain.model.impl.RefreshToken;
import domain.model.impl.User;
import dto.request.RefreshTokenRequestDto;
import dto.response.RefreshTokenResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;
import repository.RefreshTokenRepository;
import repository.UserRepository;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${jwt.refresh-token.cookie-name}")
    private String refreshTokenName;

    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRevoked(false);
        refreshToken.setToken(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));

        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token == null) {
            throw new RuntimeException("Invalid RefreshToken");
        }

        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Invalid RefreshToken");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshTokenResponseDto generateNewToken(RefreshTokenRequestDto request) {
        System.out.println("Generating New Token:" + request.getToken());
        User user = refreshTokenRepository.findByToken(request.getToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new RuntimeException("Invalid RefreshToken"));

        System.out.println("User info:" + user.getUsername() + user.getAuthorities());
        String token = jwtService.generateToken(user);
        return new RefreshTokenResponseDto(token, request.getToken(), null);
    }

    public ResponseCookie generateRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(refreshTokenName, refreshToken)
                .path("/")
                .maxAge(refreshExpiration/1000)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenName);
        if (cookie != null) {
            return cookie.getValue();
        } else  {
            return "";
        }
    }

    public void deleteByToken(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).ifPresent(token -> refreshTokenRepository.delete(token));
    }

    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName,"")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }
}
