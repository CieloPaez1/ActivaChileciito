package com.activachilecito.controller;

import com.activachilecito.core.dto.ActualizarPerfilRequest;
import com.activachilecito.core.dto.CambiarPasswordRequest;
import com.activachilecito.core.dto.UsuarioResponse;
import com.activachilecito.core.usecase.UsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> obtenerMiPerfil(Authentication authentication) {
        return ResponseEntity.ok(usuarioUseCase.obtenerMiPerfil(authentication.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> actualizarMiPerfil(
            Authentication authentication,
            @RequestBody ActualizarPerfilRequest request
    ) {
        return ResponseEntity.ok(usuarioUseCase.actualizarMiPerfil(authentication.getName(), request));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> cambiarMiPassword(
            Authentication authentication,
            @RequestBody CambiarPasswordRequest request
    ) {
        usuarioUseCase.cambiarMiPassword(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }
}
