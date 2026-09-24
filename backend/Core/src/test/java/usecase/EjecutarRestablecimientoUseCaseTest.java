package usecase;

import exception.ExcepcionTokenInvalido;
import exception.ExcepcionUsuarioNoEncontrado;
import input.EjecutarRestablecimientoRequest;
import model.RolUsuario;
import model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.PasswordEncoderPort;
import output.TokenRecuperacionPort;
import output.UsuarioOutput;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EjecutarRestablecimientoUseCaseTest {

    @Mock
    private TokenRecuperacionPort tokenRecuperacionPort;
    @Mock
    private UsuarioOutput usuarioOutput;
    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private EjecutarRestablecimientoUseCase ejecutarRestablecimientoUseCase;

    @Test
    void ejecutar_ConTokenValidoYNuevaClaveCorrecta_ActualizaClaveEInvalidaToken() {
        // Arrange
        EjecutarRestablecimientoRequest request = new EjecutarRestablecimientoRequest();
        request.setToken("token-uuid-123");
        request.setNuevaClave("NuevaClave123");

        Usuario usuario = Usuario.crear("Cielo", "Paez", "cielo@ejemplo.com", "PassAnterior", RolUsuario.DEPORTISTA, "3825123456");
        Long usuarioId = 1L;

        when(tokenRecuperacionPort.esTokenValido(request.getToken())).thenReturn(true);
        when(tokenRecuperacionPort.obtenerUsuarioIdPorToken(request.getToken())).thenReturn(usuarioId);
        when(usuarioOutput.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.encriptar(request.getNuevaClave())).thenReturn("ClaveEncriptadaHash");

        // Act
        ejecutarRestablecimientoUseCase.ejecutar(request);

        // Assert
        assertEquals("ClaveEncriptadaHash", usuario.getPassword());
        verify(usuarioOutput).guardar(usuario);
        verify(tokenRecuperacionPort).invalidarToken(request.getToken());
    }

    @Test
    void ejecutar_ConTokenInvalido_LanzaExcepcion() {
        // Arrange
        EjecutarRestablecimientoRequest request = new EjecutarRestablecimientoRequest();
        request.setToken("token-invalido");
        request.setNuevaClave("NuevaClave123");

        when(tokenRecuperacionPort.esTokenValido(request.getToken())).thenReturn(false);

        // Act & Assert
        assertThrows(exception.ExcepcionUsuario.class, () -> ejecutarRestablecimientoUseCase.ejecutar(request));
    }

    }
