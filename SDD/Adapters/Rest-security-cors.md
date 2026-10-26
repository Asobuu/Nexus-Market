# SDD — Seguridad REST y CORS

## JWT
- Claims: `sub` (id del usuario), `role`, `iat`, `exp`.
- Header: `Authorization: Bearer <token>`.
- Expiración por defecto: 1 hora (`jwt.expiration-ms`).

## Spring Security
- `SecurityFilterChain` sin sesión (`STATELESS`).
- `JwtAuthenticationFilter` antes de `UsernamePasswordAuthenticationFilter`: valida el token, carga el usuario por `sub` vía `UserRepositoryPort`, rechaza si no existe o `status != ACTIVE`.
- Autorización por rol con `@PreAuthorize` o reglas en el `SecurityFilterChain` según la tabla de `Api-rest-endpoints.md` (por ejemplo, todo `/api/admin/**` requiere rol `ADMINISTRATOR`).
- `/api/auth/**` y `GET /api/products` son públicas.

## CORS
- Orígenes permitidos en desarrollo: `http://localhost:3000` (ajustar según el frontend que se use).
- Métodos: `GET, POST, PATCH, DELETE, OPTIONS`.
- Headers permitidos: `Authorization, Content-Type, X-Request-Id`.
- Cada request/response agrega `X-Request-Id` (generado si no viene) para trazabilidad.
