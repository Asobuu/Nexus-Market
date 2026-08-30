package Application.enums;

// pasos por los que va pasando un pedido, en orden
public enum OrderStatus {
    CART,
    PENDING_PAYMENT,
    PAID,
    SHIPPED,
    DELIVERED
}
