package Application.domain.exceptions;

// se intento mover un envio a un estado al que no puede pasar desde el
// estado en que esta ahora (por ejemplo, entregarlo sin haberlo despachado)
public class InvalidShipmentStateException extends RuntimeException {

    public InvalidShipmentStateException(String message) {
        super(message);
    }
}
