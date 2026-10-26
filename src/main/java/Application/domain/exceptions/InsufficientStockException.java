package Application.domain.exceptions;

// no hay suficiente inventario para reservar lo que se pidio
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
