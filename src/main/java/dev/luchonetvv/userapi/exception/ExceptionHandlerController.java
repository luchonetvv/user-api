package dev.luchonetvv.userapi.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.validation.FieldError;

import dev.luchonetvv.userapi.domain.model.ApiError;
import dev.luchonetvv.userapi.domain.model.ApiResponse;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = { ValidateUserException.class })
    public ResponseEntity<ApiResponse<Object>> handleValidateUserException(ValidateUserException e) {
        return new ResponseEntity<>(buildResponseValidException(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(buildResponseValidException(e), HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(buildResponseException(ex, "Acceso denegado"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = { HttpServerErrorException.class })
    public ResponseEntity<ApiResponse<Object>> handleHttpServerErrorException(HttpServerErrorException ex, HttpServletResponse res) {
        return new ResponseEntity<>(buildResponseException(ex, ex.getMessage()), ex.getStatusCode());
    }

    @ExceptionHandler(value = { InsufficientAuthenticationException.class })
    public ResponseEntity<ApiResponse<Object>> handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
        return new ResponseEntity<>(buildResponseException(ex, "Autenticacion Insuficiente"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        return new ResponseEntity<>(buildResponseException(ex, "Algo salio mal"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiResponse<Object> buildResponseException(Throwable throwable, String message) {
        ApiResponse<Object> response = new ApiResponse<>();
        ApiError error = new ApiError();

        error.setCauseMessage(throwable.getMessage());
        error.setMessage(message);

        response.setError(error);
        response.setMessage(message);

        return response;
    }

    private ApiResponse<Object> buildResponseValidException(MethodArgumentNotValidException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        response.setMessage(errors);

        return response;
    }

    private ApiResponse<Object> buildResponseValidException(String message) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setMessage(message);
        return response;
    }
}
