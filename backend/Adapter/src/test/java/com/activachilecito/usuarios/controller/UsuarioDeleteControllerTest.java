package com.activachilecito.usuarios.controller;

import input.EliminarUsuarioInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.activachilecito.usuarios.adapter.output.JwtProviderAdapter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioDeleteController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
public class UsuarioDeleteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EliminarUsuarioInput eliminarUsuarioInput;

    @MockBean
    private JwtProviderAdapter jwtProviderAdapter;

    @Test
    void testEliminarUsuario_Devuelve204() throws Exception {
        // Arrange
        Long userId = 1L;
        doNothing().when(eliminarUsuarioInput).eliminarUsuario(userId);

        // Act & Assert
        mockMvc.perform(delete("/api/usuarios/me").principal(new UsernamePasswordAuthenticationToken(new com.activachilecito.usuarios.config.CustomUserDetails(userId, "test@test.com", "", "DEPORTISTA", true), null))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // 204
    }
}
