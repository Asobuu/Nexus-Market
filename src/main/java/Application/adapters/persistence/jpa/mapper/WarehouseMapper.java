package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.SellerJpaEntity;
import Application.adapters.persistence.jpa.entity.WarehouseJpaEntity;
import Application.domain.model.Warehouse;

public final class WarehouseMapper {

    private WarehouseMapper() {
    }

    public static Warehouse toDomain(WarehouseJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Warehouse warehouse = new Warehouse(entity.getName(), AddressMapper.toDomain(entity.getLocation()),
                entity.getOwner() == null ? null : SellerMapper.toDomain(entity.getOwner()));
        warehouse.assignId(entity.getId());
        return warehouse;
    }

    // "ownerEntity" ya lo resolvio el adaptador por id (o viene null si la
    // bodega es del marketplace); aca no se vuelve a mapear el Seller
    // completo para no crear una fila nueva/duplicada
    public static WarehouseJpaEntity toEntity(Warehouse warehouse, SellerJpaEntity ownerEntity) {
        WarehouseJpaEntity entity = new WarehouseJpaEntity(warehouse.getName(),
                AddressMapper.toEmbeddable(warehouse.getLocation()), ownerEntity);
        entity.setId(warehouse.getId());
        return entity;
    }
}
