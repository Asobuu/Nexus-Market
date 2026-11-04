package Application.adapters.rest.dto.response;

import Application.domain.model.user.Seller;
import lombok.Getter;

@Getter
public class SellerResponse {

    private final Long id;
    private final String fullName;
    private final String email;

    private SellerResponse(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public static SellerResponse from(Seller seller) {
        return new SellerResponse(seller.getId(), seller.getFullName(), seller.getEmail());
    }
}
