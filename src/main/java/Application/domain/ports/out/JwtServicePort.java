package Application.domain.ports.out;

import Application.domain.model.user.User;

// generar y validar los tokens JWT que usan los usuarios para autenticarse
public interface JwtServicePort {
    String generateToken(User user);
    boolean isValid(String token);
    Long getUserIdFromToken(String token);
}
