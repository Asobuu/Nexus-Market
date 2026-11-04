package Application.domain.exceptions;

// el correo no existe o la contraseña no coincide. un solo mensaje generico
// para no revelar si el correo esta registrado o no
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
