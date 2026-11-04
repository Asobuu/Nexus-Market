package Application.adapters.rest.dto.response;

import Application.domain.model.AuthToken;
import lombok.Getter;

@Getter
public class AuthTokenResponse {

    private final String token;
    private final Long userId;
    private final String role;

    private AuthTokenResponse(String token, Long userId, String role) {
        this.token = token;
        this.userId = userId;
        this.role = role;
    }

    public static AuthTokenResponse from(AuthToken authToken) {
        return new AuthTokenResponse(authToken.getToken(), authToken.getUserId(), authToken.getRole());
    }
}
