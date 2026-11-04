package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.adapters.persistence.jpa.entity.ProductVariantJpaEntity;
import Application.adapters.persistence.jpa.entity.SellerJpaEntity;
import Application.domain.enums.ProductStatus;
import Application.domain.model.Product;
import Application.domain.model.ProductVariant;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toDomain(ProductJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        Product product = new Product(entity.getName(), entity.getDescription(), entity.getType(),
                entity.getPrice(), SellerMapper.toDomain(entity.getSeller()));
        product.assignId(entity.getId());
        applyStatus(product, entity.getStatus());
        for (ProductVariantJpaEntity variantEntity : entity.getVariants()) {
            ProductVariant variant = new ProductVariant(product, variantEntity.getColor(), variantEntity.getSize(),
                    variantEntity.getModel());
            variant.assignId(variantEntity.getId());
            product.addVariant(variant);
        }
        return product;
    }

    // "sellerEntity" ya lo resolvio el adaptador por id; las variantes se
    // guardan aparte, por su propio repositorio (ver ProductVariantMapper)
    public static ProductJpaEntity toEntity(Product product, SellerJpaEntity sellerEntity) {
        ProductJpaEntity entity = new ProductJpaEntity(product.getName(), product.getDescription(),
                product.getType(), product.getPrice(), sellerEntity, product.getStatus());
        entity.setId(product.getId());
        return entity;
    }

    private static void applyStatus(Product product, ProductStatus status) {
        if (status == ProductStatus.SUSPENDED) {
            product.suspend();
        } else if (status == ProductStatus.DISCONTINUED) {
            product.discontinue();
        }
    }
}
