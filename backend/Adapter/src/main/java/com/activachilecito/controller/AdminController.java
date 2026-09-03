package com.activachilecito.controller;

import com.activachilecito.core.dto.ActualizarRolesRequest;
import com.activachilecito.core.dto.UsuarioResponse;
import com.activachilecito.core.model.EstadoUsuario;
import com.activachilecito.core.usecase.AdminUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPERUSUARIO')")
public class AdminController {

    private final AdminUseCase adminUseCase;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(adminUseCase.listarUsuarios());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<UsuarioResponse> cambiarEstadoUsuario(
            @PathVariable Long id,
            @RequestParam EstadoUsuario estado
    ) {
        return ResponseEntity.ok(adminUseCase.cambiarEstadoUsuario(id, estado));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<UsuarioResponse> actualizarRoles(
            @PathVariable Long id,
            @RequestBody ActualizarRolesRequest request
    ) {
        return ResponseEntity.ok(adminUseCase.actualizarRoles(id, request));
    }
}
