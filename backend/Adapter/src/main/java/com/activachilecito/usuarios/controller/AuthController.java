package com.activachilecito.usuarios.controller;

import com.activachilecito.usuarios.dto.LoginRequestDTO;
import com.activachilecito.usuarios.dto.TokenResponseDTO;
import input.EjecutarRestablecimientoRequest;
import input.IniciarSesionInput;
import input.RegistrarUsuarioInput;
import input.SolicitarRestablecimientoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import usecase.CerrarSesionUseCase;
import usecase.EjecutarRestablecimientoUseCase;
import usecase.SolicitarRestablecimientoUseCase;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IniciarSesionInput iniciarSesionInput;
    private final SolicitarRestablecimientoUseCase solicitarRestablecimientoUseCase;
    private final EjecutarRestablecimientoUseCase ejecutarRestablecimientoUseCase;
    private final CerrarSesionUseCase cerrarSesionUseCase;


    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO request) {
        String token = iniciarSesionInput.iniciarSesion(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<Void> solicitarRestablecimiento(@RequestBody SolicitarRestablecimientoRequest request) {
        solicitarRestablecimientoUseCase.solicitar(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset/execute")
    public ResponseEntity<Void> ejecutarRestablecimiento(@RequestBody EjecutarRestablecimientoRequest request) {
        ejecutarRestablecimientoUseCase.ejecutar(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        cerrarSesionUseCase.cerrarSesion(token);
        return ResponseEntity.ok("Sesión cerrada correctamente");
    }
}
