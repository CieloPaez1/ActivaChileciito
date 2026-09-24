package com.activachilecito.usuarios.controller;

import input.VisualizarCredencialesInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import output.CredencialesDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CredencialesControllerTest {

    @Mock
    private VisualizarCredencialesInput visualizarCredencialesInput;

    @InjectMocks
    private CredencialesController credencialesController;

    @Test
    void visualizarCredenciales_DevuelveCredencialesDTOYStatus200() {
        // Arrange
        Long idUsuarioSimulado = 1L; // Hardcoded en el controlador para este ejemplo
        CredencialesDTO credencialesDTO = new CredencialesDTO("agustin@ejemplo.com", "DEPORTISTA");
        
        when(visualizarCredencialesInput.visualizar(idUsuarioSimulado)).thenReturn(credencialesDTO);

        // Act
        ResponseEntity<CredencialesDTO> response = credencialesController.visualizarCredenciales();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("agustin@ejemplo.com", response.getBody().getEmail());
        assertEquals("DEPORTISTA", response.getBody().getRol());
        verify(visualizarCredencialesInput).visualizar(idUsuarioSimulado);
    }
}
