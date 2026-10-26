package Application.domain.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// un comprador puede armar carrito y hacer pedidos.
// solo puede ver/editar sus propios datos, nunca los de otro comprador
@Getter
@NoArgsConstructor
public class Buyer extends User {

    private Address address;

    // direcciones extra ademas de la principal
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
