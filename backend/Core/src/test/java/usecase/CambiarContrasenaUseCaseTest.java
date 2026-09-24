package usecase;

import exception.ExcepcionCredencialesInvalidas;
import exception.ExcepcionUsuario;
import exception.ExcepcionUsuarioNoEncontrado;
import input.CambiarContrasenaRequest;
import model.RolUsuario;
import model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.PasswordEncoderPort;
import output.UsuarioOutput;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CambiarContrasenaUseCaseTest {

    @Mock
    private UsuarioOutput usuarioOutput;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private CambiarContrasenaUseCase cambiarContrasenaUseCase;

    @Test
    void cambiarContrasena_Exitosa() {
        Long userId = 1L;
        Usuario usuario = Usuario.crear("Test", "User", "test@test.com", "hash-actual", RolUsuario.DEPORTISTA, "12345");
        usuario.setId(userId);
        
        CambiarContrasenaRequest request = new CambiarContrasenaRequest("clave-actual", "nueva-clave-valida");

        when(usuarioOutput.buscarPorId(userId)).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.coincide("clave-actual", "hash-actual")).thenReturn(true);
        when(passwordEncoderPort.encriptar("nueva-clave-valida")).thenReturn("nuevo-hash");

        cambiarContrasenaUseCase.cambiarContrasena(userId, request);

        assertEquals("nuevo-hash", usuario.getPassword());
        verify(usuarioOutput).guardar(usuario);
    }

    @Test
    void cambiarContrasena_UsuarioNoEncontrado_Lanza404() {
        Long userId = 99L;
        CambiarContrasenaRequest request = new CambiarContrasenaRequest("clave", "nueva-clave");

        when(usuarioOutput.buscarPorId(userId)).thenReturn(Optional.empty());

        assertThrows(ExcepcionUsuarioNoEncontrado.class, () -> cambiarContrasenaUseCase.cambiarContrasena(userId, request));
        verifyNoInteractions(passwordEncoderPort);
    }

    @Test
    void cambiarContrasena_ClaveActualIncorrecta_Lanza400() {
        Long userId = 1L;
        Usuario usuario = Usuario.crear("Test", "User", "test@test.com", "hash-actual", RolUsuario.DEPORTISTA, "12345");
        CambiarContrasenaRequest request = new CambiarContrasenaRequest("clave-mala", "nueva-clave");

        when(usuarioOutput.buscarPorId(userId)).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.coincide("clave-mala", "hash-actual")).thenReturn(false);

        assertThrows(ExcepcionCredencialesInvalidas.class, () -> cambiarContrasenaUseCase.cambiarContrasena(userId, request));
        verify(passwordEncoderPort, never()).encriptar(anyString());
        verify(usuarioOutput, never()).guardar(any());
    }

    @Test
    void cambiarContrasena_NuevaClaveCorta_LanzaExcepcion() {
        Long userId = 1L;
        Usuario usuario = Usuario.crear("Test", "User", "test@test.com", "hash-actual", RolUsuario.DEPORTISTA, "12345");
        CambiarContrasenaRequest request = new CambiarContrasenaRequest("clave-actual", "corta");

        when(usuarioOutput.buscarPorId(userId)).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.coincide("clave-actual", "hash-actual")).thenReturn(true);

        assertThrows(ExcepcionUsuario.class, () -> cambiarContrasenaUseCase.cambiarContrasena(userId, request));
        verify(passwordEncoderPort, never()).encriptar(anyString());
        verify(usuarioOutput, never()).guardar(any());
    }
}
