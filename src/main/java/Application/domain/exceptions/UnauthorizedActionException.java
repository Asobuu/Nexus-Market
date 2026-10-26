package Application.domain.exceptions;

// un actor intento hacer algo que no le corresponde a su rol
public class UnauthorizedActionException extends RuntimeException {

    public UnauthorizedActionException(String message) {
        super(message);
    }
}
