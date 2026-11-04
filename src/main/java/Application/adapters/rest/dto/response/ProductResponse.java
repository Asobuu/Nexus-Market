package Application.adapters.rest.dto.response;

import Application.domain.enums.ProductStatus;
import Application.domain.enums.ProductType;
import Application.domain.model.Product;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final ProductType type;
    private final double price;
    private final Long sellerId;
    private final ProductStatus status;
    private final List<ProductVariantResponse> variants;

    private ProductResponse(Long id, String name, String description, ProductType type, double price, Long sellerId,
                             ProductStatus status, List<ProductVariantResponse> variants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.sellerId = sellerId;
        this.status = status;
        this.variants = variants;
    }

    public static ProductResponse from(Product product) {
        List<ProductVariantResponse> variants = product.getVariants().stream()
                .map(ProductVariantResponse::from).toList();
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getType(),
                product.getPrice(), product.getSeller().getId(), product.getStatus(), variants);
    }
}
