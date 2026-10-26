package Application.domain.ports.in;

import Application.domain.model.AuthToken;
import Application.domain.model.Product;
import Application.domain.model.user.Address;
import Application.domain.model.user.Buyer;

import java.util.List;

// acciones que no requieren sesion
public interface PublicAccessPort {
    Buyer registerBuyer(String fullName, String email, String rawPassword, Address address);
    AuthToken login(String email, String rawPassword);
    List<Product> listPublishedProducts();
}
