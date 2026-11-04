package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.InventoryJpaEntity;
import Application.adapters.persistence.jpa.entity.InventoryMovementJpaEntity;
import Application.domain.model.InventoryMovement;

public final class InventoryMovementMapper {

    private InventoryMovementMapper() {
    }

    public static InventoryMovement toDomain(InventoryMovementJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        InventoryMovement movement = new InventoryMovement(InventoryMapper.toDomain(entity.getInventory()),
                entity.getType(), entity.getQuantity());
        movement.assignId(entity.getId());
        movement.assignOccurredAt(entity.getOccurredAt());
        return movement;
    }

    public static InventoryMovementJpaEntity toEntity(InventoryMovement movement, InventoryJpaEntity inventoryEntity) {
        InventoryMovementJpaEntity entity = new InventoryMovementJpaEntity(inventoryEntity, movement.getType(),
                movement.getQuantity(), movement.getOccurredAt());
        entity.setId(movement.getId());
        return entity;
    }
}
