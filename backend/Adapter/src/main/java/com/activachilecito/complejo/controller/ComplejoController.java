package com.activachilecito.complejo.controller;

import com.activachilecito.usuarios.config.CustomUserDetails;
import input.RegistrarComplejoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import output.ComplejoResponseDTO;
import usecase.RegistrarComplejoUseCase;

@RestController
@RequestMapping("/api/complejos")
@RequiredArgsConstructor
public class ComplejoController {

    private final RegistrarComplejoUseCase registrarComplejoUseCase;

    @PostMapping
    public ResponseEntity<ComplejoResponseDTO> registrarComplejo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RegistrarComplejoRequest request) {
        
        ComplejoResponseDTO response = registrarComplejoUseCase.registrar(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
