package com.activachilecito.complejo.controller;

import com.activachilecito.usuarios.config.CustomUserDetails;
import input.RegistrarComplejoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import output.ComplejoResponseDTO;
import usecase.GestionarComplejoUseCase;
import usecase.RegistrarComplejoUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/complejos")
@RequiredArgsConstructor
public class ComplejoController {

    private final RegistrarComplejoUseCase registrarComplejoUseCase;
    private final GestionarComplejoUseCase gestionarComplejoUseCase;

    @PostMapping
    @PreAuthorize("hasRole('DUENO_DE_COMPLEJO')")
    public ResponseEntity<ComplejoResponseDTO> registrarComplejo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RegistrarComplejoRequest request) {
        
        ComplejoResponseDTO response = registrarComplejoUseCase.registrar(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/mi-complejo")
    @PreAuthorize("hasRole('DUENO_DE_COMPLEJO')")
    public ResponseEntity<ComplejoResponseDTO> obtenerMiComplejo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return gestionarComplejoUseCase.obtenerPorDueno(userDetails.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PutMapping("/mi-complejo")
    @PreAuthorize("hasRole('DUENO_DE_COMPLEJO')")
    public ResponseEntity<ComplejoResponseDTO> actualizarMiComplejo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RegistrarComplejoRequest request) {

        ComplejoResponseDTO response = gestionarComplejoUseCase.actualizarOCrearPorDueno(userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ComplejoResponseDTO>> listarTodos() {
        List<ComplejoResponseDTO> complejos = gestionarComplejoUseCase.obtenerTodos();
        return ResponseEntity.ok(complejos);
    }
}
