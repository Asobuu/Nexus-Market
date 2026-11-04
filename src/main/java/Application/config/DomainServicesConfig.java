package Application.config;

import Application.domain.ports.out.AuditRepositoryPort;
import Application.domain.ports.out.BuyerRepositoryPort;
import Application.domain.ports.out.CartRepositoryPort;
import Application.domain.ports.out.InventoryMovementRepositoryPort;
import Application.domain.ports.out.InventoryRepositoryPort;
import Application.domain.ports.out.InvoiceRepositoryPort;
import Application.domain.ports.out.JwtServicePort;
import Application.domain.ports.out.LogisticsOperatorRepositoryPort;
import Application.domain.ports.out.OrderRepositoryPort;
import Application.domain.ports.out.PasswordServicePort;
import Application.domain.ports.out.ProductRepositoryPort;
import Application.domain.ports.out.ProductVariantRepositoryPort;
import Application.domain.ports.out.ReturnRepositoryPort;
import Application.domain.ports.out.SellerRepositoryPort;
import Application.domain.ports.out.ShipmentRepositoryPort;
import Application.domain.ports.out.UserRepositoryPort;
import Application.domain.ports.out.WarehouseRepositoryPort;
import Application.domain.services.AdministrationService;
import Application.domain.services.AuthService;
import Application.domain.services.CartService;
import Application.domain.services.CheckoutService;
import Application.domain.services.InventoryService;
import Application.domain.services.OrderService;
import Application.domain.services.PaymentService;
import Application.domain.services.ProductService;
import Application.domain.services.ReportService;
import Application.domain.services.ReturnService;
import Application.domain.services.ShipmentService;
import Application.domain.services.WarehouseService;
import Application.domain.validators.WarehouseOwnershipValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// los servicios de dominio son POJOs puros a proposito: no tienen
// @Component ni ninguna anotacion de Spring, para que el dominio no
// dependa del framework (ver el comentario de AuthService: "es dominio
// puro: no sabe nada de Spring Security ni de como se calcula un
// hash"). esta clase es la unica que sabe que el proyecto corre sobre
// Spring: aca se cablean como @Bean, inyectandoles los adaptadores de
// salida (puertos) que Spring si gestiona.
//
// AuthService y AdministrationService necesitan PasswordServicePort, y
// AuthService tambien JwtServicePort. esos dos puertos todavia no
// tienen adaptador (son parte de la capa de seguridad/REST, siguiente
// fase segun el SDD): mientras no exista un @Bean que los implemente,
// el contexto de Spring no va a poder levantar completo. no se
// resuelve aca a proposito, para no inventar decisiones de hashing/JWT
// que le corresponden a esa fase
@Configuration
public class DomainServicesConfig {

    @Bean
    public WarehouseOwnershipValidator warehouseOwnershipValidator() {
        return new WarehouseOwnershipValidator();
    }

    @Bean
    public AuthService authService(UserRepositoryPort userRepositoryPort, BuyerRepositoryPort buyerRepositoryPort,
                                     PasswordServicePort passwordServicePort, JwtServicePort jwtServicePort,
                                     AuditRepositoryPort auditRepositoryPort) {
        return new AuthService(userRepositoryPort, buyerRepositoryPort, passwordServicePort, jwtServicePort,
                auditRepositoryPort);
    }

    @Bean
    public CartService cartService(CartRepositoryPort cartRepositoryPort) {
        return new CartService(cartRepositoryPort);
    }

    @Bean
    public InventoryService inventoryService(InventoryRepositoryPort inventoryRepositoryPort,
                                               InventoryMovementRepositoryPort inventoryMovementRepositoryPort) {
        return new InventoryService(inventoryRepositoryPort, inventoryMovementRepositoryPort);
    }

    @Bean
    public CheckoutService checkoutService(OrderRepositoryPort orderRepositoryPort, InventoryService inventoryService,
                                             CartService cartService) {
        return new CheckoutService(orderRepositoryPort, inventoryService, cartService);
    }

    @Bean
    public OrderService orderService(ShipmentRepositoryPort shipmentRepositoryPort,
                                       WarehouseOwnershipValidator warehouseOwnershipValidator) {
        return new OrderService(shipmentRepositoryPort, warehouseOwnershipValidator);
    }

    @Bean
    public PaymentService paymentService(OrderRepositoryPort orderRepositoryPort,
                                           InvoiceRepositoryPort invoiceRepositoryPort,
                                           AuditRepositoryPort auditRepositoryPort) {
        return new PaymentService(orderRepositoryPort, invoiceRepositoryPort, auditRepositoryPort);
    }

    @Bean
    public ReturnService returnService(ReturnRepositoryPort returnRepositoryPort,
                                         AuditRepositoryPort auditRepositoryPort, InventoryService inventoryService) {
        return new ReturnService(returnRepositoryPort, auditRepositoryPort, inventoryService);
    }

    @Bean
    public ProductService productService(ProductRepositoryPort productRepositoryPort,
                                           InventoryRepositoryPort inventoryRepositoryPort,
                                           InventoryService inventoryService,
                                           ProductVariantRepositoryPort productVariantRepositoryPort,
                                           AuditRepositoryPort auditRepositoryPort) {
        return new ProductService(productRepositoryPort, inventoryRepositoryPort, inventoryService,
                productVariantRepositoryPort, auditRepositoryPort);
    }

    @Bean
    public ShipmentService shipmentService(ShipmentRepositoryPort shipmentRepositoryPort,
                                             OrderRepositoryPort orderRepositoryPort,
                                             AuditRepositoryPort auditRepositoryPort) {
        return new ShipmentService(shipmentRepositoryPort, orderRepositoryPort, auditRepositoryPort);
    }

    @Bean
    public AdministrationService administrationService(UserRepositoryPort userRepositoryPort,
                                                         SellerRepositoryPort sellerRepositoryPort,
                                                         LogisticsOperatorRepositoryPort logisticsOperatorRepositoryPort,
                                                         PasswordServicePort passwordServicePort,
                                                         AuditRepositoryPort auditRepositoryPort) {
        return new AdministrationService(userRepositoryPort, sellerRepositoryPort, logisticsOperatorRepositoryPort,
                passwordServicePort, auditRepositoryPort);
    }

    @Bean
    public WarehouseService warehouseService(WarehouseRepositoryPort warehouseRepositoryPort) {
        return new WarehouseService(warehouseRepositoryPort);
    }

    @Bean
    public ReportService reportService(InvoiceRepositoryPort invoiceRepositoryPort,
                                         OrderRepositoryPort orderRepositoryPort,
                                         ProductRepositoryPort productRepositoryPort,
                                         InventoryRepositoryPort inventoryRepositoryPort) {
        return new ReportService(invoiceRepositoryPort, orderRepositoryPort, productRepositoryPort,
                inventoryRepositoryPort);
    }
}
