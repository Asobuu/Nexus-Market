package Application.domain.services;

import Application.domain.enums.AuditEventType;
import Application.domain.enums.UserStatus;
import Application.domain.exceptions.DuplicateEmailException;
import Application.domain.model.AuditLog;
import Application.domain.model.AuthToken;
import Application.domain.model.user.Address;
import Application.domain.model.user.Buyer;
import Application.domain.model.user.User;
import Application.domain.ports.out.AuditRepositoryPort;
import Application.domain.ports.out.BuyerRepositoryPort;
import Application.domain.ports.out.JwtServicePort;
import Application.domain.ports.out.PasswordServicePort;
import Application.domain.ports.out.UserRepositoryPort;

// registro de compradores y login. es dominio puro: no sabe nada de
// Spring Security ni de como se calcula un hash, solo usa los puertos
public class AuthService {

    private final UserRepositoryPort userRepositoryPort;
    private final BuyerRepositoryPort buyerRepositoryPort;
    private final PasswordServicePort passwordServicePort;
    private final JwtServicePort jwtServicePort;
    private final AuditRepositoryPort auditRepositoryPort;

    public AuthService(UserRepositoryPort userRepositoryPort, BuyerRepositoryPort buyerRepositoryPort,
                        PasswordServicePort passwordServicePort, JwtServicePort jwtServicePort,
                        AuditRepositoryPort auditRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.buyerRepositoryPort = buyerRepositoryPort;
        this.passwordServicePort = passwordServicePort;
        this.jwtServicePort = jwtServicePort;
        this.auditRepositoryPort = auditRepositoryPort;
    }

    public Buyer register(String fullName, String email, String rawPassword, Address address) {
        if (userRepositoryPort.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
        Buyer buyer = new Buyer(fullName, email, address);
        buyer.assignPasswordHash(passwordServicePort.hash(rawPassword));
        buyer = buyerRepositoryPort.save(buyer);
        auditRepositoryPort.log(new AuditLog(AuditEventType.USER_REGISTERED, email, "BUYER",
                "se registro el comprador " + fullName));
        return buyer;
    }

    public AuthToken login(String email, String rawPassword) {
        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("correo o contraseña incorrectos"));
        if (!passwordServicePort.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("correo o contraseña incorrectos");
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("esta cuenta esta bloqueada");
        }
        String token = jwtServicePort.generateToken(user);
        return new AuthToken(token, user.getId(), user.getRoleName());
    }
}
