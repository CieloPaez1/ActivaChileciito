package com.activachilecito.usuarios.config;

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

    // Un "catch-all" para cualquier otro error inesperado (el equivalente a Exception e)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErroresInesperados(Exception e) {
        // En producción, aquí normalmente guardarías el error en un log
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "Error interno del servidor. Por favor, intente más tarde."));
    }
}
