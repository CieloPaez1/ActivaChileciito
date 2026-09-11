package usecase;

import exception.ExcepcionUsuarioNoEncontrado;
import input.ModificarUsuarioAdminRequest;
import model.RolUsuario;
import model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.UsuarioOutput;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModificarUsuarioAdminUseCaseTest {

    @Mock
    private UsuarioOutput usuarioOutput;

    @InjectMocks
    private ModificarUsuarioAdminUseCase modificarUsuarioAdminUseCase;

    @Test
    void modificar_UsuarioExisteYDatosValidos_ModificaYGuarda() {
        Long id = 1L;
        Usuario usuario = Usuario.restaurar(id, "Juan", "Perez", "juan@test.com", "hash", RolUsuario.CLIENTE, "123", true);
        ModificarUsuarioAdminRequest request = ModificarUsuarioAdminRequest.builder()
                .nombre("Juan Carlos")
                .rol(RolUsuario.ADMIN_COMPLEJO)
                .activo(false)
                .build();

        when(usuarioOutput.buscarPorId(id)).thenReturn(Optional.of(usuario));

        modificarUsuarioAdminUseCase.modificar(id, request);

        assertEquals("Juan Carlos", usuario.getNombre());
        assertEquals(RolUsuario.ADMIN_COMPLEJO, usuario.getRol());
        assertFalse(usuario.isActivo());
        assertEquals("Perez", usuario.getApellido()); // no se modifica
        verify(usuarioOutput, times(1)).guardar(usuario);
    }

    @Test
    void modificar_UsuarioNoExiste_LanzaExcepcion() {
        Long id = 99L;
        ModificarUsuarioAdminRequest request = new ModificarUsuarioAdminRequest();

        when(usuarioOutput.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(ExcepcionUsuarioNoEncontrado.class, () -> modificarUsuarioAdminUseCase.modificar(id, request));
        verify(usuarioOutput, never()).guardar(any());
    }
}
