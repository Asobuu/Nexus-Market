package Application.adapters.rest.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

// formato unico de error para todas las respuestas (ver
// Global-exception-handler.md). "fieldErrors" solo va lleno cuando el
// error viene de validacion de campos (ver Rest-validation.md); en
// cualquier otro error queda null y no aparece en el JSON
@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final Map<String, String> fieldErrors;

    public ErrorResponse(int status, String error, String message) {
        this(status, error, message, null);
    }

    public ErrorResponse(int status, String error, String message, Map<String, String> fieldErrors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }
}
