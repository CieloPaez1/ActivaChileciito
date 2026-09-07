package com.activachilecito.usuarios.controller;

import input.EliminarUsuarioInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioDeleteController {
    
    private final EliminarUsuarioInput eliminarUsuarioInput;

    public UsuarioDeleteController(EliminarUsuarioInput eliminarUsuarioInput) {
        this.eliminarUsuarioInput = eliminarUsuarioInput;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarUsuarioInput.eliminarUsuario(id);
        return ResponseEntity.noContent().build(); // Status 204
    }
}
