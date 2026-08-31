# NexusMarket — Diseño de Clases

Documento del proyecto de NexusMarket (marketplace), a partir del documento guía del curso. El código vive en `src/main/java/Application`, con nombres de clases/atributos/métodos en inglés (por la rúbrica) y comentarios en español. Este documento se irá actualizando a medida que se agreguen más clases (facturación, envíos, devoluciones, etc.).

## 1. ¿Qué es NexusMarket?

Es una plataforma que conecta compradores y vendedores. Según el documento guía, debe manejar: usuarios, vendedores, compradores, bodegas, catálogo de productos, inventario, carrito de compras, pedidos, facturación, envíos y devoluciones.

### Roles de usuario

| Rol | Qué hace |
|---|---|
| Comprador (`Buyer`) | Compra productos: arma su carrito y hace pedidos. |
| Vendedor (`Seller`) | Publica y administra sus propios productos y bodegas. |
| Operador Logístico (`LogisticsOperator`) | Se encarga de empacar y despachar los envíos. |
| Administrador (`Administrator`) | Registra vendedores y bodegas, aprueba reembolsos. |
| Supervisor (`Supervisor`) | Solo consulta información, no puede modificar nada. |

### Reglas principales que tuve en cuenta

- Cada usuario tiene un solo rol (por eso uso herencia: cada objeto es de una sola subclase).
- El inventario nunca puede quedar en negativo.
- Un pedido ya entregado no se puede volver a modificar.

### Persistencia

Las clases ya están anotadas como entidades JPA (`@Entity`), pensando en que más adelante se conecten a una base de datos con Hibernate, sin tener que reescribirlas. Por ahora no incluyen todavía los repositorios (`JpaRepository`) para guardarlas — eso se agrega en un commit aparte, cuando esté esa parte lista.

## 2. Clases hechas hasta ahora

**User (abstracta)**: id, fullName, email, status. Es la clase base de todos los usuarios.

**Buyer**: address, otherAddresses. Puede armar carritos y hacer pedidos.

**Seller**: warehouses, products. Lo crea un administrador, no se registra solo.

**LogisticsOperator**: assignedWarehouses. Maneja los envíos.

**Administrator**: no tiene atributos propios, hereda todo de User.

**Supervisor**: igual que Administrator, solo hereda de User (rol de consulta).

**Address**: street, city, country. La usan Buyer y Warehouse.

**Warehouse**: id, name, location, owner (el vendedor dueño, o null si es del marketplace).

**Product**: id, name, description, type (PHYSICAL o DIGITAL), price, published.

**Inventory**: product, warehouse, quantity. Une un producto con una bodega. El método `reserve()` no deja que la cantidad quede negativa.

**Cart**: buyer, items (lista de CartItem). Método `getTotal()` suma los subtotales.

**CartItem**: product, quantity.

**Order**: id, buyer, items (OrderItem), status, deliveryAddress. Pasa por los estados CART → PENDING_PAYMENT → PAID → SHIPPED → DELIVERED, y una vez en DELIVERED no se puede modificar más.

**OrderItem**: product, quantity, unitPrice (se copia el precio del producto en el momento de la compra).

## 3. Estructura del proyecto (hasta ahora)

```
NexusM/
├── docs/documentation.md            (este documento)
└── src/main/java/Application/
    ├── enums/
    │   ├── UserStatus.java
    │   ├── ProductType.java
    │   ├── OrderStatus.java
    │   ├── ShipmentStatus.java
    │   └── ReturnStatus.java
    └── model/
        ├── user/
        │   ├── Address.java
        │   ├── User.java
        │   ├── Buyer.java
        │   ├── Seller.java
        │   ├── LogisticsOperator.java
        │   ├── Administrator.java
        │   └── Supervisor.java
        ├── Warehouse.java
        ├── Product.java
        ├── Inventory.java
        ├── Cart.java
        ├── CartItem.java
        ├── Order.java
        └── OrderItem.java
```

## 4. Lo que falta por agregar

- **Facturación**: `Invoice.java`
- **Logística**: `Shipment.java`
- **Devoluciones**: `Return.java`
- **Persistencia**: repositorios (`JpaRepository`) para guardar todo en una base de datos, y su configuración
- Pruebas / clase de ejemplo que use todo junto
