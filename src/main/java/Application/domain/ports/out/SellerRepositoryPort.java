package Application.domain.ports.out;

import Application.domain.model.user.Seller;

import java.util.Optional;

public interface SellerRepositoryPort {
    Seller save(Seller seller);
    Optional<Seller> findById(Long id);
}
