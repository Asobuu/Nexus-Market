package Application.domain.services;

import Application.domain.enums.AuditEventType;
import Application.domain.exceptions.DuplicateEmailException;
import Application.domain.model.AuditLog;
import Application.domain.model.user.LogisticsOperator;
import Application.domain.model.user.Seller;
import Application.domain.ports.out.AuditRepositoryPort;
import Application.domain.ports.out.LogisticsOperatorRepositoryPort;
import Application.domain.ports.out.PasswordServicePort;
import Application.domain.ports.out.SellerRepositoryPort;
import Application.domain.ports.out.UserRepositoryPort;

// usuarios que solo puede crear un administrador: vendedores y
// operadores logisticos (a diferencia del comprador, que se registra
// solo). registrar bodegas se separo en WarehouseService: una bodega es
// un recurso, no un usuario, son responsabilidades distintas aunque las
// dos las dispare un administrador
public class AdministrationService {

    private final UserRepositoryPort userRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;
    private final LogisticsOperatorRepositoryPort logisticsOperatorRepositoryPort;
    private final PasswordServicePort passwordServicePort;
    private final AuditRepositoryPort auditRepositoryPort;

    public AdministrationService(UserRepositoryPort userRepositoryPort, SellerRepositoryPort sellerRepositoryPort,
                                  LogisticsOperatorRepositoryPort logisticsOperatorRepositoryPort,
                                  PasswordServicePort passwordServicePort,
                                  AuditRepositoryPort auditRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.sellerRepositoryPort = sellerRepositoryPort;
        this.logisticsOperatorRepositoryPort = logisticsOperatorRepositoryPort;
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
}
