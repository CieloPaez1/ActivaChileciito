package usecase;

import model.RolUsuario;
import model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import output.CredencialesDTO;
import output.ObtenerUsuarioPorIdPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisualizarCredencialesUseCaseTest {

    @Mock
    private ObtenerUsuarioPorIdPort obtenerUsuarioPorIdPort;

    @InjectMocks
    private VisualizarCredencialesUseCase visualizarCredencialesUseCase;

    @Test
    void visualizar_ConIdExistente_DevuelveCredencialesDTO() {
        // Arrange
        Long idUsuario = 1L;
        Usuario usuarioSimulado = Usuario.crear("Cielo", "Paez", "cielo@ejemplo.com", "Pass123", RolUsuario.CLIENTE, "3825123456");
        
        when(obtenerUsuarioPorIdPort.obtenerPorId(idUsuario)).thenReturn(usuarioSimulado);

        // Act
        CredencialesDTO resultado = visualizarCredencialesUseCase.visualizar(idUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("cielo@ejemplo.com", resultado.getEmail());
        assertEquals("CLIENTE", resultado.getRol());
        verify(obtenerUsuarioPorIdPort).obtenerPorId(idUsuario);
    }
}
