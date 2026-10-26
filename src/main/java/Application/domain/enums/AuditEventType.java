package Application.domain.enums;

// que tipo de evento quedo registrado en la auditoria
public enum AuditEventType {
    USER_REGISTERED,
    PRODUCT_PUBLISHED,
    ORDER_PAID,
    ORDER_SHIPPED,
    ORDER_DELIVERED,
    RETURN_REQUESTED,
    RETURN_APPROVED,
    RETURN_REJECTED,
    REFUND_COMPLETED
}
