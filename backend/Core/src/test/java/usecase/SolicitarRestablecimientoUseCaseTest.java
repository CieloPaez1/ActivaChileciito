package usecase;

import input.SolicitarRestablecimientoRequest;
import model.RolUsuario;
import model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.EmailSenderPort;
import output.TokenRecuperacionPort;
import output.UsuarioRepositoryPort;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitarRestablecimientoUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;
    @Mock
    private TokenRecuperacionPort tokenRecuperacionPort;
    @Mock
    private EmailSenderPort emailSenderPort;

    @InjectMocks
    private SolicitarRestablecimientoUseCase solicitarRestablecimientoUseCase;

    @Test
    void solicitar_ConEmailExistente_GeneraTokenYEnviaEmail() {
        // Arrange
        SolicitarRestablecimientoRequest request = new SolicitarRestablecimientoRequest();
        request.setEmail("cielo@ejemplo.com");
        
        Usuario usuario = Usuario.crear("Cielo", "Paez", "cielo@ejemplo.com", "Pass123", RolUsuario.CLIENTE, "3825123456");
        when(usuarioRepositoryPort.buscarPorEmail(request.getEmail())).thenReturn(Optional.of(usuario));
        when(tokenRecuperacionPort.generarToken(any())).thenReturn("token-uuid-123");

        // Act
        solicitarRestablecimientoUseCase.solicitar(request);

        // Assert
        verify(tokenRecuperacionPort).generarToken(usuario.getId());
        verify(emailSenderPort).enviarEmailRecuperacion(request.getEmail(), "token-uuid-123");
    }

    @Test
    void solicitar_ConEmailNoExistente_NoHaceNadaSilenciosamente() {
        // Arrange
        SolicitarRestablecimientoRequest request = new SolicitarRestablecimientoRequest();
        request.setEmail("noexiste@ejemplo.com");
        
        when(usuarioRepositoryPort.buscarPorEmail(request.getEmail())).thenReturn(Optional.empty());

        // Act
        solicitarRestablecimientoUseCase.solicitar(request);

        // Assert
        verify(tokenRecuperacionPort, never()).generarToken(anyLong());
        verify(emailSenderPort, never()).enviarEmailRecuperacion(anyString(), anyString());
    }
}
