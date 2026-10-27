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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GestionarComplejoUseCase {

    private final ComplejoRepositoryPort complejoRepositoryPort;
    private final UsuarioOutput usuarioOutput;

    public Optional<ComplejoResponseDTO> obtenerPorDueno(Long idUsuarioDueno) {
        if (idUsuarioDueno == null) {
            throw new ComplejoException("El identificador del usuario no puede ser nulo.");
        }
        return complejoRepositoryPort.buscarPorUsuarioId(idUsuarioDueno)
                .map(this::mapearADto);
    }

    public ComplejoResponseDTO actualizarOCrearPorDueno(Long idUsuarioDueno, RegistrarComplejoRequest request) {
        if (request == null) {
            throw new ComplejoException("Los datos del complejo no pueden ser nulos.");
        }

        Usuario usuario = usuarioOutput.buscarPorId(idUsuarioDueno)
                .orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado con ID: " + idUsuarioDueno));

        if (usuario.getRol() != RolUsuario.DUENO_DE_COMPLEJO) {
            throw new ComplejoException("Operacion denegada. Solo los propietarios pueden gestionar un complejo deportivo.");
        }

        Optional<Complejo> existenteOpt = complejoRepositoryPort.buscarPorUsuarioId(idUsuarioDueno);
        Complejo complejo;

        if (existenteOpt.isPresent()) {
            complejo = existenteOpt.get();
            complejo.actualizarDatos(
                    request.getNombre(),
                    request.getDireccion(),
                    request.getTelefono(),
                    request.getPrestaciones()
            );
            complejoRepositoryPort.guardar(idUsuarioDueno, complejo);
        } else {
            complejo = Complejo.crear(
                    idUsuarioDueno,
                    request.getNombre(),
                    request.getDireccion(),
                    request.getTelefono(),
                    request.getPrestaciones()
            );
            complejoRepositoryPort.guardar(idUsuarioDueno, complejo);
        }

        return complejoRepositoryPort.buscarPorUsuarioId(idUsuarioDueno)
                .map(this::mapearADto)
                .orElseGet(() -> mapearADto(complejo));
    }

    public List<ComplejoResponseDTO> obtenerTodos() {
        return complejoRepositoryPort.obtenerTodos().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private ComplejoResponseDTO mapearADto(Complejo complejo) {
        return ComplejoResponseDTO.builder()
                .id(complejo.getId())
                .usuarioId(complejo.getUsuarioId())
                .nombre(complejo.getNombre())
                .direccion(complejo.getDireccion())
                .telefono(complejo.getTelefono())
                .prestaciones(complejo.getPrestaciones())
                .build();
    }
}
