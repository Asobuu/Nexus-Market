# SDD — Servicios de Dominio

Viven en `domain/services/`. Dependen únicamente de puertos de salida (`domain/ports/out/`) y de otros servicios de dominio — nunca de Spring, JPA ni Mongo. Cada uno registra un evento de auditoría cuando corresponde, a través de `AuditRepositoryPort`.

## AuthService
- `register(fullName, email, rawPassword, Address)`: valida que `email` no exista (`UserRepositoryPort.existsByEmail`) → si existe, `DuplicateEmailException`. Guarda el hash de la contraseña (`PasswordServicePort.hash`). Crea el `Buyer`. Audita `USER_REGISTERED`.
- `login(email, rawPassword)`: busca el usuario por email, valida con `PasswordServicePort.matches`, valida `status == ACTIVE`, genera token con `JwtServicePort.generateToken`.

## AdministrationService
- `registerSeller`, `registerLogisticsOperator`: misma validación de email único que `AuthService.register`, pero solo puede invocarlas un `Administrator` (lo valida el caso de uso, no el servicio). Audita `USER_REGISTERED`.
- `registerWarehouse(name, location, ownerSellerId?)`: si `ownerSellerId` es null, la bodega es del marketplace.

## ProductService
- `publishProduct(...)`: crea el producto en `PUBLISHED`; si es `PHYSICAL`, crea el inventario inicial en `0` y delega en `InventoryService.registerReceipt` para dejarlo en `initialStock` (así el ingreso también queda en el historial de movimientos).
- `addVariant`, `suspendProduct`, `discontinueProduct`: delegan en los métodos de comportamiento de `Product`.

## InventoryService
Todo cambio de cantidad pasa por aquí y deja un `InventoryMovement`:
- `registerReceipt(inventory, amount)` → `RECEIPT`
- `reserveStock(inventory, amount)` → `RESERVATION`, lanza `InsufficientStockException` si no alcanza
- `registerSale(inventory, amount)` → `SALE`
- `adjustStock(inventory, amount)` → `ADJUSTMENT`
- `registerReturn(inventory, amount)` → `RETURN`

## CartService
Solo administra el carrito: `addToCart`, `viewCart`, `clear`. **No conoce `Order` ni crea pedidos** — esa es responsabilidad de `CheckoutService`.

## CheckoutService
- `checkout(cart, inventories, deliveryAddress)`: precondición — el carrito no puede estar vacío. Reserva stock de cada ítem vía `InventoryService.reserveStock`, arma el `Order` en `PENDING_PAYMENT`, vacía el carrito. Devuelve el `Order` creado.

## OrderService
- `payOrder(order)`: `order.pay()`, genera `Invoice`. Audita `ORDER_PAID`.
- `shipOrder(order, warehouse)`: crea `Shipment` en `PREPARING`.
- `deliverOrder(order, shipment)`: `shipment.deliver()`, `order.deliver()`. Audita `ORDER_DELIVERED`.

## ShipmentService
- `assignOperator(shipment, operator)`, `markShipped(shipment)` (audita `ORDER_SHIPPED`), `markDelivered(shipment)`.

## ReturnService
- `requestReturn(order, product, reason)`: precondición — `order.status == DELIVERED`, si no, `ReturnNotEligibleException`. Audita `RETURN_REQUESTED`.
- `approveReturn(returnRequest, amount)`: audita `RETURN_APPROVED`.
- `rejectReturn(returnRequest)`: audita `RETURN_REJECTED`.
- `completeRefund(returnRequest)`: precondición — debe estar `APPROVED`. Audita `REFUND_COMPLETED`.

## ReportService
Solo lectura, usado por `SupervisorPort`: `getTotalSales`, `countPendingOrders`, `countProductsByStatus`, `getLowStock`.
