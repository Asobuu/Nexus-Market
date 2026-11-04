package Application.adapters.persistence.jpa.entity;

import Application.domain.enums.ProductStatus;
import Application.domain.enums.ProductType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id", nullable = false)
    private SellerJpaEntity seller;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    // sin cascade ni orphanRemoval: ProductVariant es un agregado
    // independiente con su propio repositorio/adaptador
    // (ProductVariantRepositoryAdapter). ProductMapper.toEntity nunca llena
    // esta lista al guardar un Product, asi que si tuviera cascade=ALL +
    // orphanRemoval=true, cada vez que se guarda el producto (cambiar
    // precio, suspender, etc.) Hibernate borraria todas las variantes
    // existentes en la BD. Esta relacion es solo para leer (toDomain).
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductVariantJpaEntity> variants = new ArrayList<>();

    public ProductJpaEntity(String name, String description, ProductType type, double price,
                             SellerJpaEntity seller, ProductStatus status) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.seller = seller;
        this.status = status;
    }
}
