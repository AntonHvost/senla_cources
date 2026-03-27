package config;

import exception.AuthenticationException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import service.JwtService;
import service.RefreshTokenService;

import java.io.IOException;

@Component
public class LogoutFilter implements Filter {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private static final String LOGOUT_PATH = "/api/auth/logout";

    public LogoutFilter(JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if(!LOGOUT_PATH.equals(req.getServletPath()) || !"POST".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !(auth.getPrincipal() instanceof UserDetails userDetails)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED,"User is not authenticated");
            return;
        }

        String token = extractToken(req);
        if(token == null) {
            throw new AuthenticationException("Token not provided");
        }

        if (!jwtService.isValidToken(token, userDetails)) {
            throw new AuthenticationException("Invalid token");
        }

        String refreshToken = refreshTokenService.getRefreshTokenFromCookie(req);
        if(refreshToken != null && !refreshToken.isEmpty()) {
            refreshTokenService.deleteByToken(refreshToken);
        }

        ResponseCookie cookie = jwtService.getCleanCookie();
        ResponseCookie refreshCookie = refreshTokenService.getCleanRefreshTokenCookie();

        res.addHeader("Set-Cookie", cookie.toString());
        res.addHeader("Set-Cookie", refreshCookie.toString());
        res.setStatus(HttpServletResponse.SC_OK);
    }

    private String extractToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return jwtService.getJwtFromCookie(req);
    }
}
