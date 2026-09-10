package usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.JwtProviderPort;
import output.ValidarCredencialesPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IniciarSesionUseCaseTest {

    @Mock
    private ValidarCredencialesPort validarCredencialesPort;

    @Mock
    private JwtProviderPort jwtProviderPort;

    @InjectMocks
    private IniciarSesionUseCase iniciarSesionUseCase;

    @Test
    void iniciarSesion_CredencialesValidas_DevuelveToken() {
        // Arrange
        String email = "usuario@test.com";
        String password = "Password123!";
        String tokenEsperado = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";

        doNothing().when(validarCredencialesPort).validar(email, password);
        when(jwtProviderPort.generarToken(email)).thenReturn(tokenEsperado);

        // Act
        String tokenReal = iniciarSesionUseCase.iniciarSesion(email, password);

        // Assert
        assertEquals(tokenEsperado, tokenReal);
        verify(validarCredencialesPort).validar(email, password);
        verify(jwtProviderPort).generarToken(email);
    }
}
