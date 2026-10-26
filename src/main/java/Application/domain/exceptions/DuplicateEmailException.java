package Application.domain.exceptions;

// ya existe un usuario registrado con ese correo
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("ya existe un usuario registrado con el correo " + email);
    }
}
