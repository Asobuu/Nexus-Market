package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.AddressEmbeddable;
import Application.adapters.persistence.jpa.entity.BuyerJpaEntity;
import Application.domain.enums.UserStatus;
import Application.domain.model.user.Address;
import Application.domain.model.user.Buyer;

public final class BuyerMapper {

    private BuyerMapper() {
    }

    public static Buyer toDomain(BuyerJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Buyer buyer = new Buyer(entity.getFullName(), entity.getEmail(), AddressMapper.toDomain(entity.getAddress()));
        buyer.assignId(entity.getId());
        buyer.assignPasswordHash(entity.getPasswordHash());
        if (entity.getStatus() == UserStatus.BLOCKED) {
            buyer.block();
        }
        for (AddressEmbeddable other : entity.getOtherAddresses()) {
            buyer.addOtherAddress(AddressMapper.toDomain(other));
        }
        return buyer;
    }

    public static BuyerJpaEntity toEntity(Buyer buyer) {
        BuyerJpaEntity entity = new BuyerJpaEntity(buyer.getFullName(), buyer.getEmail(),
                AddressMapper.toEmbeddable(buyer.getAddress()));
        entity.setId(buyer.getId());
        entity.setStatus(buyer.getStatus());
        entity.setPasswordHash(buyer.getPasswordHash());
        for (Address other : buyer.getOtherAddresses()) {
            entity.getOtherAddresses().add(AddressMapper.toEmbeddable(other));
        }
        return entity;
    }
}
