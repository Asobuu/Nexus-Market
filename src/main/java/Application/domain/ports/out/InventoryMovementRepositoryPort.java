package Application.domain.ports.out;

import Application.domain.model.InventoryMovement;

public interface InventoryMovementRepositoryPort {
    InventoryMovement save(InventoryMovement movement);
}
