package usecase;

import exception.ExcepcionUsuarioNoEncontrado;
import input.ModificarUsuarioAdminRequest;
import lombok.RequiredArgsConstructor;
import model.Usuario;
import output.UsuarioOutput;

@RequiredArgsConstructor
public class ModificarUsuarioAdminUseCase {

    private final UsuarioOutput usuarioOutput;

    public void modificar(Long id, ModificarUsuarioAdminRequest request) {
        Usuario usuario = usuarioOutput.buscarPorId(id)
                .orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado con ID: " + id));

        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            usuario.setNombre(request.getNombre());
        }
        if (request.getApellido() != null && !request.getApellido().isBlank()) {
            usuario.setApellido(request.getApellido());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            usuario.setEmail(request.getEmail());
        }
        if (request.getTelefono() != null && !request.getTelefono().isBlank()) {
            usuario.setTelefono(request.getTelefono());
        }
        if (request.getRol() != null) {
            usuario.setRol(request.getRol());
        }
        if (request.getActivo() != null) {
            usuario.setActivo(request.getActivo());
        }

        usuarioOutput.guardar(usuario);
    }
}
