# NexusMarket — Diseño de Clases

Documento del proyecto de NexusMarket (marketplace), a partir del documento guía del curso. El código vive en `src/main/java/Application`, con nombres de clases/atributos/métodos en inglés (por la rúbrica) y comentarios en español.

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
- Una devolución solo se puede pedir sobre un pedido que ya fue entregado.

### Persistencia

Todas las entidades están anotadas con JPA (`@Entity`) y cada una tiene su repositorio (`JpaRepository`) en el paquete `repository`, para guardarlas en MySQL con Hibernate. La configuración de la base de datos está en `src/main/resources/application.properties`.

> Aviso: esta parte la armé siguiendo la documentación de Spring Data JPA, pero no la pude compilar yo mismo porque en el entorno donde escribí el código no tenía acceso a internet para descargar las dependencias de Maven. Recomiendo correr `mvn compile` o `mvn spring-boot:run` en tu máquina antes de entregar, por si hay que ajustar algo.

## 2. Clases del modelo (entidades)

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

**Invoice**: se genera cuando el pedido se paga; guarda el total del pedido en ese momento.

**Shipment**: el envío físico de un pedido (solo aplica a productos físicos). Tiene la bodega de origen, el operador asignado y su propio estado (PREPARING, SHIPPED, DELIVERED).

**Return**: una solicitud de devolución de un producto de un pedido. El reembolso (`refundAmount`) se guarda en la misma clase, no hice una clase `Refund` aparte.

## 3. Capa de servicios

Además de las entidades, agregué una capa de servicios (`Application.service`) con la lógica de negocio que conecta varias clases entre sí. Cada servicio usa los repositorios correspondientes y, cuando aplica, llama a los métodos que ya tienen las entidades (`pay()`, `ship()`, `reserve()`, etc.) en vez de repetir esa lógica.

**UserService**: registrar compradores y vendedores.

**ProductService**: publicar un producto nuevo y crearle su inventario inicial en una bodega (si es físico); agregar más stock.

**CartService**: agregar productos al carrito; `checkout()` convierte el carrito en un pedido, reservando el stock de cada producto y dejando el carrito vacío.

**OrderService**: marcar un pedido como pagado (y generar su factura), crear el envío y marcarlo como entregado.

**ShipmentService**: asignar un operador logístico a un envío.

**ReturnService**: pedir una devolución, aprobarla o rechazarla, y completar el reembolso.

## 4. Estructura del proyecto

```
NexusM/
├── docs/documentation.md
├── pom.xml
└── src/main/java/Application/
    ├── NexusMApplication.java
    ├── enums/
    │   ├── UserStatus.java
    │   ├── ProductType.java
    │   ├── OrderStatus.java
    │   ├── ShipmentStatus.java
    │   └── ReturnStatus.java
    ├── model/
    │   ├── user/
    │   │   ├── Address.java
    │   │   ├── User.java
    │   │   ├── Buyer.java
    │   │   ├── Seller.java
    │   │   ├── LogisticsOperator.java
    │   │   ├── Administrator.java
    │   │   └── Supervisor.java
    │   ├── Warehouse.java
    │   ├── Product.java
    │   ├── Inventory.java
    │   ├── Cart.java
    │   ├── CartItem.java
    │   ├── Order.java
    │   ├── OrderItem.java
    │   ├── Invoice.java
    │   ├── Shipment.java
    │   └── Return.java
    ├── repository/
    │   └── (un JpaRepository por cada entidad)
    ├── service/
    │   ├── UserService.java
    │   ├── ProductService.java
    │   ├── CartService.java
    │   ├── OrderService.java
    │   ├── ShipmentService.java
    │   └── ReturnService.java
    └── demo/
        └── MarketplaceDemo.java   (crea datos de prueba al arrancar la app)
```

## 5. Lo que falta por agregar

- **Controladores REST**: exponer los servicios como endpoints (`@RestController`) para poder probarlos desde Postman o un frontend.
- **Seguridad**: login y permisos por rol (el proyecto ya trae `spring-boot-starter-security` en el `pom.xml`, pero todavía no está configurado).
- **Pruebas unitarias** de los servicios.
