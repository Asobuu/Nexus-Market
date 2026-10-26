# SDD — Manejo Global de Excepciones

`@RestControllerAdvice` en `adapters/rest/exception/GlobalExceptionHandler.java`, mapea cada excepción de dominio a un código HTTP:

| Excepción | HTTP |
|---|---|
| `DuplicateEmailException` | 409 Conflict |
| `InsufficientStockException` | 409 Conflict |
| `InvalidOrderStateException` | 409 Conflict |
| `ReturnNotEligibleException` | 409 Conflict |
| `EntityNotFoundException` | 404 Not Found |
| `UnauthorizedActionException` | 403 Forbidden |
| `MethodArgumentNotValidException` (validación Bean Validation) | 400 Bad Request |
| `AuthenticationException` / credenciales inválidas | 401 Unauthorized |
| Cualquier otra excepción no controlada | 500 Internal Server Error |

Todas las respuestas de error siguen el mismo formato:

```json
{
  "timestamp": "...",
  "status": 409,
  "error": "INSUFFICIENT_STOCK",
  "message": "no hay suficiente stock de Wireless Mouse"
}
```
