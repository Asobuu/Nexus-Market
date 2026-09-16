package Application.service;

import Application.model.user.Address;
import Application.model.user.Buyer;
import Application.model.user.Seller;
import Application.repository.BuyerRepository;
import Application.repository.SellerRepository;
import org.springframework.stereotype.Service;

// registro de usuarios. las validaciones basicas (que no falten datos)
// las dejo aca en vez de en las entidades
@Service
public class UserService {

    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;

    public UserService(BuyerRepository buyerRepository, SellerRepository sellerRepository) {
        this.buyerRepository = buyerRepository;
        this.sellerRepository = sellerRepository;
    }

    public Buyer registerBuyer(String fullName, String email, Address address) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("el email es obligatorio");
        }
        Buyer buyer = new Buyer(fullName, email, address);
        return buyerRepository.save(buyer);
    }

    // los vendedores no se registran solos, los crea un administrador
    public Seller registerSeller(String fullName, String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("el email es obligatorio");
        }
        Seller seller = new Seller(fullName, email);
        return sellerRepository.save(seller);
    }
}
