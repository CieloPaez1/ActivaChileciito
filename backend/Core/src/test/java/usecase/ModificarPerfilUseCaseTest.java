package usecase;

import exception.ExcepcionUsuarioNoEncontrado;
import input.ModificarPerfilRequest;
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
class ModificarPerfilUseCaseTest {

    @Mock
    private UsuarioOutput usuarioOutput;

    @InjectMocks
    private ModificarPerfilUseCase modificarPerfilUseCase;

    @Test
    void modificar_UsuarioExiste_ActualizaDatosPermitidosYRetornaDTOSinAlterarDatosSensibles() {
        // Arrange
        Long userId = 1L;
        Usuario usuarioExistente = Usuario.crear("Cielo", "Paez", "cielo@ejemplo.com", "PassSecreta", RolUsuario.DEPORTISTA, "3825123456");
        usuarioExistente.setId(userId);
        
        ModificarPerfilRequest request = ModificarPerfilRequest.builder()
                .nombre("NuevoNombre")
                .apellido("NuevoApellido")
                .telefono("111111111")
                .build();

        when(usuarioOutput.buscarPorId(userId)).thenReturn(Optional.of(usuarioExistente));

        // Act
        PerfilResponseDTO response = modificarPerfilUseCase.modificar(userId, request);

        // Assert
        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals("NuevoNombre", response.getNombre());
        assertEquals("NuevoApellido", response.getApellido());
        assertEquals("cielo@ejemplo.com", response.getEmail()); // Email inalterado
        assertEquals("DEPORTISTA", response.getRol()); // Rol inalterado
        assertEquals("PassSecreta", usuarioExistente.getPassword()); // Password inalterada en la entidad

        verify(usuarioOutput).guardar(usuarioExistente);
    }

    @Test
    void modificar_UsuarioNoExiste_LanzaExcepcion404() {
        // Arrange
        Long userId = 99L;
        ModificarPerfilRequest request = new ModificarPerfilRequest();
        when(usuarioOutput.buscarPorId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionUsuarioNoEncontrado.class, () -> modificarPerfilUseCase.modificar(userId, request));
    }
}
