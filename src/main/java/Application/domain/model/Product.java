package Application.domain.model;

import Application.domain.enums.ProductStatus;
import Application.domain.enums.ProductType;
import Application.domain.model.user.Seller;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// lo que un vendedor ofrece en el catalogo
@Getter
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private String description;
    private ProductType type;
    private double price;
    private Seller seller;
    private ProductStatus status;

    // el mismo producto pero en otro color, talla o modelo
    private List<ProductVariant> variants = new ArrayList<>();

    public Product(String name, String description, ProductType type, double price, Seller seller) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.seller = seller;
        this.status = ProductStatus.PUBLISHED;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void assignSeller(Seller seller) {
        this.seller = seller;
    }

    public boolean needsShipping() {
        return type == ProductType.PHYSICAL;
    }

    public boolean isAvailable() {
        return status == ProductStatus.PUBLISHED;
    }

    public void publish() {
        status = ProductStatus.PUBLISHED;
    }

    public void suspend() {
        status = ProductStatus.SUSPENDED;
    }

    public void discontinue() {
        status = ProductStatus.DISCONTINUED;
    }

    public void addVariant(ProductVariant variant) {
        variants.add(variant);
    }
}
