package com.activachilecito.usuarios.controller;

import com.activachilecito.usuarios.entity.DTO.UsuarioDto;
import com.activachilecito.usuarios.mapper.UsuarioMapper;
import input.ObtenerUsuariosPorRolInput;
import model.RolUsuario;
import model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioGetController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
public class UsuarioGetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObtenerUsuariosPorRolInput obtenerUsuariosPorRolInput;

    @MockBean
    private usecase.ObtenerTodosLosUsuariosUseCase obtenerTodosLosUsuariosUseCase;

    @MockBean
    private UsuarioMapper usuarioMapper;

    @Test
    void testObtenerUsuariosPorRol_DevuelveLista() throws Exception {
        // Arrange
        Usuario usuario = Usuario.crear("Cielo", "Paez", "cielo@ejemplo.com", "Pass123", RolUsuario.CLIENTE, "3825123456");
        UsuarioDto dto = new UsuarioDto(1L, "Cielo", "Paez", "cielo@ejemplo.com", "Pass123", RolUsuario.CLIENTE, "3825123456", true);

        when(obtenerUsuariosPorRolInput.obtenerUsuariosPorRol(RolUsuario.CLIENTE))
                .thenReturn(List.of(usuario));
        when(usuarioMapper.toDto(any(Usuario.class))).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/rol/CLIENTE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Cielo"))
                .andExpect(jsonPath("$[0].telefono").value("3825123456"));
    }
}
