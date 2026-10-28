package Application.domain.services;

import Application.domain.enums.MovementType;
import Application.domain.model.Inventory;
import Application.domain.model.InventoryMovement;
import Application.domain.ports.out.InventoryMovementRepositoryPort;
import Application.domain.ports.out.InventoryRepositoryPort;

// todo cambio en la cantidad de un inventario pasa por aca, para poder
// dejar registrado que tipo de movimiento fue (no solo el numero final)
public class InventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryMovementRepositoryPort movementRepositoryPort;

    public InventoryService(InventoryRepositoryPort inventoryRepositoryPort,
                             InventoryMovementRepositoryPort movementRepositoryPort) {
        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.movementRepositoryPort = movementRepositoryPort;
    }

    public Inventory registerReceipt(Inventory inventory, int amount) {
        inventory.addStock(amount);
        inventoryRepositoryPort.save(inventory);
        registerMovement(inventory, MovementType.RECEIPT, amount);
        return inventory;
    }

    public Inventory reserveStock(Inventory inventory, int amount) {
        inventory.reserve(amount);
        inventoryRepositoryPort.save(inventory);
        registerMovement(inventory, MovementType.RESERVATION, amount);
        return inventory;
    }

    public Inventory registerSale(Inventory inventory, int amount) {
        registerMovement(inventory, MovementType.SALE, amount);
        return inventory;
    }

    public Inventory adjustStock(Inventory inventory, int amount) {
        if (amount >= 0) {
            inventory.addStock(amount);
        } else {
            inventory.reserve(-amount);
        }
        inventoryRepositoryPort.save(inventory);
        registerMovement(inventory, MovementType.ADJUSTMENT, amount);
        return inventory;
    }

    public Inventory registerReturn(Inventory inventory, int amount) {
        inventory.addStock(amount);
        inventoryRepositoryPort.save(inventory);
        registerMovement(inventory, MovementType.RETURN, amount);
        return inventory;
    }

    private void registerMovement(Inventory inventory, MovementType type, int amount) {
        movementRepositoryPort.save(new InventoryMovement(inventory, type, amount));
    }
}
