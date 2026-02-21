package it.univr.glicemiewebapp.config;

import it.univr.glicemiewebapp.service.JwtService;
import it.univr.glicemiewebapp.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * WebSocket handshake interceptor that extracts and validates JWT tokens from
 * cookies
 * and stores authentication information in WebSocket session attributes.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;
    private final MyUserDetailsService userDetailsService;

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            String jwt = extractJwtFromCookies(httpRequest);

            if (jwt != null && jwtService.checkValidity(jwt)) {
                try {
                    String email = jwtService.getMailFromToken(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // Store authentication in WebSocket session attributes
                    attributes.put("authentication", authentication);
                    attributes.put("user", userDetails);

                    log.debug("WebSocket authentication successful for user: {}", email);
                    return true;
                } catch (Exception e) {
                    log.warn("Failed to authenticate WebSocket connection: {}", e.getMessage());
                }
            } else {
                log.warn("Invalid or missing JWT token in WebSocket handshake");
            }
        }

        // Reject handshake if authentication fails
        return false;
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @Nullable Exception exception) {
        // No action needed after handshake
    }

    /**
     * Extracts JWT token from the "token" cookie.
     */
    private String extractJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
