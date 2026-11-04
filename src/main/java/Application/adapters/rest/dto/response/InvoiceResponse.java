package Application.adapters.rest.dto.response;

import Application.domain.model.Invoice;
import lombok.Getter;

@Getter
public class InvoiceResponse {

    private final Long id;
    private final Long orderId;
    private final double totalAmount;

    private InvoiceResponse(Long id, Long orderId, double totalAmount) {
        this.id = id;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
    }

    public static InvoiceResponse from(Invoice invoice) {
        return new InvoiceResponse(invoice.getId(), invoice.getOrder().getId(), invoice.getTotalAmount());
    }
}
