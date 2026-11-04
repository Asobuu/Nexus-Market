package Application.domain.exceptions;

// las credenciales son correctas pero la cuenta no esta activa (bloqueada/suspendida)
public class AccountBlockedException extends RuntimeException {

    public AccountBlockedException(String message) {
        super(message);
    }
}
