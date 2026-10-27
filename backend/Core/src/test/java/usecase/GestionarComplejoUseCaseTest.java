package usecase;

import com.activachilecito.core.complejo.exception.ComplejoException;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import input.RegistrarComplejoRequest;
import model.RolUsuario;
import model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.ComplejoResponseDTO;
import output.UsuarioOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionarComplejoUseCaseTest {

    @Mock
    private ComplejoRepositoryPort complejoRepositoryPort;

    @Mock
    private UsuarioOutput usuarioOutput;

    @InjectMocks
    private GestionarComplejoUseCase gestionarComplejoUseCase;

    private Usuario duenoUsuario;
    private Complejo complejoExistente;

    @BeforeEach
    void setUp() {
        duenoUsuario = Usuario.restaurar(1L, "Carlos", "Gomez", "carlos@test.com", "hash", RolUsuario.DUENO_DE_COMPLEJO, "3825123456", true);
        complejoExistente = Complejo.restaurar(10L, 1L, "Complejo San Martin", "La Plata 456", "3825123456", List.of("Iluminacion LED", "Vestuarios"), new ArrayList<>());
    }

    @Test
    void obtenerPorDueno_ExisteComplejo_RetornaOptionalConDatos() {
        when(complejoRepositoryPort.buscarPorUsuarioId(1L)).thenReturn(Optional.of(complejoExistente));

        Optional<ComplejoResponseDTO> resultado = gestionarComplejoUseCase.obtenerPorDueno(1L);

        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getId());
        assertEquals("Complejo San Martin", resultado.get().getNombre());
        assertEquals(2, resultado.get().getPrestaciones().size());
    }

    @Test
    void actualizarOCrearPorDueno_ComplejoExistente_ActualizaNombreYPrestaciones() {
        RegistrarComplejoRequest request = RegistrarComplejoRequest.builder()
                .nombre("Complejo San Martin Renovado")
                .direccion("Av. Pelagio B. Luna 200")
                .telefono("3825998877")
                .prestaciones(List.of("Iluminacion LED", "Vestuarios", "Buffet", "Estacionamiento"))
                .build();

        when(usuarioOutput.buscarPorId(1L)).thenReturn(Optional.of(duenoUsuario));
        when(complejoRepositoryPort.buscarPorUsuarioId(1L)).thenReturn(Optional.of(complejoExistente));

        ComplejoResponseDTO response = gestionarComplejoUseCase.actualizarOCrearPorDueno(1L, request);

        assertNotNull(response);
        assertEquals("Complejo San Martin Renovado", response.getNombre());
        assertEquals("Av. Pelagio B. Luna 200", response.getDireccion());
        assertEquals(4, response.getPrestaciones().size());
        verify(complejoRepositoryPort, times(1)).guardar(eq(1L), any(Complejo.class));
    }

    @Test
    void actualizarOCrearPorDueno_UsuarioNoEsDueno_LanzaComplejoException() {
        Usuario deportista = Usuario.restaurar(2L, "Ana", "Lopez", "ana@test.com", "hash", RolUsuario.DEPORTISTA, "123", true);
        RegistrarComplejoRequest request = RegistrarComplejoRequest.builder()
                .nombre("Complejo Test")
                .direccion("Dir 123")
                .telefono("38250000")
                .build();

        when(usuarioOutput.buscarPorId(2L)).thenReturn(Optional.of(deportista));

        assertThrows(ComplejoException.class, () -> gestionarComplejoUseCase.actualizarOCrearPorDueno(2L, request));
        verify(complejoRepositoryPort, never()).guardar(anyLong(), any());
    }

    @Test
    void obtenerTodos_RetornaCatalogoDeComplejos() {
        when(complejoRepositoryPort.obtenerTodos()).thenReturn(List.of(complejoExistente));

        List<ComplejoResponseDTO> lista = gestionarComplejoUseCase.obtenerTodos();

        assertEquals(1, lista.size());
        assertEquals("Complejo San Martin", lista.get(0).getNombre());
    }
}
