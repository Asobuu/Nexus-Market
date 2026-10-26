package Application.domain.model;

import lombok.Getter;

// lo que devuelve un login exitoso: el token JWT y algunos datos basicos
// del usuario, para que el cliente sepa con quien esta hablando
@Getter
public class AuthToken {

    private final String token;
    private final Long userId;
    private final String role;

    public AuthToken(String token, Long userId, String role) {
        this.token = token;
        this.userId = userId;
        this.role = role;
    }
}
