package Application.adapters.rest.dto.request;

import Application.domain.enums.ProductType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishProductRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 2000)
    private String description;

    @NotNull
    private ProductType type;

    @Positive
    private double price;

    // null si el producto es digital (no necesita bodega, ver
    // Product.needsShipping); obligatorio si es PHYSICAL
    private Long warehouseId;

    @Min(1)
    private int initialStock;
}
