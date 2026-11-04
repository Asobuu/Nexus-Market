package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.adapters.persistence.jpa.entity.ProductVariantJpaEntity;
import Application.adapters.persistence.jpa.mapper.ProductVariantMapper;
import Application.adapters.persistence.jpa.repository.ProductJpaRepository;
import Application.adapters.persistence.jpa.repository.ProductVariantJpaRepository;
import Application.domain.model.ProductVariant;
import Application.domain.ports.out.ProductVariantRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class ProductVariantRepositoryAdapter implements ProductVariantRepositoryPort {

    private final ProductVariantJpaRepository productVariantJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    public ProductVariantRepositoryAdapter(ProductVariantJpaRepository productVariantJpaRepository,
                                            ProductJpaRepository productJpaRepository) {
        this.productVariantJpaRepository = productVariantJpaRepository;
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public ProductVariant save(ProductVariant variant) {
        ProductJpaEntity productEntity = productJpaRepository.getReferenceById(variant.getProduct().getId());
        ProductVariantJpaEntity entity =
                productVariantJpaRepository.save(ProductVariantMapper.toEntity(variant, productEntity));
        return ProductVariantMapper.toDomain(entity);
    }
}
