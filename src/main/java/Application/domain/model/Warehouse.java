package Application.domain.model;

import Application.domain.model.user.Address;
import Application.domain.model.user.Seller;
import lombok.Getter;
import lombok.NoArgsConstructor;

// una bodega donde se guardan los productos.
// si owner es null, la bodega es del marketplace; si no, es de ese vendedor
@Getter
@NoArgsConstructor
public class Warehouse {

    private Long id;
    private String name;
    private Address location;
    private Seller owner;

    public Warehouse(String name, Address location, Seller owner) {
        this.name = name;
        this.location = location;
        this.owner = owner;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void assignOwner(Seller owner) {
        this.owner = owner;
    }
}
