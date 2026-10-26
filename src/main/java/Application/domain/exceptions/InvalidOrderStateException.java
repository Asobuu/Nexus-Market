package Application.domain.exceptions;

// se intento una transicion de estado que el pedido no permite
// (por ejemplo modificar un pedido que ya quedo entregado)
public class InvalidOrderStateException extends RuntimeException {

    public InvalidOrderStateException(String message) {
        super(message);
    }
}
