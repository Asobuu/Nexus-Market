package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.OrderItemJpaEntity;
import Application.domain.model.OrderItem;
import Application.domain.model.Warehouse;

public final class OrderItemMapper {

    private OrderItemMapper() {
    }

    public static OrderItem toDomain(OrderItemJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Warehouse warehouse = entity.getWarehouse() == null ? null : WarehouseMapper.toDomain(entity.getWarehouse());
        OrderItem item = new OrderItem(ProductMapper.toDomain(entity.getProduct()), entity.getQuantity(), warehouse);
        item.assignId(entity.getId());
        // el constructor recalcula el precio desde el producto actual; se
        // restaura el precio real con el que se compro (ver OrderItem.assignUnitPrice)
        item.assignUnitPrice(entity.getUnitPrice());
        return item;
    }
}
