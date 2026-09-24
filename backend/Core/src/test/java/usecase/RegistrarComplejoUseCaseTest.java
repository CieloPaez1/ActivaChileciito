package usecase;

import com.activachilecito.core.complejo.exception.ComplejoException;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import exception.ExcepcionUsuarioNoEncontrado;
import input.RegistrarComplejoRequest;
import model.RolUsuario;
import model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.ComplejoResponseDTO;
import output.UsuarioOutput;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarComplejoUseCaseTest {

    @Mock
    private ComplejoRepositoryPort complejoRepositoryPort;

    @Mock
    private UsuarioOutput usuarioOutput;

    @InjectMocks
    private RegistrarComplejoUseCase registrarComplejoUseCase;

    @Test
    void registrar_UsuarioValidoYSinComplejoPrevio_RegistraCorrectamente() {
        Long idDueno = 1L;
        Usuario usuario = Usuario.restaurar(idDueno, "Juan", "Perez", "juan@test.com", "hash", RolUsuario.DUENO_DE_COMPLEJO, "123", true);
        
        RegistrarComplejoRequest request = RegistrarComplejoRequest.builder()
                .nombre("PÃ¡del Chilecito")
                .direccion("Av. Principal 123")
                .telefono("555-1234")
                .prestaciones(List.of("Cancha Cristal", "Bar"))
                .build();

        when(usuarioOutput.buscarPorId(idDueno)).thenReturn(Optional.of(usuario));
        

        ComplejoResponseDTO response = registrarComplejoUseCase.registrar(idDueno, request);

        assertNotNull(response);
        assertEquals("PÃ¡del Chilecito", response.getNombre());
        assertEquals("Av. Principal 123", response.getDireccion());
        
        verify(complejoRepositoryPort).guardar(eq(idDueno), any(Complejo.class));
    }

    @Test
    void registrar_UsuarioNoExiste_LanzaExcepcion404() {
        Long idInvalido = 99L;
        RegistrarComplejoRequest request = new RegistrarComplejoRequest();
        when(usuarioOutput.buscarPorId(idInvalido)).thenReturn(Optional.empty());

        assertThrows(ExcepcionUsuarioNoEncontrado.class, () -> registrarComplejoUseCase.registrar(idInvalido, request));
        verifyNoInteractions(complejoRepositoryPort);
    }

    @Test
    void registrar_UsuarioNoEsAdmin_LanzaExcepcionDeReglaDeNegocio() {
        Long idDEPORTISTA = 2L;
        Usuario usuarioDEPORTISTA = Usuario.restaurar(idDEPORTISTA, "Ana", "Paz", "ana@test.com", "hash", RolUsuario.DEPORTISTA, "123", true);
        RegistrarComplejoRequest request = new RegistrarComplejoRequest();

        when(usuarioOutput.buscarPorId(idDEPORTISTA)).thenReturn(Optional.of(usuarioDEPORTISTA));

        assertThrows(ComplejoException.class, () -> registrarComplejoUseCase.registrar(idDEPORTISTA, request));
        verifyNoInteractions(complejoRepositoryPort);
    }

    }
