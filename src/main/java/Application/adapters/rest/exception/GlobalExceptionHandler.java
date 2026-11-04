package Application.adapters.rest.exception;

import Application.adapters.rest.dto.response.ErrorResponse;
import Application.domain.exceptions.AccountBlockedException;
import Application.domain.exceptions.DuplicateEmailException;
import Application.domain.exceptions.EntityNotFoundException;
import Application.domain.exceptions.InsufficientStockException;
import Application.domain.exceptions.InvalidCredentialsException;
import Application.domain.exceptions.InvalidOrderStateException;
import Application.domain.exceptions.InvalidShipmentStateException;
import Application.domain.exceptions.InvalidWarehouseException;
import Application.domain.exceptions.ReturnNotEligibleException;
import Application.domain.exceptions.UnauthorizedActionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

// un solo formato de error para toda la API (ver Global-exception-handler.md).
// InvalidShipmentStateException e InvalidWarehouseException no estaban en
// la tabla original del SDD porque se agregaron despues (ver
// WarehouseOwnershipValidator y el guard de Shipment): se clasifican con
// el mismo criterio que InvalidOrderStateException/ReturnNotEligibleException,
// porque son el mismo tipo de problema (una transicion o una regla de
// negocio invalida, no un dato que no existe ni un permiso)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException exception) {
        return conflict("DUPLICATE_EMAIL", exception);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException exception) {
        return conflict("INSUFFICIENT_STOCK", exception);
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrderState(InvalidOrderStateException exception) {
        return conflict("INVALID_ORDER_STATE", exception);
    }

    @ExceptionHandler(InvalidShipmentStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidShipmentState(InvalidShipmentStateException exception) {
        return conflict("INVALID_SHIPMENT_STATE", exception);
    }

    @ExceptionHandler(InvalidWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleInvalidWarehouse(InvalidWarehouseException exception) {
        return conflict("INVALID_WAREHOUSE", exception);
    }

    @ExceptionHandler(ReturnNotEligibleException.class)
    public ResponseEntity<ErrorResponse> handleReturnNotEligible(ReturnNotEligibleException exception) {
        return conflict("RETURN_NOT_ELIGIBLE", exception);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException exception) {
        return build(HttpStatus.NOT_FOUND, "ENTITY_NOT_FOUND", exception.getMessage());
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAction(UnauthorizedActionException exception) {
        return build(HttpStatus.FORBIDDEN, "UNAUTHORIZED_ACTION", exception.getMessage());
    }

    // cuenta bloqueada: existe y la contraseña puede ser correcta, pero no
    // puede entrar - es un problema de permiso, no de credenciales (esa es
    // InvalidCredentialsException, que va a 401)
    @ExceptionHandler(AccountBlockedException.class)
    public ResponseEntity<ErrorResponse> handleAccountBlocked(AccountBlockedException exception) {
        return build(HttpStatus.FORBIDDEN, "ACCOUNT_BLOCKED", exception.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException exception) {
        return build(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ErrorResponse body = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "VALIDATION_ERROR",
                "uno o mas campos no son validos", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "ocurrio un error inesperado, intenta de nuevo mas tarde");
    }

    private ResponseEntity<ErrorResponse> conflict(String error, RuntimeException exception) {
        return build(HttpStatus.CONFLICT, error, exception.getMessage());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), error, message));
    }
}
