package Application.adapters.rest.dto.request;

import Application.domain.model.user.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// una direccion dentro de otro request (registro de comprador, checkout,
// registro de bodega). reglas minimas segun Rest-validation.md
@Getter
@Setter
public class AddressRequest {

    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    public Address toDomain() {
        return new Address(street, city, country);
    }
}
