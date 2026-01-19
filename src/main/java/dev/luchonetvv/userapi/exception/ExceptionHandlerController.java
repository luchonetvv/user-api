package dev.luchonetvv.userapi.exception;

import javax.servlet.http.HttpServletResponse;

import dev.luchonetvv.userapi.domain.model.ErrorMessageResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = { ValidateUserException.class })
    public ResponseEntity<ErrorMessageResponse> handleValidateUserException(ValidateUserException e) {
        HttpStatus status = "El correo ya registrado".equals(e.getMessage())
                ? HttpStatus.CONFLICT
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(new ErrorMessageResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .filter(fe -> "email".equals(fe.getField()))
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElseGet(() -> e.getBindingResult().getFieldErrors().stream()
                        .filter(fe -> "password".equals(fe.getField()))
                        .findFirst()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .orElseGet(() -> e.getBindingResult().getFieldErrors().stream()
                                .findFirst()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .orElse("Solicitud inválida")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponse(msg));
    }
    
    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<ErrorMessageResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessageResponse("Acceso denegado"));
    }

    @ExceptionHandler(value = { HttpServerErrorException.class })
    public ResponseEntity<ErrorMessageResponse> handleHttpServerErrorException(HttpServerErrorException ex, HttpServletResponse res) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorMessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(value = { InsufficientAuthenticationException.class })
    public ResponseEntity<ErrorMessageResponse> handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageResponse("Autenticación insuficiente"));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorMessageResponse> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE) // 415
                .body(new ErrorMessageResponse("Content-Type no soportado."));
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ErrorMessageResponse> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageResponse("Error interno" + ex.getClass().getSimpleName() + ": " + ex.getMessage()));
    }
}
