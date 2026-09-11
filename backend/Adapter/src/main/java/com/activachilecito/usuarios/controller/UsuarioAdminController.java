package com.activachilecito.usuarios.controller;

import input.ModificarUsuarioAdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usecase.ModificarUsuarioAdminUseCase;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioAdminController {

    private final ModificarUsuarioAdminUseCase modificarUsuarioAdminUseCase;

    @PutMapping("/{id}")
    public ResponseEntity<Void> modificarUsuario(@PathVariable Long id, @RequestBody ModificarUsuarioAdminRequest request) {
        modificarUsuarioAdminUseCase.modificar(id, request);
        return ResponseEntity.ok().build();
    }
}
