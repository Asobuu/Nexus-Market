package Application.model.user;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// un comprador puede armar carrito y hacer pedidos.
// solo puede ver/editar sus propios datos, nunca los de otro comprador
@Entity
@DiscriminatorValue("BUYER")
@Getter
@Setter
@NoArgsConstructor
public class Buyer extends User {

    @Embedded
    private Address address;

    // direcciones extra, quedan en otra tabla aparte (buyer_extra_addresses)
    @ElementCollection
    @CollectionTable(name = "buyer_extra_addresses", joinColumns = @JoinColumn(name = "buyer_id"))
    private List<Address> otherAddresses = new ArrayList<>();

    public Buyer(String fullName, String email, Address address) {
        super(fullName, email);
        this.address = address;
    }

    @Override
    public String getRoleName() {
        return "Buyer";
    }

    public void addOtherAddress(Address address) {
        otherAddresses.add(address);
    }
}
