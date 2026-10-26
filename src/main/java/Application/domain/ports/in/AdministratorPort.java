package Application.domain.ports.in;

import Application.domain.model.Return;
import Application.domain.model.Warehouse;
import Application.domain.model.user.Address;
import Application.domain.model.user.LogisticsOperator;
import Application.domain.model.user.Seller;

// requiere sesion con rol ADMINISTRATOR
public interface AdministratorPort {
    Seller registerSeller(Long adminId, String fullName, String email, String rawPassword);
    LogisticsOperator registerLogisticsOperator(Long adminId, String fullName, String email, String rawPassword);
    Warehouse registerWarehouse(Long adminId, String name, Address location, Long ownerSellerId);
    Return approveReturn(Long adminId, Long returnId, double refundAmount);
    Return rejectReturn(Long adminId, Long returnId);
    Return completeRefund(Long adminId, Long returnId);
}
