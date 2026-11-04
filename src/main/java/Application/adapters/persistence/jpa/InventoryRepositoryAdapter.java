package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.InventoryJpaEntity;
import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.adapters.persistence.jpa.entity.WarehouseJpaEntity;
import Application.adapters.persistence.jpa.mapper.InventoryMapper;
import Application.adapters.persistence.jpa.repository.InventoryJpaRepository;
import Application.adapters.persistence.jpa.repository.ProductJpaRepository;
import Application.adapters.persistence.jpa.repository.WarehouseJpaRepository;
import Application.domain.model.Inventory;
import Application.domain.ports.out.InventoryRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InventoryRepositoryAdapter implements InventoryRepositoryPort {

    private final InventoryJpaRepository inventoryJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final WarehouseJpaRepository warehouseJpaRepository;

    public InventoryRepositoryAdapter(InventoryJpaRepository inventoryJpaRepository,
                                       ProductJpaRepository productJpaRepository,
                                       WarehouseJpaRepository warehouseJpaRepository) {
        this.inventoryJpaRepository = inventoryJpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.warehouseJpaRepository = warehouseJpaRepository;
    }

    @Override
    public Inventory save(Inventory inventory) {
        ProductJpaEntity productEntity = productJpaRepository.getReferenceById(inventory.getProduct().getId());
        WarehouseJpaEntity warehouseEntity =
                warehouseJpaRepository.getReferenceById(inventory.getWarehouse().getId());
        InventoryJpaEntity entity =
                inventoryJpaRepository.save(InventoryMapper.toEntity(inventory, productEntity, warehouseEntity));
        return InventoryMapper.toDomain(entity);
    }

    @Override
    public Optional<Inventory> findById(Long id) {
        return inventoryJpaRepository.findById(id).map(InventoryMapper::toDomain);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryJpaRepository.findAll().stream().map(InventoryMapper::toDomain).toList();
    }

    @Override
    public List<Inventory> findByProductId(Long productId) {
        return inventoryJpaRepository.findByProductId(productId).stream().map(InventoryMapper::toDomain).toList();
    }
}
