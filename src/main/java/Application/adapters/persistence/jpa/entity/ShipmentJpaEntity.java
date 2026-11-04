package Application.adapters.persistence.jpa.entity;

import Application.domain.enums.ShipmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
public class ShipmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderJpaEntity order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_warehouse_id", nullable = false)
    private WarehouseJpaEntity originWarehouse;

    @OneToMany(mappedBy = "shipment", fetch = FetchType.LAZY)
    private List<OrderItemJpaEntity> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operator_id")
    private LogisticsOperatorJpaEntity operator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status;

    public ShipmentJpaEntity(OrderJpaEntity order, WarehouseJpaEntity originWarehouse, ShipmentStatus status) {
        this.order = order;
        this.originWarehouse = originWarehouse;
        this.status = status;
    }
}
