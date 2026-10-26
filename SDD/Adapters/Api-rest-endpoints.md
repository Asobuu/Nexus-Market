# SDD — Endpoints REST

Prefijo base: `/api`. Todas las rutas salvo `/api/auth/**` y `GET /api/products` requieren JWT (`Authorization: Bearer <token>`).

## Público

| Método | Ruta | Rol | Body | Respuesta |
|---|---|---|---|---|
| POST | `/api/auth/register` | ninguno | `RegisterBuyerRequest` | `201` `BuyerResponse` |
| POST | `/api/auth/login` | ninguno | `LoginRequest` | `200` `AuthTokenResponse` |
| GET | `/api/products` | ninguno | — | `200` `List<ProductResponse>` |

## Buyer

| Método | Ruta | Body | Respuesta |
|---|---|---|---|
| POST | `/api/cart/items` | `AddToCartRequest` | `200` `CartResponse` |
| GET | `/api/cart` | — | `200` `CartResponse` |
| POST | `/api/cart/checkout` | `CheckoutRequest` | `201` `OrderResponse` |
| GET | `/api/orders/mine` | — | `200` `List<OrderResponse>` |
| POST | `/api/returns` | `RequestReturnRequest` | `201` `ReturnResponse` |

## Seller

| Método | Ruta | Body | Respuesta |
|---|---|---|---|
| POST | `/api/products` | `PublishProductRequest` | `201` `ProductResponse` |
| POST | `/api/products/{id}/variants` | `AddVariantRequest` | `201` `ProductVariantResponse` |
| POST | `/api/inventory/{id}/receipt` | `AddStockRequest` | `200` `InventoryResponse` |
| PATCH | `/api/products/{id}/status` | `ChangeProductStatusRequest` | `200` `ProductResponse` |
| GET | `/api/products/mine` | — | `200` `List<ProductResponse>` |

## Logistics Operator

| Método | Ruta | Respuesta |
|---|---|---|
| POST | `/api/shipments/{id}/assign-self` | `200` `ShipmentResponse` |
| POST | `/api/shipments/{id}/ship` | `200` `ShipmentResponse` |
| POST | `/api/shipments/{id}/deliver` | `200` `ShipmentResponse` |

## Administrator

| Método | Ruta | Body | Respuesta |
|---|---|---|---|
| POST | `/api/admin/sellers` | `RegisterSellerRequest` | `201` `SellerResponse` |
| POST | `/api/admin/logistics-operators` | `RegisterOperatorRequest` | `201` `LogisticsOperatorResponse` |
| POST | `/api/admin/warehouses` | `RegisterWarehouseRequest` | `201` `WarehouseResponse` |
| POST | `/api/returns/{id}/approve` | `ApproveReturnRequest` | `200` `ReturnResponse` |
| POST | `/api/returns/{id}/reject` | — | `200` `ReturnResponse` |

## Supervisor

| Método | Ruta | Respuesta |
|---|---|---|
| GET | `/api/reports/sales` | `200` `{ totalSales }` |
| GET | `/api/reports/orders/pending` | `200` `{ count }` |
| GET | `/api/reports/products/status?status=` | `200` `{ count }` |
| GET | `/api/reports/inventory/low-stock?threshold=` | `200` `List<InventoryResponse>` |
