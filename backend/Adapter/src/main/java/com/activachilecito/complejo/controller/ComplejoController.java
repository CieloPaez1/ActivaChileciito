package com.activachilecito.complejo.controller;

import com.activachilecito.usuarios.config.CustomUserDetails;
import input.RegistrarComplejoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import output.ComplejoResponseDTO;
import usecase.ModificarComplejoUseCase;
import usecase.ObtenerMiComplejoUseCase;
import usecase.RegistrarComplejoUseCase;

@RestController
@RequestMapping("/api/complejos")
@RequiredArgsConstructor
public class ComplejoController {

    private final RegistrarComplejoUseCase registrarComplejoUseCase;
    private final ObtenerMiComplejoUseCase obtenerMiComplejoUseCase;
    private final ModificarComplejoUseCase modificarComplejoUseCase;

    @PostMapping
    public ResponseEntity<ComplejoResponseDTO> registrarComplejo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RegistrarComplejoRequest request) {
        ComplejoResponseDTO response = registrarComplejoUseCase.registrar(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/mi-complejo")
    public ResponseEntity<ComplejoResponseDTO> obtenerMiComplejo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return obtenerMiComplejoUseCase.obtener(userDetails.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PutMapping("/mi-complejo")
    public ResponseEntity<ComplejoResponseDTO> modificarMiComplejo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RegistrarComplejoRequest request) {
        ComplejoResponseDTO response = modificarComplejoUseCase.modificar(userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }
}
