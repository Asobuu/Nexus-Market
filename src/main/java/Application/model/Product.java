package Application.model;

import Application.enums.ProductStatus;
import Application.enums.ProductType;
import Application.model.user.Seller;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    // el mismo producto pero en otro color, talla o modelo
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants = new ArrayList<>();

    public Product(String name, String description, ProductType type, double price, Seller seller) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.seller = seller;
        this.status = ProductStatus.PUBLISHED;
    }

    public boolean needsShipping() {
        return type == ProductType.PHYSICAL;
    }

    public boolean isAvailable() {
        return status == ProductStatus.PUBLISHED;
    }

    public void suspend() {
        status = ProductStatus.SUSPENDED;
    }

    public void discontinue() {
        status = ProductStatus.DISCONTINUED;
    }

    public void publish() {
        status = ProductStatus.PUBLISHED;
    }

    public void addVariant(ProductVariant variant) {
        variants.add(variant);
    }
}
