package usecase;

import exception.ExcepcionUsuarioNoEncontrado;
import input.ModificarPerfilRequest;
import lombok.RequiredArgsConstructor;
import model.Usuario;
import output.PerfilResponseDTO;
import output.UsuarioRepositoryPort;

@RequiredArgsConstructor
public class ModificarPerfilUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public PerfilResponseDTO modificar(Long id, ModificarPerfilRequest request) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado con ID: " + id));

        // Reglas de negocio: Solo actualizar campos permitidos, evitando nulos que pisen la info actual
        if (request.getNombre() != null && !request.getNombre().trim().isEmpty()) {
            usuario.setNombre(request.getNombre());
        }
        if (request.getApellido() != null && !request.getApellido().trim().isEmpty()) {
            usuario.setApellido(request.getApellido());
        }
        if (request.getTelefono() != null && !request.getTelefono().trim().isEmpty()) {
            usuario.setTelefono(request.getTelefono());
        }

        usuarioRepositoryPort.actualizar(usuario);

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
