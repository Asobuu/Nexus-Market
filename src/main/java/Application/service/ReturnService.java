package Application.service;

import Application.enums.OrderStatus;
import Application.model.Order;
import Application.model.Product;
import Application.model.Return;
import Application.repository.ReturnRepository;
import org.springframework.stereotype.Service;

// solicitar, aprobar/rechazar y cerrar una devolucion
@Service
public class ReturnService {

    private final ReturnRepository returnRepository;

    public ReturnService(ReturnRepository returnRepository) {
        this.returnRepository = returnRepository;
    }

    // solo se puede pedir devolucion de un pedido que ya fue entregado
    public Return requestReturn(Order order, Product product, String reason) {
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException("solo se puede pedir devolucion de un pedido ya entregado");
        }
        Return returnRequest = new Return(order, product, reason);
        return returnRepository.save(returnRequest);
    }

    public Return approveReturn(Return returnRequest, double refundAmount) {
        returnRequest.approve(refundAmount);
        return returnRepository.save(returnRequest);
    }

    public Return rejectReturn(Return returnRequest) {
        returnRequest.reject();
        return returnRepository.save(returnRequest);
    }

    public Return completeRefund(Return returnRequest) {
        returnRequest.completeRefund();
        return returnRepository.save(returnRequest);
    }
}
