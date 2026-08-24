package com.activachilecito.usuarios.controller;

import input.RegistrarUsuarioInput;
import model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UsuarioPostController.class)
public class UsuarioPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrarUsuarioInput registrarUsuarioInput;

    @Test
    void testRegistrarUsuario_Devuelve201() throws Exception {
        // Arrange
        Usuario usuarioDevuelto = Usuario.crear("Cielo", "Paez", "cielo@ejemplo.com", "Pass123", model.RolUsuario.CLIENTE, "3825123456");
        when(registrarUsuarioInput.registrarUsuario(any(Usuario.class))).thenReturn(usuarioDevuelto);

        String jsonBody = """
                {
                  "nombre": "Cielo",
                  "apellido": "Paez",
                  "email": "cielo@ejemplo.com",
                  "password": "Password123",
                  "rol": "CLIENTE",
                  "telefono": "3825123456"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated()) // 201
                .andExpect(content().string("Usuario creado correctamente"));
    }
}
