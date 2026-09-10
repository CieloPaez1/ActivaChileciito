package usecase;

import exception.ExcepcionTokenInvalido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.TokenBlacklistPort;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CerrarSesionUseCaseTest {

    @Mock
    private TokenBlacklistPort tokenBlacklistPort;

    @InjectMocks
    private CerrarSesionUseCase cerrarSesionUseCase;

    @Test
    void cerrarSesion_ConTokenValido_InvalidaTokenExitosamente() {
        String tokenHeader = "Bearer jwt-token-valido";
        String jwt = "jwt-token-valido";

        when(tokenBlacklistPort.esTokenInvalido(jwt)).thenReturn(false);

        cerrarSesionUseCase.cerrarSesion(tokenHeader);

        verify(tokenBlacklistPort).invalidarToken(jwt);
    }

    @Test
    void cerrarSesion_ConTokenNulo_LanzaExcepcion() {
        assertThrows(ExcepcionTokenInvalido.class, () -> cerrarSesionUseCase.cerrarSesion(null));
        verifyNoInteractions(tokenBlacklistPort);
    }

    @Test
    void cerrarSesion_ConTokenMalformado_LanzaExcepcion() {
        assertThrows(ExcepcionTokenInvalido.class, () -> cerrarSesionUseCase.cerrarSesion("TokenInvalido"));
        verifyNoInteractions(tokenBlacklistPort);
    }

    @Test
    void cerrarSesion_ConTokenYaInvalidado_LanzaExcepcion() {
        String tokenHeader = "Bearer jwt-token-ya-invalido";
        String jwt = "jwt-token-ya-invalido";

        when(tokenBlacklistPort.esTokenInvalido(jwt)).thenReturn(true);

        assertThrows(ExcepcionTokenInvalido.class, () -> cerrarSesionUseCase.cerrarSesion(tokenHeader));
        verify(tokenBlacklistPort, never()).invalidarToken(anyString());
    }
}
