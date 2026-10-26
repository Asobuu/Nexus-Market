package Application.domain.exceptions;

// se pidio una devolucion sobre un pedido que todavia no esta entregado
public class ReturnNotEligibleException extends RuntimeException {

    public ReturnNotEligibleException(String message) {
        super(message);
    }
}
