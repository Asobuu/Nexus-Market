package Application.adapters.persistence.jpa;

import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.adapters.persistence.jpa.entity.SellerJpaEntity;
import Application.adapters.persistence.jpa.mapper.ProductMapper;
import Application.adapters.persistence.jpa.repository.ProductJpaRepository;
import Application.adapters.persistence.jpa.repository.SellerJpaRepository;
import Application.domain.model.Product;
import Application.domain.ports.out.ProductRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final SellerJpaRepository sellerJpaRepository;

    public ProductRepositoryAdapter(ProductJpaRepository productJpaRepository,
                                     SellerJpaRepository sellerJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.sellerJpaRepository = sellerJpaRepository;
    }

    @Override
    public Product save(Product product) {
        SellerJpaEntity sellerEntity = sellerJpaRepository.getReferenceById(product.getSeller().getId());
        ProductJpaEntity entity = productJpaRepository.save(ProductMapper.toEntity(product, sellerEntity));
        return ProductMapper.toDomain(entity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id).map(ProductMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll().stream().map(ProductMapper::toDomain).toList();
    }

    @Override
    public List<Product> findBySellerId(Long sellerId) {
        return productJpaRepository.findBySellerId(sellerId).stream().map(ProductMapper::toDomain).toList();
    }
}
