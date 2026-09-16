package Application.model;

import Application.enums.ShipmentStatus;
import Application.model.user.LogisticsOperator;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// el envio fisico de un pedido. solo aplica si el pedido tiene
// productos fisicos (los digitales no generan envio)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse originWarehouse;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private LogisticsOperator operator;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    public Shipment(Order order, Warehouse originWarehouse) {
        this.order = order;
        this.originWarehouse = originWarehouse;
        this.status = ShipmentStatus.PREPARING;
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
