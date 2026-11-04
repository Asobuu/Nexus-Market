package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.SellerJpaEntity;
import Application.adapters.persistence.jpa.entity.WarehouseJpaEntity;
import Application.adapters.persistence.jpa.mapper.WarehouseMapper;
import Application.adapters.persistence.jpa.repository.SellerJpaRepository;
import Application.adapters.persistence.jpa.repository.WarehouseJpaRepository;
import Application.domain.model.Warehouse;
import Application.domain.ports.out.WarehouseRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WarehouseRepositoryAdapter implements WarehouseRepositoryPort {

    private final WarehouseJpaRepository warehouseJpaRepository;
    private final SellerJpaRepository sellerJpaRepository;

    public WarehouseRepositoryAdapter(WarehouseJpaRepository warehouseJpaRepository,
                                       SellerJpaRepository sellerJpaRepository) {
        this.warehouseJpaRepository = warehouseJpaRepository;
        this.sellerJpaRepository = sellerJpaRepository;
    }

    @Override
    public Warehouse save(Warehouse warehouse) {
        SellerJpaEntity ownerEntity = warehouse.getOwner() == null
                ? null
                : sellerJpaRepository.getReferenceById(warehouse.getOwner().getId());
        WarehouseJpaEntity entity = warehouseJpaRepository.save(WarehouseMapper.toEntity(warehouse, ownerEntity));
        return WarehouseMapper.toDomain(entity);
    }

    @Override
    public Optional<Warehouse> findById(Long id) {
        return warehouseJpaRepository.findById(id).map(WarehouseMapper::toDomain);
    }
}
