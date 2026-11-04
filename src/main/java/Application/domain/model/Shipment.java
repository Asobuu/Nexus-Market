package Application.domain.model;

import Application.domain.enums.ShipmentStatus;
import Application.domain.exceptions.InvalidShipmentStateException;
import Application.domain.model.user.LogisticsOperator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// el envio fisico de (parte de) un pedido. un pedido puede tener varios
// Shipment, uno por cada vendedor cuyos productos lo conforman, porque
// cada vendedor despacha desde su propia bodega. por eso este Shipment
// no carga el pedido completo, solo el subconjunto de items que le
// corresponden a ese vendedor.
// avanza siempre PREPARING -> SHIPPED -> DELIVERED, sin saltarse pasos
@Getter
@NoArgsConstructor
public class Shipment {

    private Long id;
    private Order order;
    private Warehouse originWarehouse;
    private List<OrderItem> items = new ArrayList<>();
    private LogisticsOperator operator;
    private ShipmentStatus status;

    public Shipment(Order order, Warehouse originWarehouse, List<OrderItem> items) {
        this.order = order;
        this.originWarehouse = originWarehouse;
        this.items = new ArrayList<>(items);
        this.status = ShipmentStatus.PREPARING;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    // solo se puede tomar/reasignar mientras sigue en preparacion
    public void assignOperator(LogisticsOperator operator) {
        requireStatus(ShipmentStatus.PREPARING);
        this.operator = operator;
    }

    public void ship() {
        requireStatus(ShipmentStatus.PREPARING);
        status = ShipmentStatus.SHIPPED;
    }

    public void deliver() {
        requireStatus(ShipmentStatus.SHIPPED);
        status = ShipmentStatus.DELIVERED;
    }

    // la usa el mapper de persistencia para reconstruir un envio ya
    // guardado, saltandose las validaciones de transicion (ver Order.assignStatus)
    public void assignStatus(ShipmentStatus status) {
        this.status = status;
    }

    private void requireStatus(ShipmentStatus expected) {
        if (status != expected) {
            throw new InvalidShipmentStateException(
                    "el envio " + id + " esta en estado " + status + ", se esperaba " + expected);
        }
    }
}
