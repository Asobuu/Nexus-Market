package Application.model;

import Application.model.user.Address;
import Application.model.user.Seller;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// una bodega donde se guardan los productos.
// si owner es null, la bodega es del marketplace; si no, es de ese vendedor
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Address location;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller owner;

    public Warehouse(String name, Address location, Seller owner) {
        this.name = name;
        this.location = location;
        this.owner = owner;
    }
}
