package usecase;

import com.activachilecito.core.complejo.exception.ComplejoException;
import com.activachilecito.core.complejo.model.Cancha;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.model.HorarioDisponibilidad;
import com.activachilecito.core.complejo.output.CanchaRepositoryPort;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.CanchaResponseDTO;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObtenerCanchasPorComplejoUseCaseTest {

    @Mock
    private ComplejoRepositoryPort complejoRepositoryPort;

    @Mock
    private CanchaRepositoryPort canchaRepositoryPort;

    @InjectMocks
    private ObtenerCanchasPorComplejoUseCase obtenerCanchasPorComplejoUseCase;

    private Complejo complejo;

    @BeforeEach
    void setUp() {
        complejo = Complejo.restaurar(5L, 1L, "Complejo Padel Pro", "Castro y Bazan 100", "3825001122", List.of("Wifi"), new ArrayList<>());
    }

    @Test
    void obtenerPorComplejo_IdValido_RetornaListaDeCanchas() {
        Long complejoId = 5L;
        when(complejoRepositoryPort.buscarPorId(complejoId)).thenReturn(Optional.of(complejo));

        Cancha cancha1 = Cancha.restaurar(1L, complejoId, "Cancha A", "Padel", "Sintetico", new BigDecimal("10000.00"), true,
                List.of(HorarioDisponibilidad.restaurar(100L, 1, LocalTime.of(18, 0), LocalTime.of(23, 0), 1L)));
        Cancha cancha2 = Cancha.restaurar(2L, complejoId, "Cancha B", "Padel", "Panoramica", new BigDecimal("12000.00"), true, new ArrayList<>());

        when(canchaRepositoryPort.buscarPorComplejoId(complejoId)).thenReturn(List.of(cancha1, cancha2));

        List<CanchaResponseDTO> resultado = obtenerCanchasPorComplejoUseCase.obtenerPorComplejo(complejoId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Cancha A", resultado.get(0).getNombre());
        assertEquals(1, resultado.get(0).getHorarios().size());
        assertEquals("Cancha B", resultado.get(1).getNombre());
    }

    @Test
    void obtenerPorComplejo_ComplejoNoExiste_LanzaComplejoException() {
        Long idInexistente = 999L;
        when(complejoRepositoryPort.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ComplejoException.class, () -> obtenerCanchasPorComplejoUseCase.obtenerPorComplejo(idInexistente));
        verify(canchaRepositoryPort, never()).buscarPorComplejoId(any());
    }

    @Test
    void obtenerPorDueno_UsuarioConComplejo_RetornaListaDeCanchas() {
        Long usuarioId = 1L;
        when(complejoRepositoryPort.buscarPorUsuarioId(usuarioId)).thenReturn(Optional.of(complejo));

        Cancha cancha = Cancha.restaurar(1L, 5L, "Cancha Central", "Futbol 5", "Cesped", new BigDecimal("14000.00"), true, new ArrayList<>());
        when(canchaRepositoryPort.buscarPorComplejoId(5L)).thenReturn(List.of(cancha));

        List<CanchaResponseDTO> resultado = obtenerCanchasPorComplejoUseCase.obtenerPorDueno(usuarioId);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cancha Central", resultado.get(0).getNombre());
    }

    @Test
    void obtenerPorDueno_UsuarioSinComplejo_RetornaListaVacia() {
        Long usuarioId = 2L;
        when(complejoRepositoryPort.buscarPorUsuarioId(usuarioId)).thenReturn(Optional.empty());

        List<CanchaResponseDTO> resultado = obtenerCanchasPorComplejoUseCase.obtenerPorDueno(usuarioId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(canchaRepositoryPort, never()).buscarPorComplejoId(any());
    }
}
