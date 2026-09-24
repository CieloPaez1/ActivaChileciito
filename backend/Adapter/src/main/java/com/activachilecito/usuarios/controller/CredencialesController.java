package com.activachilecito.usuarios.controller;

import input.VisualizarCredencialesInput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import output.CredencialesDTO;

@RestController
@RequestMapping("/api/credenciales")
@RequiredArgsConstructor
public class CredencialesController {

    private final VisualizarCredencialesInput visualizarCredencialesInput;

    @GetMapping
    public ResponseEntity<CredencialesDTO> visualizarCredenciales() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Long idUsuario = extraerIdDeAutenticacion(authentication);
        CredencialesDTO credenciales = visualizarCredencialesInput.visualizar(idUsuario);
        
        return ResponseEntity.ok(credenciales);
    }

    private Long extraerIdDeAutenticacion(Authentication authentication) {
        // Retornamos 1L como ID de ejemplo simulando la extracciÃ³n del Principal
        return 1L;
    }
}
