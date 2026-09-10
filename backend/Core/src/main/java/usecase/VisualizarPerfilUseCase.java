package usecase;

import exception.ExcepcionUsuarioNoEncontrado;
import lombok.RequiredArgsConstructor;
import model.Usuario;
import output.PerfilResponseDTO;
import output.UsuarioRepositoryPort;

@RequiredArgsConstructor
public class VisualizarPerfilUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public PerfilResponseDTO visualizar(Long id) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado con ID: " + id));

        return PerfilResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .activo(usuario.isActivo())
                .build();
    }
}
