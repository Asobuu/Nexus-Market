package Application.adapters.useCases;

import Application.domain.model.AuthToken;
import Application.domain.model.Product;
import Application.domain.model.user.Address;
import Application.domain.model.user.Buyer;
import Application.domain.ports.in.PublicAccessPort;
import Application.domain.ports.out.ProductRepositoryPort;
import Application.domain.services.AuthService;
import org.springframework.stereotype.Component;

import java.util.List;

// acciones sin sesion: registrarse, hacer login y ver el catalogo
// publico. no hay ningun id de actor que validar aca, por eso es el
// adaptador mas simple de los seis
@Component
public class PublicAccessUseCaseAdapter implements PublicAccessPort {

    private final AuthService authService;
    private final ProductRepositoryPort productRepositoryPort;

    public PublicAccessUseCaseAdapter(AuthService authService, ProductRepositoryPort productRepositoryPort) {
        this.authService = authService;
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Buyer registerBuyer(String fullName, String email, String rawPassword, Address address) {
        return authService.register(fullName, email, rawPassword, address);
    }

    @Override
    public AuthToken login(String email, String rawPassword) {
        return authService.login(email, rawPassword);
    }

    @Override
    public List<Product> listPublishedProducts() {
        return productRepositoryPort.findAll().stream().filter(Product::isAvailable).toList();
    }
}
