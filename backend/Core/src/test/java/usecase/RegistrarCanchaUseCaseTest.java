package usecase;

import com.activachilecito.core.complejo.exception.ComplejoException;
import com.activachilecito.core.complejo.model.Cancha;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.CanchaRepositoryPort;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import exception.ExcepcionUsuarioNoEncontrado;
import input.HorarioRequest;
import input.RegistrarCanchaRequest;
import model.RolUsuario;
import model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.CanchaResponseDTO;
import output.UsuarioOutput;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarCanchaUseCaseTest {

    @Mock
    private ComplejoRepositoryPort complejoRepositoryPort;

    @Mock
    private CanchaRepositoryPort canchaRepositoryPort;

    @Mock
    private UsuarioOutput usuarioOutput;

    @InjectMocks
    private RegistrarCanchaUseCase registrarCanchaUseCase;

    private Usuario duenoUsuario;
    private Complejo complejoExistente;

    @BeforeEach
    void setUp() {
        duenoUsuario = Usuario.restaurar(1L, "Carlos", "Gomez", "carlos@test.com", "hash", RolUsuario.DUENO_DE_COMPLEJO, "123456", true);
        complejoExistente = Complejo.restaurar(10L, 1L, "Complejo San Martin", "La Plata 456", "3825123456", List.of("Iluminacion LED", "Vestuarios"), new ArrayList<>());
    }

    @Test
    void registrar_UsuarioDuenoValido_RegistraCanchaYHorariosExitosamente() {
        Long idDueno = 1L;
        RegistrarCanchaRequest request = RegistrarCanchaRequest.builder()
                .nombre("Cancha 1 - Sintetico")
                .deporte("Futbol 5")
                .tipoSuelo("Cesped Sintetico")
                .precioTurno(new BigDecimal("15000.00"))
                .horarios(List.of(
                        new HorarioRequest(1, LocalTime.of(18, 0), LocalTime.of(23, 0)),
                        new HorarioRequest(2, LocalTime.of(18, 0), LocalTime.of(23, 0))
                ))
                .build();

        when(usuarioOutput.buscarPorId(idDueno)).thenReturn(Optional.of(duenoUsuario));
        when(complejoRepositoryPort.buscarPorUsuarioId(idDueno)).thenReturn(Optional.of(complejoExistente));
        when(canchaRepositoryPort.existePorNombreYComplejoId("Cancha 1 - Sintetico", 10L)).thenReturn(false);

        when(canchaRepositoryPort.guardar(any(Cancha.class))).thenAnswer(invocation -> {
            Cancha c = invocation.getArgument(0);
            return Cancha.restaurar(50L, c.getComplejoId(), c.getNombre(), c.getDeporte(), c.getTipoSuelo(), c.getPrecioTurno(), c.getActiva(), c.getHorariosDisponibilidad());
        });

        CanchaResponseDTO response = registrarCanchaUseCase.registrar(idDueno, request);

        assertNotNull(response);
        assertEquals(50L, response.getId());
        assertEquals(10L, response.getComplejoId());
        assertEquals("Cancha 1 - Sintetico", response.getNombre());
        assertEquals("Futbol 5", response.getDeporte());
        assertEquals(new BigDecimal("15000.00"), response.getPrecioTurno());
        assertTrue(response.getActiva());
        assertEquals(2, response.getHorarios().size());

        verify(canchaRepositoryPort, times(1)).guardar(any(Cancha.class));
    }

    @Test
    void registrar_UsuarioNoExiste_LanzaExcepcion404() {
        Long idInvalido = 99L;
        RegistrarCanchaRequest request = RegistrarCanchaRequest.builder().nombre("Cancha Test").build();
        when(usuarioOutput.buscarPorId(idInvalido)).thenReturn(Optional.empty());

        assertThrows(ExcepcionUsuarioNoEncontrado.class, () -> registrarCanchaUseCase.registrar(idInvalido, request));
        verifyNoInteractions(complejoRepositoryPort);
        verifyNoInteractions(canchaRepositoryPort);
    }

    @Test
    void registrar_UsuarioNoEsDueno_LanzaExcepcionDeReglaDeNegocio() {
        Long idDeportista = 2L;
        Usuario usuarioDeportista = Usuario.restaurar(idDeportista, "Cielo", "Paez", "cielo@test.com", "hash", RolUsuario.DEPORTISTA, "987654", true);
        RegistrarCanchaRequest request = RegistrarCanchaRequest.builder().nombre("Cancha Test").build();

        when(usuarioOutput.buscarPorId(idDeportista)).thenReturn(Optional.of(usuarioDeportista));

        assertThrows(ComplejoException.class, () -> registrarCanchaUseCase.registrar(idDeportista, request));
        verifyNoInteractions(complejoRepositoryPort);
        verifyNoInteractions(canchaRepositoryPort);
    }

    @Test
    void registrar_DuenoSinComplejoRegistrado_LanzaComplejoException() {
        Long idDueno = 1L;
        RegistrarCanchaRequest request = RegistrarCanchaRequest.builder()
                .nombre("Cancha Sin Complejo")
                .deporte("Padel")
                .tipoSuelo("Cristal")
                .precioTurno(new BigDecimal("12000.00"))
                .build();

        when(usuarioOutput.buscarPorId(idDueno)).thenReturn(Optional.of(duenoUsuario));
        when(complejoRepositoryPort.buscarPorUsuarioId(idDueno)).thenReturn(Optional.empty());

        ComplejoException ex = assertThrows(ComplejoException.class, () -> registrarCanchaUseCase.registrar(idDueno, request));
        assertTrue(ex.getMessage().contains("Debe registrar un complejo previamente"));
        verifyNoInteractions(canchaRepositoryPort);
    }

    @Test
    void registrar_NombreCanchaDuplicadoEnMismoComplejo_LanzaComplejoException() {
        Long idDueno = 1L;
        RegistrarCanchaRequest request = RegistrarCanchaRequest.builder()
                .nombre("Cancha Central")
                .deporte("Tenis")
                .tipoSuelo("Polvo de ladrillo")
                .precioTurno(new BigDecimal("10000.00"))
                .build();

        when(usuarioOutput.buscarPorId(idDueno)).thenReturn(Optional.of(duenoUsuario));
        when(complejoRepositoryPort.buscarPorUsuarioId(idDueno)).thenReturn(Optional.of(complejoExistente));
        when(canchaRepositoryPort.existePorNombreYComplejoId("Cancha Central", 10L)).thenReturn(true);

        ComplejoException ex = assertThrows(ComplejoException.class, () -> registrarCanchaUseCase.registrar(idDueno, request));
        assertTrue(ex.getMessage().contains("Ya existe una cancha con el nombre"));
        verify(canchaRepositoryPort, never()).guardar(any());
    }

    @Test
    void registrar_HorarioInvalidoHoraInicioPosterior_LanzaComplejoException() {
        Long idDueno = 1L;
        RegistrarCanchaRequest request = RegistrarCanchaRequest.builder()
                .nombre("Cancha 2")
                .deporte("Futbol 7")
                .tipoSuelo("Sintetico")
                .precioTurno(new BigDecimal("18000.00"))
                .horarios(List.of(
                        new HorarioRequest(1, LocalTime.of(22, 0), LocalTime.of(20, 0)) // Hora inicio > Hora fin
                ))
                .build();

        when(usuarioOutput.buscarPorId(idDueno)).thenReturn(Optional.of(duenoUsuario));
        when(complejoRepositoryPort.buscarPorUsuarioId(idDueno)).thenReturn(Optional.of(complejoExistente));
        when(canchaRepositoryPort.existePorNombreYComplejoId("Cancha 2", 10L)).thenReturn(false);

        assertThrows(ComplejoException.class, () -> registrarCanchaUseCase.registrar(idDueno, request));
        verify(canchaRepositoryPort, never()).guardar(any());
    }
}
