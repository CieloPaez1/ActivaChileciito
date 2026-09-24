package com.activachilecito.usuarios.controller;

import com.activachilecito.usuarios.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import output.PerfilResponseDTO;
import usecase.VisualizarPerfilUseCase;

import input.CambiarContrasenaRequest;
import usecase.CambiarContrasenaUseCase;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import input.ModificarPerfilRequest;
import usecase.ModificarPerfilUseCase;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class UsuarioController {

    private final VisualizarPerfilUseCase visualizarPerfilUseCase;
    private final ModificarPerfilUseCase modificarPerfilUseCase;
    private final CambiarContrasenaUseCase cambiarContrasenaUseCase;

    @GetMapping("/me")
    public ResponseEntity<PerfilResponseDTO> visualizarPerfil(@AuthenticationPrincipal CustomUserDetails userDetails) {
        PerfilResponseDTO perfil = visualizarPerfilUseCase.visualizar(userDetails.getId());
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/me")
    public ResponseEntity<PerfilResponseDTO> modificarPerfil(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ModificarPerfilRequest request) {
        PerfilResponseDTO perfilActualizado = modificarPerfilUseCase.modificar(userDetails.getId(), request);
        return ResponseEntity.ok(perfilActualizado);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> cambiarContrasena(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CambiarContrasenaRequest request) {
        cambiarContrasenaUseCase.cambiarContrasena(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }
}
