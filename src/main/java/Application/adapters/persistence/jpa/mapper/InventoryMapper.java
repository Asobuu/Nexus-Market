package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.InventoryJpaEntity;
import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.adapters.persistence.jpa.entity.WarehouseJpaEntity;
import Application.domain.model.Inventory;

public final class InventoryMapper {

    private InventoryMapper() {
    }

    public static Inventory toDomain(InventoryJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Inventory inventory = new Inventory(ProductMapper.toDomain(entity.getProduct()),
                WarehouseMapper.toDomain(entity.getWarehouse()), entity.getQuantity());
        inventory.assignId(entity.getId());
        return inventory;
    }

    public static InventoryJpaEntity toEntity(Inventory inventory, ProductJpaEntity productEntity,
                                               WarehouseJpaEntity warehouseEntity) {
        InventoryJpaEntity entity = new InventoryJpaEntity(productEntity, warehouseEntity, inventory.getQuantity());
        entity.setId(inventory.getId());
        return entity;
    }
}
