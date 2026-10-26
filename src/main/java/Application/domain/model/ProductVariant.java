package Application.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

// una variante de un producto, por ejemplo la misma camiseta pero en
// otro color o talla. no todos los campos aplican siempre (un mismo
// producto puede variar solo en color, o solo en talla, etc)
@Getter
@NoArgsConstructor
public class ProductVariant {

    private Long id;
    private Product product;
    private String color;
    private String size;
    private String model;

    public ProductVariant(Product product, String color, String size, String model) {
        this.product = product;
        this.color = color;
        this.size = size;
        this.model = model;
    }

    public void assignId(Long id) {
        this.id = id;
    }
}
