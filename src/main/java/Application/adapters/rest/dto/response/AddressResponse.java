package Application.adapters.rest.dto.response;

import Application.domain.model.user.Address;
import lombok.Getter;

@Getter
public class AddressResponse {

    private final String street;
    private final String city;
    private final String country;

    private AddressResponse(String street, String city, String country) {
        this.street = street;
        this.city = city;
        this.country = country;
    }

    public static AddressResponse from(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressResponse(address.getStreet(), address.getCity(), address.getCountry());
    }
}
