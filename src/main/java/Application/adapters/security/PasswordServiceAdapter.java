package Application.adapters.security;

import Application.domain.ports.out.PasswordServicePort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

// bcrypt para el hash de contraseñas. no se expone como un bean
// PasswordEncoder generico porque este proyecto no usa el
// UserDetailsService/login-form de Spring Security (la autenticacion es
// toda via JWT, ver JwtAuthenticationFilter), asi que no hace falta
// compartirlo con nada mas
@Component
public class PasswordServiceAdapter implements PasswordServicePort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String hash) {
        return encoder.matches(rawPassword, hash);
    }
}
