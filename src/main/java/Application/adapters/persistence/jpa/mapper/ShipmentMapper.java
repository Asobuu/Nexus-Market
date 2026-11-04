package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.LogisticsOperatorJpaEntity;
import Application.adapters.persistence.jpa.entity.OrderItemJpaEntity;
import Application.adapters.persistence.jpa.entity.OrderJpaEntity;
import Application.adapters.persistence.jpa.entity.ShipmentJpaEntity;
import Application.adapters.persistence.jpa.entity.WarehouseJpaEntity;
import Application.domain.model.OrderItem;
import Application.domain.model.Shipment;

import java.util.ArrayList;
import java.util.List;

public final class ShipmentMapper {

    private ShipmentMapper() {
    }

    public static Shipment toDomain(ShipmentJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemJpaEntity itemEntity : entity.getItems()) {
            items.add(OrderItemMapper.toDomain(itemEntity));
        }
        Shipment shipment = new Shipment(OrderMapper.toDomain(entity.getOrder()),
                WarehouseMapper.toDomain(entity.getOriginWarehouse()), items);
        shipment.assignId(entity.getId());
        // el operador se asigna ANTES de restaurar el estado real: Shipment
        // recien construido siempre esta en PREPARING, que es el unico
        // estado en que assignOperator() deja asignar (ver Shipment.assignOperator)
        if (entity.getOperator() != null) {
            shipment.assignOperator(LogisticsOperatorMapper.toDomain(entity.getOperator()));
        }
        shipment.assignStatus(entity.getStatus());
        return shipment;
    }

    // el adaptador ya resolvio el pedido, la bodega de origen y el operador
    // por id; los OrderItemJpaEntity de este envio se actualizan por fuera
    // (ver ShipmentRepositoryAdapter), porque ya existen como items de un
    // pedido guardado, no se vuelven a crear aca
    public static ShipmentJpaEntity toEntity(Shipment shipment, OrderJpaEntity orderEntity,
                                              WarehouseJpaEntity originWarehouseEntity,
                                              LogisticsOperatorJpaEntity operatorEntity) {
        ShipmentJpaEntity entity = new ShipmentJpaEntity(orderEntity, originWarehouseEntity, shipment.getStatus());
        entity.setId(shipment.getId());
        entity.setOperator(operatorEntity);
        return entity;
    }
}
