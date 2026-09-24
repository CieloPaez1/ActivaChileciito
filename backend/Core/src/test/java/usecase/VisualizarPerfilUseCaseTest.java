package usecase;

import exception.ExcepcionUsuarioNoEncontrado;
import model.RolUsuario;
import model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.PerfilResponseDTO;
import output.UsuarioOutput;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisualizarPerfilUseCaseTest {

    @Mock
    private UsuarioOutput usuarioOutput;

    @InjectMocks
    private VisualizarPerfilUseCase visualizarPerfilUseCase;

    @Test
    void visualizar_UsuarioExiste_DevuelvePerfilResponseDTO_SinPassword() {
        // Arrange
        Long userId = 1L;
        Usuario usuario = Usuario.crear("Cielo", "Paez", "cielo@ejemplo.com", "PassSecreta", RolUsuario.DEPORTISTA, "3825123456");
        usuario.setId(userId);
        when(usuarioOutput.buscarPorId(userId)).thenReturn(Optional.of(usuario));

        // Act
        PerfilResponseDTO response = visualizarPerfilUseCase.visualizar(userId);

        // Assert
        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals("Cielo", response.getNombre());
        assertEquals("Paez", response.getApellido());
        assertEquals("cielo@ejemplo.com", response.getEmail());
        assertEquals("DEPORTISTA", response.getRol());
        assertTrue(response.isActivo());
        verify(usuarioOutput).buscarPorId(userId);
    }

    @Test
    void visualizar_UsuarioNoExiste_LanzaExcepcion404() {
        // Arrange
        Long userId = 99L;
        when(usuarioOutput.buscarPorId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionUsuarioNoEncontrado.class, () -> visualizarPerfilUseCase.visualizar(userId));
    }
}
