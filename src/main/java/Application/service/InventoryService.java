package Application.service;

import Application.enums.MovementType;
import Application.model.Inventory;
import Application.model.InventoryMovement;
import Application.repository.InventoryMovementRepository;
import Application.repository.InventoryRepository;
import org.springframework.stereotype.Service;

// todo cambio en la cantidad de un inventario pasa por aca, para poder
// dejar registrado que tipo de movimiento fue (no solo el numero final)
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository movementRepository;

    public InventoryService(InventoryRepository inventoryRepository, InventoryMovementRepository movementRepository) {
        this.inventoryRepository = inventoryRepository;
        this.movementRepository = movementRepository;
    }

    // entra mercancia nueva a la bodega
    public Inventory registerReceipt(Inventory inventory, int amount) {
        inventory.addStock(amount);
        inventoryRepository.save(inventory);
        registerMovement(inventory, MovementType.RECEIPT, amount);
        return inventory;
    }

    // se aparta stock para un pedido que todavia no se ha despachado
    public Inventory reserveStock(Inventory inventory, int amount) {
        inventory.reserve(amount);
        inventoryRepository.save(inventory);
        registerMovement(inventory, MovementType.RESERVATION, amount);
        return inventory;
    }

    // el pedido ya se despacho, ese stock reservado sale de verdad
    public Inventory registerSale(Inventory inventory, int amount) {
        registerMovement(inventory, MovementType.SALE, amount);
        return inventory;
    }

    // correccion manual del inventario (por ejemplo un conteo fisico)
    public Inventory adjustStock(Inventory inventory, int amount) {
        if (amount >= 0) {
            inventory.addStock(amount);
        } else {
            inventory.reserve(-amount);
        }
        inventoryRepository.save(inventory);
        registerMovement(inventory, MovementType.ADJUSTMENT, amount);
        return inventory;
    }

    // el producto vuelve a la bodega por una devolucion
    public Inventory registerReturn(Inventory inventory, int amount) {
        inventory.addStock(amount);
        inventoryRepository.save(inventory);
        registerMovement(inventory, MovementType.RETURN, amount);
        return inventory;
    }

    private void registerMovement(Inventory inventory, MovementType type, int amount) {
        movementRepository.save(new InventoryMovement(inventory, type, amount));
    }
}
