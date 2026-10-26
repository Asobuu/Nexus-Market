package Application.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

// la factura que se genera cuando el pedido se paga
@Getter
@NoArgsConstructor
public class Invoice {

    private Long id;
    private Order order;
    private double totalAmount;

    public Invoice(Order order) {
        this.order = order;
        this.totalAmount = order.getTotal();
    }

    public void assignId(Long id) {
        this.id = id;
    }
}
