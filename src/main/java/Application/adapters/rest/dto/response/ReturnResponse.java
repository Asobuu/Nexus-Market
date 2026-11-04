package Application.adapters.rest.dto.response;

import Application.domain.enums.ReturnStatus;
import Application.domain.model.Return;
import lombok.Getter;

@Getter
public class ReturnResponse {

    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String reason;
    private final ReturnStatus status;
    private final double refundAmount;

    private ReturnResponse(Long id, Long orderId, Long productId, String reason, ReturnStatus status,
                            double refundAmount) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.reason = reason;
        this.status = status;
        this.refundAmount = refundAmount;
    }

    public static ReturnResponse from(Return returnRequest) {
        return new ReturnResponse(returnRequest.getId(), returnRequest.getOrder().getId(),
                returnRequest.getProduct().getId(), returnRequest.getReason(), returnRequest.getStatus(),
                returnRequest.getRefundAmount());
    }
}
