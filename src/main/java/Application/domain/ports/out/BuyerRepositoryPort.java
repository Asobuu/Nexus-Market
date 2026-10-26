package Application.domain.ports.out;

import Application.domain.model.user.Buyer;

import java.util.Optional;

public interface BuyerRepositoryPort {
    Buyer save(Buyer buyer);
    Optional<Buyer> findById(Long id);
}
