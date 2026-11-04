package Application.adapters.rest.dto.response;

import Application.domain.model.ProductVariant;
import lombok.Getter;

@Getter
public class ProductVariantResponse {

    private final Long id;
    private final Long productId;
    private final String color;
    private final String size;
    private final String model;

    private ProductVariantResponse(Long id, Long productId, String color, String size, String model) {
        this.id = id;
        this.productId = productId;
        this.color = color;
        this.size = size;
        this.model = model;
    }

    public static ProductVariantResponse from(ProductVariant variant) {
        return new ProductVariantResponse(variant.getId(), variant.getProduct().getId(), variant.getColor(),
                variant.getSize(), variant.getModel());
    }
}
