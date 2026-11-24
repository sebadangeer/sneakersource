package com.GestionSNKR.SnearksSource.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.GestionSNKR.SnearksSource.DTO.ErrorResponse;

@RestControllerAdvice
public class AuthExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(AuthExceptionHandler.class);

    @ExceptionHandler(GoogleAuthException.class)
    public ResponseEntity<ErrorResponse> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("No autorizado"));
    }

    @ExceptionHandler(GoogleConflictException.class)
    public ResponseEntity<ErrorResponse> conflict() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Conflicto de identidad"));
    }

    @ExceptionHandler(GoogleConfigurationException.class)
    public ResponseEntity<ErrorResponse> configuration() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Error interno"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> invalidPathParameter(MethodArgumentTypeMismatchException exception) {
        log.warn("Parámetro de ruta inválido: {}={}", exception.getName(), exception.getValue());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("El identificador recibido no es válido"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> internal(Exception exception) {
        log.error("Error no controlado en una API", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Error interno"));
    }
}
