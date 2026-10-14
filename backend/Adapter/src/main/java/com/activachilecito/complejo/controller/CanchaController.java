package com.activachilecito.complejo.controller;

import com.activachilecito.usuarios.config.CustomUserDetails;
import input.RegistrarCanchaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import output.CanchaResponseDTO;
import usecase.ObtenerCanchasPorComplejoUseCase;
import usecase.RegistrarCanchaUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/canchas")
@RequiredArgsConstructor
public class CanchaController {

    private final RegistrarCanchaUseCase registrarCanchaUseCase;
    private final ObtenerCanchasPorComplejoUseCase obtenerCanchasPorComplejoUseCase;

    @PostMapping
    @PreAuthorize("hasRole('DUENO_DE_COMPLEJO')")
    public ResponseEntity<CanchaResponseDTO> registrarCancha(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RegistrarCanchaRequest request) {

        CanchaResponseDTO response = registrarCanchaUseCase.registrar(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/complejo/{complejoId}")
    public ResponseEntity<List<CanchaResponseDTO>> obtenerCanchasPorComplejo(
            @PathVariable Long complejoId) {

        List<CanchaResponseDTO> canchas = obtenerCanchasPorComplejoUseCase.obtenerPorComplejo(complejoId);
        return ResponseEntity.ok(canchas);
    }

    @GetMapping("/mis-canchas")
    @PreAuthorize("hasRole('DUENO_DE_COMPLEJO')")
    public ResponseEntity<List<CanchaResponseDTO>> obtenerMisCanchas(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<CanchaResponseDTO> canchas = obtenerCanchasPorComplejoUseCase.obtenerPorDueno(userDetails.getId());
        return ResponseEntity.ok(canchas);
    }
}
