package com.activachilecito.config;

import com.activachilecito.core.complejo.exception.ComplejoException;
import exception.ExcepcionUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class ManejadorExcepsionesGlobal {
    // Le decimos qué excepción específica debe atrapar este método
    @ExceptionHandler(ExcepcionUsuario.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionUsuario(ExcepcionUsuario e) {
        // Devolvemos el error 400 (Bad Request) con el mensaje de tu regla de negocio
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(ComplejoException.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionComplejo(ComplejoException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(exception.ExcepcionUsuarioNoEncontrado.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionUsuarioNoEncontrado(exception.ExcepcionUsuarioNoEncontrado e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(exception.ExcepcionCredencialesInvalidas.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionCredencialesInvalidas(exception.ExcepcionCredencialesInvalidas e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<Map<String, String>> manejarExcepcionAutenticacion(org.springframework.security.core.AuthenticationException e) {
        String mensaje = e.getMessage();
        if (mensaje != null && mensaje.equals("Bad credentials")) {
            mensaje = "Credenciales incorrectas";
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("error", mensaje));
    }

    // Un "catch-all" para cualquier otro error inesperado (el equivalente a Exception e)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErroresInesperados(Exception e) {
        // En producción, aquí normalmente guardarías el error en un log
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "Error interno del servidor. Por favor, intente más tarde."));
    }
}
