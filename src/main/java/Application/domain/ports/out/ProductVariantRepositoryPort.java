package Application.domain.ports.out;

import Application.domain.model.ProductVariant;

public interface ProductVariantRepositoryPort {
    ProductVariant save(ProductVariant variant);
}
