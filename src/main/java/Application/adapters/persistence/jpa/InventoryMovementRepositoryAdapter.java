package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.InventoryJpaEntity;
import Application.adapters.persistence.jpa.entity.InventoryMovementJpaEntity;
import Application.adapters.persistence.jpa.mapper.InventoryMovementMapper;
import Application.adapters.persistence.jpa.repository.InventoryJpaRepository;
import Application.adapters.persistence.jpa.repository.InventoryMovementJpaRepository;
import Application.domain.model.InventoryMovement;
import Application.domain.ports.out.InventoryMovementRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class InventoryMovementRepositoryAdapter implements InventoryMovementRepositoryPort {

    private final InventoryMovementJpaRepository inventoryMovementJpaRepository;
    private final InventoryJpaRepository inventoryJpaRepository;

    public InventoryMovementRepositoryAdapter(InventoryMovementJpaRepository inventoryMovementJpaRepository,
                                               InventoryJpaRepository inventoryJpaRepository) {
        this.inventoryMovementJpaRepository = inventoryMovementJpaRepository;
        this.inventoryJpaRepository = inventoryJpaRepository;
    }

    @Override
    public InventoryMovement save(InventoryMovement movement) {
        InventoryJpaEntity inventoryEntity =
                inventoryJpaRepository.getReferenceById(movement.getInventory().getId());
        InventoryMovementJpaEntity entity = inventoryMovementJpaRepository.save(
                InventoryMovementMapper.toEntity(movement, inventoryEntity));
        return InventoryMovementMapper.toDomain(entity);
    }
}
