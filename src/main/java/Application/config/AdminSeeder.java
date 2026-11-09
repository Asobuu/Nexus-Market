package Application.config;

import Application.adapters.persistence.jpa.entity.AdministratorJpaEntity;
import Application.adapters.persistence.jpa.repository.UserJpaRepository;
import Application.domain.ports.out.PasswordServicePort;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Bootstrap del primer Administrator. AdministratorPort.registerSeller /
// registerLogisticsOperator / registerWarehouse exigen un adminId ya
// autenticado (a proposito: un administrador no se auto-registra, solo
// PublicAccessPort.registerBuyer es publico), pero eso significa que sin
// ningun Administrator ya creado en la base de datos es imposible crear
// el primer Seller/LogisticsOperator/Warehouse - y por lo tanto imposible
// publicar productos o armar un pedido real para la demo.
//
// Este seeder corre una sola vez al arrancar la app y crea un
// administrador por defecto si todavia no existe (verifica por email,
// asi que no duplica nada en arranques siguientes). Usa directo
// UserJpaRepository (no AdministrationService/UserRepositoryPort) porque
// a proposito no existe ningun metodo en el dominio para crear un
// Administrator - esa es la unica forma de meter el primero.
//
// IMPORTANTE: esto es solo para la entrega del curso. En un proyecto
// real este seeder no existiria (el primer admin se crearia por fuera,
// a mano, una sola vez) y nunca se deja una contraseña fija en el
// codigo fuente.
@Component
public class AdminSeeder implements ApplicationRunner {

    private static final String SEED_EMAIL = "admin@nexusmarket.com";
    private static final String SEED_PASSWORD = "Admin123!";

    private final UserJpaRepository userJpaRepository;
    private final PasswordServicePort passwordServicePort;

    public AdminSeeder(UserJpaRepository userJpaRepository, PasswordServicePort passwordServicePort) {
        this.userJpaRepository = userJpaRepository;
        this.passwordServicePort = passwordServicePort;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userJpaRepository.existsByEmail(SEED_EMAIL)) {
            return;
        }
        AdministratorJpaEntity admin = new AdministratorJpaEntity("Administrador Inicial", SEED_EMAIL);
        admin.setPasswordHash(passwordServicePort.hash(SEED_PASSWORD));
        userJpaRepository.save(admin);
        System.out.println("== Administrador inicial creado -> email: " + SEED_EMAIL
                + " | password: " + SEED_PASSWORD + " ==");
    }
}
