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

    // la usa el mapper de persistencia al reconstruir una factura ya
    // guardada: el constructor recalcula el total desde el pedido, pero el
    // valor real es el que quedo grabado (si el precio de un producto
    // cambia despues, una factura vieja no se debe recalcular)
    public void assignTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
