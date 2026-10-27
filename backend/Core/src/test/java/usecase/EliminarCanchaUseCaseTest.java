package usecase;

import com.activachilecito.core.complejo.exception.ComplejoException;
import com.activachilecito.core.complejo.model.Cancha;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.CanchaRepositoryPort;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EliminarCanchaUseCaseTest {

    @Mock
    private ComplejoRepositoryPort complejoRepositoryPort;

    @Mock
    private CanchaRepositoryPort canchaRepositoryPort;

    @InjectMocks
    private EliminarCanchaUseCase eliminarCanchaUseCase;

    private Complejo complejoPropio;
    private Cancha canchaPropia;

    @BeforeEach
    void setUp() {
        complejoPropio = Complejo.restaurar(10L, 1L, "Complejo Pro", "Calle 1", "3825111", List.of(), new ArrayList<>());
        canchaPropia = Cancha.restaurar(50L, 10L, "Cancha 1", "Padel", "Cristal", new BigDecimal("12000"), true, new ArrayList<>());
    }

    @Test
    void eliminar_CanchaDePropietarioValido_EliminaExitosamente() {
        when(canchaRepositoryPort.buscarPorId(50L)).thenReturn(Optional.of(canchaPropia));
        when(complejoRepositoryPort.buscarPorUsuarioId(1L)).thenReturn(Optional.of(complejoPropio));

        eliminarCanchaUseCase.eliminar(1L, 50L);

        verify(canchaRepositoryPort, times(1)).eliminar(50L);
    }

    @Test
    void eliminar_CanchaDeOtroComplejo_LanzaComplejoException() {
        Cancha canchaAjena = Cancha.restaurar(99L, 999L, "Cancha Ajena", "Tenis", "Polvo", new BigDecimal("10000"), true, new ArrayList<>());
        when(canchaRepositoryPort.buscarPorId(99L)).thenReturn(Optional.of(canchaAjena));
        when(complejoRepositoryPort.buscarPorUsuarioId(1L)).thenReturn(Optional.of(complejoPropio));

        assertThrows(ComplejoException.class, () -> eliminarCanchaUseCase.eliminar(1L, 99L));
        verify(canchaRepositoryPort, never()).eliminar(anyLong());
    }
}
