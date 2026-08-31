package Application.model;

import Application.enums.ProductType;
import Application.model.user.Seller;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// lo que un vendedor ofrece en el catalogo
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private double price;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private boolean published;

    public Product(String name, String description, ProductType type, double price, Seller seller) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.seller = seller;
        this.published = true;
    }

    public boolean needsShipping() {
        return type == ProductType.PHYSICAL;
    }
}
