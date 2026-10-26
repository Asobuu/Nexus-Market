# SDD — Modelo de Dominio (NexusMarket)

Este documento describe el dominio puro de NexusMarket: clases, value objects, enums y excepciones. **Ninguna clase de este paquete (`domain/`) puede importar Spring, JPA, Jackson ni nada de infraestructura.** Las anotaciones de persistencia viven aparte, en `adapters/persistence/jpa/`, sobre clases espejo mapeadas desde/hacia estas.

## 1. Jerarquía de usuarios

**User** (abstracta): `id`, `fullName`, `email`, `status` (`UserStatus`). Método abstracto `getRoleName()`. Comportamiento: `activate()`, `block()` — controla las transiciones de `status`, nadie fuera de la clase puede asignarlo directamente.

- **Buyer** extiende User: `address` (Address), `otherAddresses` (List\<Address\>).
- **Seller** extiende User: `warehouses` (List\<Warehouse\>), `products` (List\<Product\>).
- **LogisticsOperator** extiende User: `assignedWarehouses` (List\<Warehouse\>).
- **Administrator** extiende User: sin atributos propios.
- **Supervisor** extiende User: sin atributos propios, acceso de solo lectura.

## 2. Value objects

**Address**: `street`, `city`, `country`. Inmutable — se crea completo y no cambia; si una dirección cambia, se reemplaza por una nueva instancia.

## 3. Catálogo e inventario

**Warehouse**: `id`, `name`, `location` (Address), `owner` (Seller, null si es del marketplace).

**Product**: `id`, `name`, `description`, `type` (ProductType), `price`, `seller`, `status` (ProductStatus), `variants` (List\<ProductVariant\>). Comportamiento: `publish()`, `suspend()`, `discontinue()`, `isAvailable()`, `needsShipping()`, `addVariant(variant)`.

**ProductVariant**: `id`, `product`, `color`, `size`, `model`.

**Inventory**: `id`, `product`, `warehouse`, `quantity`. Comportamiento: `reserve(amount)` (lanza `InsufficientStockException` si no alcanza), `addStock(amount)`.

**InventoryMovement**: `id`, `inventory`, `type` (MovementType), `quantity`, `occurredAt`. Registro inmutable, se crea una vez y no se modifica.

## 4. Carrito y pedido

**Cart**: `id`, `buyer`, `items` (List\<CartItem\>). Comportamiento: `addItem(item)`, `getTotal()`, `clear()`.

**CartItem**: `id`, `product`, `quantity`. `getSubtotal()`.

**Order**: `id`, `buyer`, `items` (List\<OrderItem\>), `status` (OrderStatus), `deliveryAddress`. Comportamiento: `pay()`, `ship()`, `deliver()`, `getTotal()`. Lanza `InvalidOrderStateException` si se intenta modificar un pedido en `DELIVERED`.

**OrderItem**: `id`, `product`, `quantity`, `unitPrice` (copiado del precio del producto al momento de la compra).

**Invoice**: `id`, `order`, `totalAmount`.

**Shipment**: `id`, `order`, `originWarehouse`, `operator`, `status` (ShipmentStatus). Comportamiento: `assignOperator(operator)`, `ship()`, `deliver()`.

**Return**: `id`, `order`, `product`, `reason`, `status` (ReturnStatus), `refundAmount`. Comportamiento: `approve(amount)`, `reject()`, `completeRefund()`. Lanza `ReturnNotEligibleException` si el pedido no está en `DELIVERED`.

## 5. Auditoría

**AuditLog** (vive en el dominio como modelo, se persiste en MongoDB): `id`, `eventType` (AuditEventType), `actorEmail`, `actorRole`, `description`, `occurredAt`.

## 6. Enums

- **UserStatus**: `ACTIVE`, `BLOCKED`
- **ProductType**: `PHYSICAL`, `DIGITAL`
- **ProductStatus**: `PUBLISHED`, `SUSPENDED`, `DISCONTINUED`
- **MovementType**: `RECEIPT`, `RESERVATION`, `SALE`, `ADJUSTMENT`, `RETURN`
- **OrderStatus**: `CART`, `PENDING_PAYMENT`, `PAID`, `SHIPPED`, `DELIVERED`
- **ShipmentStatus**: `PREPARING`, `SHIPPED`, `DELIVERED`
- **ReturnStatus**: `REQUESTED`, `APPROVED`, `REJECTED`, `COMPLETED`
- **AuditEventType**: `USER_REGISTERED`, `PRODUCT_PUBLISHED`, `ORDER_PAID`, `ORDER_SHIPPED`, `ORDER_DELIVERED`, `RETURN_REQUESTED`, `RETURN_APPROVED`, `RETURN_REJECTED`, `REFUND_COMPLETED`

## 7. Excepciones de dominio (`domain/exceptions/`)

- **DuplicateEmailException** — ya existe un usuario con ese correo.
- **InsufficientStockException** — no hay suficiente inventario para reservar.
- **InvalidOrderStateException** — se intenta una transición de pedido no permitida.
- **ReturnNotEligibleException** — se pide una devolución sobre un pedido que no está entregado.
- **UnauthorizedActionException** — un actor intenta algo fuera de su rol.
- **EntityNotFoundException** — se busca por id algo que no existe.
