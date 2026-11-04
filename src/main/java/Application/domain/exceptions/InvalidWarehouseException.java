package Application.domain.exceptions;

// la bodega de origen de un envio no le pertenece al vendedor de los
// productos que se estan despachando
public class InvalidWarehouseException extends RuntimeException {

    public InvalidWarehouseException(String message) {
        super(message);
    }
}
