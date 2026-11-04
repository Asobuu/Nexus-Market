package Application.adapters.rest.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddStockRequest {

    @Min(1)
    private int amount;
}
