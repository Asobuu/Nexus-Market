package Application.adapters.rest.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveReturnRequest {

    @Positive
    private double refundAmount;
}
