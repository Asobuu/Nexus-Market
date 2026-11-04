package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.SellerJpaEntity;
import Application.domain.enums.UserStatus;
import Application.domain.model.user.Seller;

public final class SellerMapper {

    private SellerMapper() {
    }

    public static Seller toDomain(SellerJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Seller seller = new Seller(entity.getFullName(), entity.getEmail());
        seller.assignId(entity.getId());
        seller.assignPasswordHash(entity.getPasswordHash());
        if (entity.getStatus() == UserStatus.BLOCKED) {
            seller.block();
        }
        // seller.getWarehouses()/getProducts() quedan vacios a proposito
        // (ver SellerJpaEntity): quien los necesita consulta
        // WarehouseJpaRepository/ProductJpaRepository por sellerId
        return seller;
    }

    public static SellerJpaEntity toEntity(Seller seller) {
        SellerJpaEntity entity = new SellerJpaEntity(seller.getFullName(), seller.getEmail());
        entity.setId(seller.getId());
        entity.setStatus(seller.getStatus());
        entity.setPasswordHash(seller.getPasswordHash());
        return entity;
    }
}
