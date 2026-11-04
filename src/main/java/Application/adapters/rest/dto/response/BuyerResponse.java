package Application.adapters.rest.dto.response;

import Application.domain.model.user.Buyer;
import lombok.Getter;

@Getter
public class BuyerResponse {

    private final Long id;
    private final String fullName;
    private final String email;
    private final AddressResponse address;

    private BuyerResponse(Long id, String fullName, String email, AddressResponse address) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.address = address;
    }

    public static BuyerResponse from(Buyer buyer) {
        return new BuyerResponse(buyer.getId(), buyer.getFullName(), buyer.getEmail(),
                AddressResponse.from(buyer.getAddress()));
    }
}
