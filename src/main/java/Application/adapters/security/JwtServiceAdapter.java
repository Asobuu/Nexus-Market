package Application.adapters.security;

import Application.domain.model.user.User;
import Application.domain.ports.out.JwtServicePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// genera y valida los JWT. claims segun el SDD (Rest-security-cors.md):
// sub = id del usuario, role, iat, exp. expiracion por defecto 1 hora
// (jwt.expiration-ms, configurable en application.properties)
@Component
public class JwtServiceAdapter implements JwtServicePort {

    private final SecretKey key;
    private final long expirationMs;

    public JwtServiceAdapter(@Value("${jwt.secret}") String secret,
                              @Value("${jwt.expiration-ms:3600000}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    @Override
    public String generateToken(User user) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("role", UserRoles.of(user))
                .issuedAt(now)
                .expiration(expiresAt)
                .signWith(key)
                .compact();
    }

    @Override
    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
