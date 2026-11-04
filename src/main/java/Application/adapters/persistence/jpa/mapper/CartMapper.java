package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.BuyerJpaEntity;
import Application.adapters.persistence.jpa.entity.CartItemJpaEntity;
import Application.adapters.persistence.jpa.entity.CartJpaEntity;
import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.domain.model.Cart;
import Application.domain.model.CartItem;

import java.util.Map;

public final class CartMapper {

    private CartMapper() {
    }

    public static Cart toDomain(CartJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Cart cart = new Cart(BuyerMapper.toDomain(entity.getBuyer()));
        cart.assignId(entity.getId());
        for (CartItemJpaEntity itemEntity : entity.getItems()) {
            CartItem item = new CartItem(ProductMapper.toDomain(itemEntity.getProduct()), itemEntity.getQuantity());
            item.assignId(itemEntity.getId());
            cart.addItem(item);
        }
        return cart;
    }

    // "buyerEntity" y los productos de "productEntitiesByProductId" ya los
    // resolvio el adaptador por id (ver CartRepositoryAdapter); aca solo se
    // arma el arbol de entidades con esas referencias
    public static CartJpaEntity toEntity(Cart cart, BuyerJpaEntity buyerEntity,
                                          Map<Long, ProductJpaEntity> productEntitiesByProductId) {
        CartJpaEntity entity = new CartJpaEntity(buyerEntity);
        entity.setId(cart.getId());
        for (CartItem item : cart.getItems()) {
            ProductJpaEntity productEntity = productEntitiesByProductId.get(item.getProduct().getId());
            CartItemJpaEntity itemEntity = new CartItemJpaEntity(entity, productEntity, item.getQuantity());
            itemEntity.setId(item.getId());
            entity.getItems().add(itemEntity);
        }
        return entity;
    }
}
