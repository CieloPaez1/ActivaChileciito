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

    public ComplejoResponseDTO registrar(Long idUsuarioDueÃno, RegistrarComplejoRequest request) {
        Usuario usuario = usuarioOutput.buscarPorId(idUsuarioDueÃno)
                .orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado con ID: " + idUsuarioDueÃno));

        if (usuario.getRol() != RolUsuario.DUENO_DE_COMPLEJO) {
            throw new ComplejoException("OperaciÃ³n denegada. El usuario no posee rol de Propietario de Complejo.");
        }

        Complejo complejo = Complejo.crear(
                request.getNombre(),
                request.getDireccion(),
                request.getTelefono(),
                request.getPrestaciones()
        );

        complejoRepositoryPort.guardar(idUsuarioDueÃno, complejo);

        return ComplejoResponseDTO.builder()
                .nombre(complejo.getNombre())
                .direccion(complejo.getDireccion())
                .telefono(complejo.getTelefono())
                .prestaciones(complejo.getPrestaciones())
                .build();
    }
}
