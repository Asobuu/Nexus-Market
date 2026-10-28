package Application.domain.services;

import Application.domain.enums.AuditEventType;
import Application.domain.enums.OrderStatus;
import Application.domain.exceptions.ReturnNotEligibleException;
import Application.domain.model.AuditLog;
import Application.domain.model.Order;
import Application.domain.model.Product;
import Application.domain.model.Return;
import Application.domain.ports.out.AuditRepositoryPort;
import Application.domain.ports.out.ReturnRepositoryPort;

// solicitar, aprobar/rechazar y cerrar una devolucion
public class ReturnService {

    private final ReturnRepositoryPort returnRepositoryPort;
    private final AuditRepositoryPort auditRepositoryPort;

    public ReturnService(ReturnRepositoryPort returnRepositoryPort, AuditRepositoryPort auditRepositoryPort) {
        this.returnRepositoryPort = returnRepositoryPort;
        this.auditRepositoryPort = auditRepositoryPort;
    }

    public Return requestReturn(Order order, Product product, String reason) {
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new ReturnNotEligibleException("solo se puede pedir devolucion de un pedido ya entregado");
        }
        Return returnRequest = new Return(order, product, reason);
        returnRequest = returnRepositoryPort.save(returnRequest);
        auditRepositoryPort.log(new AuditLog(AuditEventType.RETURN_REQUESTED, order.getBuyer().getEmail(), "BUYER",
                "se pidio devolucion del pedido " + order.getId()));
        return returnRequest;
    }

    public Return approveReturn(Return returnRequest, double refundAmount) {
        returnRequest.approve(refundAmount);
        returnRequest = returnRepositoryPort.save(returnRequest);
        auditRepositoryPort.log(new AuditLog(AuditEventType.RETURN_APPROVED, "", "ADMINISTRATOR",
                "se aprobo la devolucion " + returnRequest.getId()));
        return returnRequest;
    }

    public Return rejectReturn(Return returnRequest) {
        returnRequest.reject();
        returnRequest = returnRepositoryPort.save(returnRequest);
        auditRepositoryPort.log(new AuditLog(AuditEventType.RETURN_REJECTED, "", "ADMINISTRATOR",
                "se rechazo la devolucion " + returnRequest.getId()));
        return returnRequest;
    }

    public Return completeRefund(Return returnRequest) {
        returnRequest.completeRefund();
        returnRequest = returnRepositoryPort.save(returnRequest);
        auditRepositoryPort.log(new AuditLog(AuditEventType.REFUND_COMPLETED, "", "ADMINISTRATOR",
                "se completo el reembolso de la devolucion " + returnRequest.getId()));
        return returnRequest;
    }
}
