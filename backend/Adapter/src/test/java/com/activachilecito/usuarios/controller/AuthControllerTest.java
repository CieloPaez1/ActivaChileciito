package com.activachilecito.usuarios.controller;

import com.activachilecito.usuarios.dto.LoginRequestDTO;
import com.activachilecito.usuarios.dto.TokenResponseDTO;
import input.EjecutarRestablecimientoRequest;
import input.IniciarSesionInput;
import input.SolicitarRestablecimientoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import usecase.CerrarSesionUseCase;
import usecase.EjecutarRestablecimientoUseCase;
import usecase.SolicitarRestablecimientoUseCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private IniciarSesionInput iniciarSesionInput;
    @Mock
    private SolicitarRestablecimientoUseCase solicitarRestablecimientoUseCase;
    @Mock
    private EjecutarRestablecimientoUseCase ejecutarRestablecimientoUseCase;
    @Mock
    private CerrarSesionUseCase cerrarSesionUseCase;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_ConCredencialesValidas_DevuelveTokenYStatus200() {
        LoginRequestDTO request = new LoginRequestDTO("usuario@test.com", "Password123!");
        String tokenSimulado = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        
        when(iniciarSesionInput.iniciarSesion(request.getEmail(), request.getPassword())).thenReturn(tokenSimulado);

        ResponseEntity<TokenResponseDTO> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tokenSimulado, response.getBody().getToken());
        verify(iniciarSesionInput).iniciarSesion(request.getEmail(), request.getPassword());
    }

    @Test
    void solicitarRestablecimiento_LlamaAUsoDeCasoYDevuelve200() {
        SolicitarRestablecimientoRequest request = new SolicitarRestablecimientoRequest();
        request.setEmail("usuario@test.com");

        ResponseEntity<Void> response = authController.solicitarRestablecimiento(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(solicitarRestablecimientoUseCase).solicitar(request);
    }

    @Test
    void ejecutarRestablecimiento_LlamaAUsoDeCasoYDevuelve200() {
        EjecutarRestablecimientoRequest request = new EjecutarRestablecimientoRequest();
        request.setToken("token-123");
        request.setNuevaClave("NuevaClave123");

        ResponseEntity<Void> response = authController.ejecutarRestablecimiento(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ejecutarRestablecimientoUseCase).ejecutar(request);
    }

    @Test
    void logout_ConToken_LlamaAUsoDeCasoYDevuelve200() {
        String token = "Bearer token-123";

        ResponseEntity<String> response = authController.logout(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sesión cerrada correctamente", response.getBody());
        verify(cerrarSesionUseCase).cerrarSesion(token);
    }
}
