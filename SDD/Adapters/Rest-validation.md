# SDD — Validación REST (Jakarta Bean Validation)

Reglas mínimas por campo en los Request DTOs:

- `fullName`: `@NotBlank`, `@Size(max = 120)`
- `email`: `@NotBlank`, `@Email`
- `rawPassword`: `@NotBlank`, `@Size(min = 8)`
- `price`: `@Positive`
- `quantity` / `initialStock` / `amount`: `@Min(1)`
- `street`, `city`, `country` (dentro de `AddressRequest`): `@NotBlank`
- `reason` (en `RequestReturnRequest`): `@NotBlank`, `@Size(max = 300)`

Los errores de validación los captura `@RestControllerAdvice` (ver `Global-exception-handler.md`) y devuelven `400` con el detalle de cada campo.
