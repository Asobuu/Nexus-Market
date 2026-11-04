package Application.adapters.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {

    @Valid
    @NotNull
    private AddressRequest deliveryAddress;
}
