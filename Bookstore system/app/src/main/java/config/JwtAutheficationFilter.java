package config;

import exception.AuthenticationException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import service.JwtService;
import service.UserService;
import service.UserServiceInterface;

import java.io.IOException;

@Component
public class JwtAutheficationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserServiceInterface userService;

    public JwtAutheficationFilter(JwtService jwtService, UserServiceInterface userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String jwt = jwtService.getJwtFromCookie(request);
        final String email;

        if ((jwt == null && (authHeader == null || !authHeader.startsWith("Bearer ")))
                || (request.getRequestURI().contains("/register")
                || request.getRequestURI().contains("/login")
                || request.getRequestURI().contains("/refresh-token")
        )) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwt == null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        try {
            email = jwtService.extractUsername(jwt);

            if(StringUtils.isNotEmpty(email)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(email);

                if (jwtService.isValidToken(jwt, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);
                }
            }
        } catch (Exception e) {
            throw new AuthenticationException("Invalid or expired token");
        }
        filterChain.doFilter(request, response);
    }
}
