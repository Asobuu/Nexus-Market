package Application.model;

import Application.enums.ReturnStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// cuando un comprador pide devolver un producto de un pedido.
// el reembolso lo guardo aca mismo (refundAmount) para no tener que
// crear otra clase Refund aparte
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Return {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String reason;

    @Enumerated(EnumType.STRING)
    private ReturnStatus status;

    private double refundAmount;

    public Return(Order order, Product product, String reason) {
        this.order = order;
        this.product = product;
        this.reason = reason;
        this.status = ReturnStatus.REQUESTED;
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
