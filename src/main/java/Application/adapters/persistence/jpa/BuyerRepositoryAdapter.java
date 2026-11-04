package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.BuyerJpaEntity;
import Application.adapters.persistence.jpa.mapper.BuyerMapper;
import Application.adapters.persistence.jpa.repository.BuyerJpaRepository;
import Application.domain.model.user.Buyer;
import Application.domain.ports.out.BuyerRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BuyerRepositoryAdapter implements BuyerRepositoryPort {

    private final BuyerJpaRepository buyerJpaRepository;

    public BuyerRepositoryAdapter(BuyerJpaRepository buyerJpaRepository) {
        this.buyerJpaRepository = buyerJpaRepository;
    }

    @Override
    public Buyer save(Buyer buyer) {
        BuyerJpaEntity entity = buyerJpaRepository.save(BuyerMapper.toEntity(buyer));
        return BuyerMapper.toDomain(entity);
    }

    @Override
    public Optional<Buyer> findById(Long id) {
        return buyerJpaRepository.findById(id).map(BuyerMapper::toDomain);
    }
}
