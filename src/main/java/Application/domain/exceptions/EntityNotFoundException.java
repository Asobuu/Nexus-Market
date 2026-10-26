package Application.domain.exceptions;

// se busco por id algo que no existe
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, Long id) {
        super("no se encontro " + entityName + " con id " + id);
    }
}
