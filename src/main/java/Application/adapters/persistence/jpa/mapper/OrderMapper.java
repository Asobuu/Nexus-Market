package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.BuyerJpaEntity;
import Application.adapters.persistence.jpa.entity.OrderItemJpaEntity;
import Application.adapters.persistence.jpa.entity.OrderJpaEntity;
import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.adapters.persistence.jpa.entity.ShipmentJpaEntity;
import Application.adapters.persistence.jpa.entity.WarehouseJpaEntity;
import Application.domain.model.Order;
import Application.domain.model.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static Order toDomain(OrderJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemJpaEntity itemEntity : entity.getItems()) {
            items.add(OrderItemMapper.toDomain(itemEntity));
        }
        Order order = new Order(BuyerMapper.toDomain(entity.getBuyer()), items,
                AddressMapper.toDomain(entity.getDeliveryAddress()));
        order.assignId(entity.getId());
        // el constructor siempre arranca en PENDING_PAYMENT; se restaura el
        // estado real guardado, sin pasar por las validaciones de transicion
        // (ver Order.assignStatus)
        order.assignStatus(entity.getStatus());
        return order;
    }

    // "buyerEntity" y los mapas de producto/bodega por id ya los resolvio el
    // adaptador (ver OrderRepositoryAdapter); aca solo se arma el arbol de
    // entidades con esas referencias.
    // "existingShipmentByItemId" tambien lo resuelve el adaptador: como el
    // dominio (OrderItem) no sabe a que Shipment quedo asignado un item (eso
    // es plomeria pura de persistencia), si no se restaura aca el FK
    // "shipment_id" que ya tenia guardado, el merge de Hibernate (items es
    // cascade=ALL + orphanRemoval=true) lo pisaria con null cada vez que se
    // vuelve a guardar el Order (por ejemplo al pasar a SHIPPED/DELIVERED en
    // ShipmentService), borrando la asignacion al envio que se acababa de hacer
    public static OrderJpaEntity toEntity(Order order, BuyerJpaEntity buyerEntity,
                                           Map<Long, ProductJpaEntity> productEntitiesByProductId,
                                           Map<Long, WarehouseJpaEntity> warehouseEntitiesByWarehouseId,
                                           Map<Long, ShipmentJpaEntity> existingShipmentByItemId) {
        OrderJpaEntity entity = new OrderJpaEntity(buyerEntity, AddressMapper.toEmbeddable(order.getDeliveryAddress()),
                order.getStatus());
        entity.setId(order.getId());
        for (OrderItem item : order.getItems()) {
            ProductJpaEntity productEntity = productEntitiesByProductId.get(item.getProduct().getId());
            WarehouseJpaEntity warehouseEntity = item.getWarehouse() == null ? null
                    : warehouseEntitiesByWarehouseId.get(item.getWarehouse().getId());
            OrderItemJpaEntity itemEntity = new OrderItemJpaEntity(entity, productEntity, item.getQuantity(),
                    item.getUnitPrice(), warehouseEntity);
            itemEntity.setId(item.getId());
            itemEntity.setShipment(existingShipmentByItemId.get(item.getId()));
            entity.getItems().add(itemEntity);
        }
        return entity;
    }
}
