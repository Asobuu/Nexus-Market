package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.OrderJpaEntity;
import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.adapters.persistence.jpa.entity.ReturnJpaEntity;
import Application.adapters.persistence.jpa.mapper.ReturnMapper;
import Application.adapters.persistence.jpa.repository.OrderJpaRepository;
import Application.adapters.persistence.jpa.repository.ProductJpaRepository;
import Application.adapters.persistence.jpa.repository.ReturnJpaRepository;
import Application.domain.model.Return;
import Application.domain.ports.out.ReturnRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReturnRepositoryAdapter implements ReturnRepositoryPort {

    private final ReturnJpaRepository returnJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    public ReturnRepositoryAdapter(ReturnJpaRepository returnJpaRepository, OrderJpaRepository orderJpaRepository,
                                    ProductJpaRepository productJpaRepository) {
        this.returnJpaRepository = returnJpaRepository;
        this.orderJpaRepository = orderJpaRepository;
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Return save(Return returnRequest) {
        OrderJpaEntity orderEntity = orderJpaRepository.getReferenceById(returnRequest.getOrder().getId());
        ProductJpaEntity productEntity = productJpaRepository.getReferenceById(returnRequest.getProduct().getId());
        ReturnJpaEntity entity =
                returnJpaRepository.save(ReturnMapper.toEntity(returnRequest, orderEntity, productEntity));
        return ReturnMapper.toDomain(entity);
    }

    @Override
    public Optional<Return> findById(Long id) {
        return returnJpaRepository.findById(id).map(ReturnMapper::toDomain);
    }
}
