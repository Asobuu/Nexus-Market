package Application.domain.services;

import Application.domain.enums.AuditEventType;
import Application.domain.exceptions.DuplicateEmailException;
import Application.domain.model.AuditLog;
import Application.domain.model.Warehouse;
import Application.domain.model.user.Address;
import Application.domain.model.user.LogisticsOperator;
import Application.domain.model.user.Seller;
import Application.domain.ports.out.AuditRepositoryPort;
import Application.domain.ports.out.LogisticsOperatorRepositoryPort;
import Application.domain.ports.out.PasswordServicePort;
import Application.domain.ports.out.SellerRepositoryPort;
import Application.domain.ports.out.UserRepositoryPort;
import Application.domain.ports.out.WarehouseRepositoryPort;

// todo lo que solo puede hacer un administrador: registrar vendedores,
// operadores logisticos y bodegas. antes esto estaba mezclado con el
// registro de compradores en una sola clase generica, y no deberia
// porque son responsabilidades de actores distintos
public class AdministrationService {

    private final UserRepositoryPort userRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;
    private final LogisticsOperatorRepositoryPort logisticsOperatorRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;
    private final PasswordServicePort passwordServicePort;
    private final AuditRepositoryPort auditRepositoryPort;

    public AdministrationService(UserRepositoryPort userRepositoryPort, SellerRepositoryPort sellerRepositoryPort,
                                  LogisticsOperatorRepositoryPort logisticsOperatorRepositoryPort,
                                  WarehouseRepositoryPort warehouseRepositoryPort,
                                  PasswordServicePort passwordServicePort,
                                  AuditRepositoryPort auditRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.sellerRepositoryPort = sellerRepositoryPort;
        this.logisticsOperatorRepositoryPort = logisticsOperatorRepositoryPort;
        this.warehouseRepositoryPort = warehouseRepositoryPort;
        this.passwordServicePort = passwordServicePort;
        this.auditRepositoryPort = auditRepositoryPort;
    }

    public Seller registerSeller(String fullName, String email, String rawPassword) {
        if (userRepositoryPort.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
        Seller seller = new Seller(fullName, email);
        seller.assignPasswordHash(passwordServicePort.hash(rawPassword));
        seller = sellerRepositoryPort.save(seller);
        auditRepositoryPort.log(new AuditLog(AuditEventType.USER_REGISTERED, email, "SELLER",
                "se registro el vendedor " + fullName));
        return seller;
    }

    public LogisticsOperator registerLogisticsOperator(String fullName, String email, String rawPassword) {
        if (userRepositoryPort.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
        LogisticsOperator operator = new LogisticsOperator(fullName, email);
        operator.assignPasswordHash(passwordServicePort.hash(rawPassword));
        operator = logisticsOperatorRepositoryPort.save(operator);
        auditRepositoryPort.log(new AuditLog(AuditEventType.USER_REGISTERED, email, "LOGISTICS_OPERATOR",
                "se registro el operador logistico " + fullName));
        return operator;
    }

    public Warehouse registerWarehouse(String name, Address location, Seller owner) {
        Warehouse warehouse = new Warehouse(name, location, owner);
        return warehouseRepositoryPort.save(warehouse);
    }
}
