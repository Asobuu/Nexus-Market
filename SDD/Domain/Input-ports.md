# SDD — Puertos de Entrada (Input Ports)

Cada interfaz vive en `domain/ports/in/`. Son el único punto de entrada al dominio desde afuera — los adaptadores de casos de uso (`adapters/useCases/`) las implementan, y los controladores REST solo conocen estas interfaces, nunca los servicios de dominio directamente.

## PublicAccessPort
Acciones sin autenticación.

```java
Buyer registerBuyer(String fullName, String email, String rawPassword, Address address);
AuthToken login(String email, String rawPassword);
List<Product> listPublishedProducts();
```

## BuyerPort
Requiere sesión con rol `BUYER`.

```java
Cart addToCart(Long buyerId, Long productId, int quantity);
Cart viewCart(Long buyerId);
Order checkout(Long buyerId, Address deliveryAddress);
List<Order> viewMyOrders(Long buyerId);
Return requestReturn(Long buyerId, Long orderId, Long productId, String reason);
```

## SellerPort
Requiere sesión con rol `SELLER`.

```java
Product publishProduct(Long sellerId, String name, String description, ProductType type,
                        double price, Long warehouseId, int initialStock);
ProductVariant addVariant(Long sellerId, Long productId, String color, String size, String model);
Inventory addStock(Long sellerId, Long inventoryId, int amount);
Product suspendProduct(Long sellerId, Long productId);
Product discontinueProduct(Long sellerId, Long productId);
List<Product> viewMyProducts(Long sellerId);
```

## LogisticsOperatorPort
Requiere sesión con rol `LOGISTICS_OPERATOR`.

```java
Shipment assignSelf(Long operatorId, Long shipmentId);
Shipment markShipped(Long operatorId, Long shipmentId);
Shipment markDelivered(Long operatorId, Long shipmentId);
```

## AdministratorPort
Requiere sesión con rol `ADMINISTRATOR`.

```java
Seller registerSeller(Long adminId, String fullName, String email, String rawPassword);
LogisticsOperator registerLogisticsOperator(Long adminId, String fullName, String email, String rawPassword);
Warehouse registerWarehouse(Long adminId, String name, Address location, Long ownerSellerId);
Return approveReturn(Long adminId, Long returnId, double refundAmount);
Return rejectReturn(Long adminId, Long returnId);
Return completeRefund(Long adminId, Long returnId);
```

## SupervisorPort
Requiere sesión con rol `SUPERVISOR`. Solo lectura.

```java
double getTotalSales();
long countPendingOrders();
long countProductsByStatus(ProductStatus status);
List<Inventory> getLowStock(int threshold);
```
