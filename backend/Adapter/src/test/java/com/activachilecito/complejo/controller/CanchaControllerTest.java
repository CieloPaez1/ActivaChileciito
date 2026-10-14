package com.activachilecito.complejo.controller;

import com.activachilecito.usuarios.adapter.output.JwtProviderAdapter;
import com.activachilecito.usuarios.config.CustomUserDetails;
import input.RegistrarCanchaRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import output.CanchaResponseDTO;
import output.HorarioResponseDTO;
import usecase.ObtenerCanchasPorComplejoUseCase;
import usecase.RegistrarCanchaUseCase;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CanchaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CanchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrarCanchaUseCase registrarCanchaUseCase;

    @MockBean
    private ObtenerCanchasPorComplejoUseCase obtenerCanchasPorComplejoUseCase;

    @MockBean
    private JwtProviderAdapter jwtProviderAdapter;

    @Test
    void testRegistrarCancha_Devuelve201() throws Exception {
        CustomUserDetails userDetails = new CustomUserDetails(1L, "carlos@test.com", "pass", "DUENO_DE_COMPLEJO", true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        CanchaResponseDTO dto = CanchaResponseDTO.builder()
                .id(10L)
                .complejoId(1L)
                .nombre("Cancha Central")
                .deporte("Padel")
                .tipoSuelo("Cristal")
                .precioTurno(new BigDecimal("12000.00"))
                .activa(true)
                .horarios(List.of(
                        new HorarioResponseDTO(1L, 1, LocalTime.of(18, 0), LocalTime.of(23, 0))
                ))
                .build();

        when(registrarCanchaUseCase.registrar(eq(1L), any(RegistrarCanchaRequest.class))).thenReturn(dto);

        String jsonBody = """
                {
                  "nombre": "Cancha Central",
                  "deporte": "Padel",
                  "tipoSuelo": "Cristal",
                  "precioTurno": 12000.00,
                  "horarios": [
                    {
                      "diaSemana": 1,
                      "horaInicio": "18:00:00",
                      "horaFin": "23:00:00"
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/canchas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .principal(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nombre").value("Cancha Central"))
                .andExpect(jsonPath("$.deporte").value("Padel"))
                .andExpect(jsonPath("$.precioTurno").value(12000.00))
                .andExpect(jsonPath("$.activa").value(true))
                .andExpect(jsonPath("$.horarios[0].diaSemana").value(1));
    }

    @Test
    void testObtenerCanchasPorComplejo_Devuelve200() throws Exception {
        CanchaResponseDTO dto = CanchaResponseDTO.builder()
                .id(1L)
                .complejoId(5L)
                .nombre("Cancha 1")
                .deporte("Futbol 5")
                .tipoSuelo("Sintetico")
                .precioTurno(new BigDecimal("15000.00"))
                .activa(true)
                .horarios(List.of())
                .build();

        when(obtenerCanchasPorComplejoUseCase.obtenerPorComplejo(5L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/canchas/complejo/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Cancha 1"))
                .andExpect(jsonPath("$[0].deporte").value("Futbol 5"));
    }
}
