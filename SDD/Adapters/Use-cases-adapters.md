# SDD — Adaptadores de Casos de Uso

Viven en `adapters/useCases/`. Cada uno implementa un puerto de entrada, inyectando los servicios de dominio que necesita.

```
PublicAccessUseCaseAdapter implements PublicAccessPort
  -> AuthService, ProductRepositoryPort

BuyerUseCaseAdapter implements BuyerPort
  -> CartService, CheckoutService, OrderService, ReturnService, InventoryRepositoryPort

SellerUseCaseAdapter implements SellerPort
  -> ProductService, InventoryService

LogisticsOperatorUseCaseAdapter implements LogisticsOperatorPort
  -> ShipmentService

AdministratorUseCaseAdapter implements AdministratorPort
  -> AdministrationService, ReturnService

SupervisorUseCaseAdapter implements SupervisorPort
  -> ReportService
```

Estos adaptadores no tienen lógica de negocio propia: solo validan que el actor (resuelto del JWT) tenga el id/rol correcto y delegan en el servicio de dominio.
