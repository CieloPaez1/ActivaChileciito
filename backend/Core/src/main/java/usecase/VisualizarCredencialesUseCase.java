package usecase;

import input.VisualizarCredencialesInput;
import lombok.RequiredArgsConstructor;
import model.Usuario;
import output.CredencialesDTO;
import output.ObtenerUsuarioPorIdPort;

@RequiredArgsConstructor
public class VisualizarCredencialesUseCase implements VisualizarCredencialesInput {

    private final ObtenerUsuarioPorIdPort obtenerUsuarioPorIdPort;

    @Override
    public CredencialesDTO visualizar(Long idUsuario) {
        Usuario usuario = obtenerUsuarioPorIdPort.obtenerPorId(idUsuario);
        return CredencialesDTO.builder()
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .build();
    }
}
