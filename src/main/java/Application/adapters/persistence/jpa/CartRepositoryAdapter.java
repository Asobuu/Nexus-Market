package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.BuyerJpaEntity;
import Application.adapters.persistence.jpa.entity.CartJpaEntity;
import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.adapters.persistence.jpa.mapper.CartMapper;
import Application.adapters.persistence.jpa.repository.BuyerJpaRepository;
import Application.adapters.persistence.jpa.repository.CartJpaRepository;
import Application.adapters.persistence.jpa.repository.ProductJpaRepository;
import Application.domain.model.Cart;
import Application.domain.model.CartItem;
import Application.domain.ports.out.CartRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CartRepositoryAdapter implements CartRepositoryPort {

    private final CartJpaRepository cartJpaRepository;
    private final BuyerJpaRepository buyerJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    public CartRepositoryAdapter(CartJpaRepository cartJpaRepository, BuyerJpaRepository buyerJpaRepository,
                                  ProductJpaRepository productJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
        this.buyerJpaRepository = buyerJpaRepository;
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Cart save(Cart cart) {
        BuyerJpaEntity buyerEntity = buyerJpaRepository.getReferenceById(cart.getBuyer().getId());

        Map<Long, ProductJpaEntity> productEntitiesByProductId = new HashMap<>();
        for (CartItem item : cart.getItems()) {
            productEntitiesByProductId.computeIfAbsent(item.getProduct().getId(), productJpaRepository::getReferenceById);
        }

        CartJpaEntity entity = cartJpaRepository.save(
                CartMapper.toEntity(cart, buyerEntity, productEntitiesByProductId));
        return CartMapper.toDomain(entity);
    }

    @Override
    public Optional<Cart> findByBuyerId(Long buyerId) {
        return cartJpaRepository.findByBuyerId(buyerId).map(CartMapper::toDomain);
    }
}
