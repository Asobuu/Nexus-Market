package Application.model;

import Application.enums.MovementType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// deja un registro de cada cambio que tuvo un inventario (ingreso,
// reserva, salida, ajuste, devolucion), para poder ver el historial
// despues y no solo la cantidad que quedo
@Entity
@Getter
@Setter
@NoArgsConstructor
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Enumerated(EnumType.STRING)
    private MovementType type;

    private int quantity;

    private LocalDateTime occurredAt;

    public InventoryMovement(Inventory inventory, MovementType type, int quantity) {
        this.inventory = inventory;
        this.type = type;
        this.quantity = quantity;
        this.occurredAt = LocalDateTime.now();
    }
}
