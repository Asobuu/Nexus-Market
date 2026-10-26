package Application.domain.ports.out;

import Application.domain.model.Cart;

import java.util.Optional;

public interface CartRepositoryPort {
    Cart save(Cart cart);
    Optional<Cart> findByBuyerId(Long buyerId);
}
