package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.SellerJpaEntity;
import Application.adapters.persistence.jpa.mapper.SellerMapper;
import Application.adapters.persistence.jpa.repository.SellerJpaRepository;
import Application.domain.model.user.Seller;
import Application.domain.ports.out.SellerRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SellerRepositoryAdapter implements SellerRepositoryPort {

    private final SellerJpaRepository sellerJpaRepository;

    public SellerRepositoryAdapter(SellerJpaRepository sellerJpaRepository) {
        this.sellerJpaRepository = sellerJpaRepository;
    }

    @Override
    public Seller save(Seller seller) {
        SellerJpaEntity entity = sellerJpaRepository.save(SellerMapper.toEntity(seller));
        return SellerMapper.toDomain(entity);
    }

    @Override
    public Optional<Seller> findById(Long id) {
        return sellerJpaRepository.findById(id).map(SellerMapper::toDomain);
    }
}
