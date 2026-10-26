package Application.domain.model;

import Application.domain.enums.MovementType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// deja un registro de cada cambio que tuvo un inventario (ingreso,
// reserva, salida, ajuste, devolucion). es un registro inmutable, se
// crea una vez y no se modifica despues
@Getter
@NoArgsConstructor
public class InventoryMovement {

    private Long id;
    private Inventory inventory;
    private MovementType type;
    private int quantity;
    private LocalDateTime occurredAt;

    public InventoryMovement(Inventory inventory, MovementType type, int quantity) {
        this.inventory = inventory;
        this.type = type;
        this.quantity = quantity;
        this.occurredAt = LocalDateTime.now();
    }

    public void assignId(Long id) {
        this.id = id;
    }
}
