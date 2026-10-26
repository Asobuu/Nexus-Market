package Application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// una variante de un producto, por ejemplo la misma camiseta pero en
// otro color o talla. no todos los campos aplican siempre (un mismo
// producto puede variar solo en color, o solo en talla, etc)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
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
}
