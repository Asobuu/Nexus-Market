package Application.adapters.persistence.jpa.mapper;

import Application.adapters.persistence.jpa.entity.ProductJpaEntity;
import Application.adapters.persistence.jpa.entity.ProductVariantJpaEntity;
import Application.domain.model.ProductVariant;

public final class ProductVariantMapper {

    private ProductVariantMapper() {
    }

    public static ProductVariant toDomain(ProductVariantJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductVariant variant = new ProductVariant(ProductMapper.toDomain(entity.getProduct()), entity.getColor(),
                entity.getSize(), entity.getModel());
        variant.assignId(entity.getId());
        return variant;
    }

    public static ProductVariantJpaEntity toEntity(ProductVariant variant, ProductJpaEntity productEntity) {
        ProductVariantJpaEntity entity = new ProductVariantJpaEntity(productEntity, variant.getColor(),
                variant.getSize(), variant.getModel());
        entity.setId(variant.getId());
        return entity;
    }
}
