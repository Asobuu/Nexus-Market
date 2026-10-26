package Application.domain.model;

import Application.domain.enums.ReturnStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

// cuando un comprador pide devolver un producto de un pedido.
// el reembolso lo guardo aca mismo (refundAmount) para no tener que
// crear otra clase Refund aparte
@Getter
@NoArgsConstructor
public class Return {

    private Long id;
    private Order order;
    private Product product;
    private String reason;
    private ReturnStatus status;
    private double refundAmount;

    public Return(Order order, Product product, String reason) {
        this.order = order;
        this.product = product;
        this.reason = reason;
        this.status = ReturnStatus.REQUESTED;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public void approve(double refundAmount) {
        this.status = ReturnStatus.APPROVED;
        this.refundAmount = refundAmount;
    }

    public void reject() {
        this.status = ReturnStatus.REJECTED;
    }

    public void completeRefund() {
        if (status != ReturnStatus.APPROVED) {
            throw new IllegalStateException("la devolucion debe estar aprobada antes de reembolsar");
        }
        this.status = ReturnStatus.COMPLETED;
    }
}
