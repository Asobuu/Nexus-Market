package Application.domain.model;

import Application.domain.enums.ReturnStatus;
import Application.domain.exceptions.ReturnNotEligibleException;
import lombok.Getter;
import lombok.NoArgsConstructor;

// cuando un comprador pide devolver un producto de un pedido.
// el reembolso lo guardo aca mismo (refundAmount) para no tener que
// crear otra clase Refund aparte.
// avanza siempre REQUESTED -> (APPROVED o REJECTED) -> COMPLETED (solo
// si se aprobo), sin saltarse pasos ni volver atras
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
        requireStatus(ReturnStatus.REQUESTED);
        this.status = ReturnStatus.APPROVED;
        this.refundAmount = refundAmount;
    }

    public void reject() {
        requireStatus(ReturnStatus.REQUESTED);
        this.status = ReturnStatus.REJECTED;
    }

    public void completeRefund() {
        requireStatus(ReturnStatus.APPROVED);
        this.status = ReturnStatus.COMPLETED;
    }

    // las usa el mapper de persistencia para reconstruir una devolucion ya
    // guardada, saltandose las validaciones de transicion (ver Order.assignStatus)
    public void assignStatus(ReturnStatus status) {
        this.status = status;
    }

    public void assignRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    private void requireStatus(ReturnStatus expected) {
        if (status != expected) {
            throw new ReturnNotEligibleException(
                    "la devolucion " + id + " esta en estado " + status + ", se esperaba " + expected);
        }
    }
}
