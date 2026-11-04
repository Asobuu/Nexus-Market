package Application.adapters.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

// agrega X-Request-Id a cada request/response para poder rastrear una
// peticion especifica en los logs (ver Rest-security-cors.md). si el
// cliente ya mando uno, se reusa; si no, se genera uno nuevo
@Component
@Order(1)
public class RequestIdFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-Request-Id";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestId = request.getHeader(HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }
        response.setHeader(HEADER, requestId);
        filterChain.doFilter(request, response);
    }
}
