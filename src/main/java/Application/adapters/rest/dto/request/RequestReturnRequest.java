package Application.adapters.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestReturnRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;

    @NotBlank
    @Size(max = 300)
    private String reason;
}
