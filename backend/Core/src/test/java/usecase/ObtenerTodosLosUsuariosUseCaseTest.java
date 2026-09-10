package usecase;

import model.RolUsuario;
import model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.UsuarioOutput;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObtenerTodosLosUsuariosUseCaseTest {

    @Mock
    private UsuarioOutput usuarioOutput;

    @InjectMocks
    private ObtenerTodosLosUsuariosUseCase obtenerTodosLosUsuariosUseCase;

    @Test
    void obtenerTodos_RetornaListaDeUsuarios() {
        Usuario u1 = Usuario.restaurar(1L, "Juan", "Perez", "juan@test.com", "hash", RolUsuario.CLIENTE, "123", true);
        Usuario u2 = Usuario.restaurar(2L, "Ana", "Gomez", "ana@test.com", "hash", RolUsuario.ADMIN_SISTEMA, "456", true);
        
        when(usuarioOutput.listarTodos()).thenReturn(List.of(u1, u2));

        List<Usuario> usuarios = obtenerTodosLosUsuariosUseCase.obtenerTodos();

        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
        assertEquals("Juan", usuarios.get(0).getNombre());
        assertEquals("Ana", usuarios.get(1).getNombre());
        verify(usuarioOutput, times(1)).listarTodos();
    }
}
