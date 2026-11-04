package Application.domain.services;

import Application.domain.enums.AuditEventType;
import Application.domain.enums.OrderStatus;
import Application.domain.exceptions.ReturnNotEligibleException;
import Application.domain.model.AuditLog;
import Application.domain.model.Inventory;
import Application.domain.model.Order;
import Application.domain.model.OrderItem;
import Application.domain.model.Product;
import Application.domain.model.Return;
import Application.domain.model.Warehouse;
import Application.domain.ports.out.AuditRepositoryPort;
import Application.domain.ports.out.ReturnRepositoryPort;

// solicitar, aprobar/rechazar y cerrar una devolucion
public class ReturnService {

    private final ReturnRepositoryPort returnRepositoryPort;
    private final AuditRepositoryPort auditRepositoryPort;
    private final InventoryService inventoryService;

    public ReturnService(ReturnRepositoryPort returnRepositoryPort, AuditRepositoryPort auditRepositoryPort,
                          InventoryService inventoryService) {
        this.returnRepositoryPort = returnRepositoryPort;
        this.auditRepositoryPort = auditRepositoryPort;
        this.inventoryService = inventoryService;
    }

    public Return requestReturn(Order order, Product product, String reason) {
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new ReturnNotEligibleException("solo se puede pedir devolucion de un pedido ya entregado");
        }
        // valida que el producto si haya sido parte de ese pedido (si no,
        // cualquier comprador podria pedir devolucion de algo que nunca compro)
        findPurchasedItem(order, product);

        Return returnRequest = new Return(order, product, reason);
        returnRequest = returnRepositoryPort.save(returnRequest);
        auditRepositoryPort.log(new AuditLog(AuditEventType.RETURN_REQUESTED, order.getBuyer().getEmail(), "BUYER",
                "se pidio devolucion del pedido " + order.getId()));
        return returnRequest;
    }

    public Return approveReturn(Return returnRequest, double refundAmount, String adminEmail) {
        returnRequest.approve(refundAmount);
        returnRequest = returnRepositoryPort.save(returnRequest);
        auditRepositoryPort.log(new AuditLog(AuditEventType.RETURN_APPROVED, adminEmail, "ADMINISTRATOR",
                "se aprobo la devolucion " + returnRequest.getId()));
        return returnRequest;
    }

    public Return rejectReturn(Return returnRequest, String adminEmail) {
        returnRequest.reject();
        returnRequest = returnRepositoryPort.save(returnRequest);
        auditRepositoryPort.log(new AuditLog(AuditEventType.RETURN_REJECTED, adminEmail, "ADMINISTRATOR",
                "se rechazo la devolucion " + returnRequest.getId()));
        return returnRequest;
    }

    // "inventory" solo aplica si el producto es fisico (needsShipping); si es
    // digital no hay stock que devolver, se puede pasar null y no se repone nada.
    // si se pasa, tiene que ser el inventario del MISMO producto y de la
    // MISMA bodega de donde salio originalmente (ver OrderItem.warehouse) -
    // si no, se estaria reponiendo stock en la bodega de otro vendedor
    public Return completeRefund(Return returnRequest, String adminEmail, Inventory inventory) {
        returnRequest.completeRefund();
        returnRequest = returnRepositoryPort.save(returnRequest);

        if (inventory != null) {
            OrderItem purchasedItem = findPurchasedItem(returnRequest.getOrder(), returnRequest.getProduct());
            validateInventoryMatchesOrigin(purchasedItem, inventory);
            inventoryService.registerReturn(inventory, purchasedItem.getQuantity());
        }

        auditRepositoryPort.log(new AuditLog(AuditEventType.REFUND_COMPLETED, adminEmail, "ADMINISTRATOR",
                "se completo el reembolso de la devolucion " + returnRequest.getId()));
        return returnRequest;
    }

    // confirma que el producto fue parte del pedido y devuelve el item
    // correspondiente (comparando por id, no por referencia: los objetos
    // pueden venir de consultas distintas)
    private OrderItem findPurchasedItem(Order order, Product product) {
        for (OrderItem item : order.getItems()) {
            if (item.getProduct().getId().equals(product.getId())) {
                return item;
            }
        }
        throw new ReturnNotEligibleException("el producto no forma parte de ese pedido");
    }

    // el stock se repone en la misma bodega de donde salio, no en
    // cualquier otra (eso seria devolverle el stock al vendedor equivocado)
    private void validateInventoryMatchesOrigin(OrderItem purchasedItem, Inventory inventory) {
        if (!inventory.getProduct().getId().equals(purchasedItem.getProduct().getId())) {
            throw new ReturnNotEligibleException("el inventario indicado no corresponde al producto devuelto");
        }
        Warehouse originalWarehouse = purchasedItem.getWarehouse();
        if (originalWarehouse != null && !inventory.getWarehouse().getId().equals(originalWarehouse.getId())) {
            throw new ReturnNotEligibleException("el stock debe devolverse a la misma bodega de donde salio");
        }
    }
}
