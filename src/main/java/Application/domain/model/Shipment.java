package Application.domain.model;

import Application.domain.enums.ShipmentStatus;
import Application.domain.model.user.LogisticsOperator;
import lombok.Getter;
import lombok.NoArgsConstructor;

// el envio fisico de un pedido. solo aplica si el pedido tiene
// productos fisicos (los digitales no generan envio)
@Getter
@NoArgsConstructor
public class Shipment {

    private Long id;
    private Order order;
    private Warehouse originWarehouse;
    private LogisticsOperator operator;
    private ShipmentStatus status;

    public Shipment(Order order, Warehouse originWarehouse) {
        this.order = order;
        this.originWarehouse = originWarehouse;
        this.status = ShipmentStatus.PREPARING;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void assignOperator(LogisticsOperator operator) {
        this.operator = operator;
    }

    public void ship() {
        status = ShipmentStatus.SHIPPED;
    }

    public void deliver() {
        status = ShipmentStatus.DELIVERED;
    }
}
