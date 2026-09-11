package usecase;

import com.activachilecito.core.complejo.exception.ComplejoException;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import exception.ExcepcionUsuarioNoEncontrado;
import input.RegistrarComplejoRequest;
import lombok.RequiredArgsConstructor;
import model.RolUsuario;
import model.Usuario;
import output.ComplejoResponseDTO;
import output.UsuarioOutput;

@RequiredArgsConstructor
public class RegistrarComplejoUseCase {

    private final ComplejoRepositoryPort complejoRepositoryPort;
    private final UsuarioOutput usuarioOutput;

    public ComplejoResponseDTO registrar(Long idUsuarioDueño, RegistrarComplejoRequest request) {
        Usuario usuario = usuarioOutput.buscarPorId(idUsuarioDueño)
                .orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado con ID: " + idUsuarioDueño));

        if (usuario.getRol() != RolUsuario.ADMIN_COMPLEJO) {
            throw new ComplejoException("Operación denegada. El usuario no posee rol de Propietario de Complejo.");
        }

        Complejo complejo = Complejo.crear(
                request.getNombre(),
                request.getDireccion(),
                request.getTelefono(),
                request.getPrestaciones()
        );

        complejoRepositoryPort.guardar(idUsuarioDueño, complejo);

        return ComplejoResponseDTO.builder()
                .nombre(complejo.getNombre())
                .direccion(complejo.getDireccion())
                .telefono(complejo.getTelefono())
                .prestaciones(complejo.getPrestaciones())
                .build();
    }
}
