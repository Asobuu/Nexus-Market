package Application.domain.ports.out;

// convierte una contraseña en texto plano a un hash seguro, y despues
// permite verificar un intento de login contra ese hash
public interface PasswordServicePort {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String hash);
}
