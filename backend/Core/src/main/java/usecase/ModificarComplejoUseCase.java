package usecase;

import com.activachilecito.core.complejo.exception.ComplejoException;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import input.RegistrarComplejoRequest;
import lombok.RequiredArgsConstructor;
import output.ComplejoResponseDTO;

@RequiredArgsConstructor
public class ModificarComplejoUseCase {

    private final ComplejoRepositoryPort complejoRepositoryPort;

    public ComplejoResponseDTO modificar(Long idUsuarioDueno, RegistrarComplejoRequest request) {
        Complejo complejo = complejoRepositoryPort.buscarPorDueno(idUsuarioDueno)
                .orElseThrow(() -> new ComplejoException("No se encontró ningún complejo registrado para este usuario."));

        complejo.actualizar(
                request.getNombre(),
                request.getDireccion(),
                request.getTelefono(),
                request.getPrestaciones()
        );

        complejoRepositoryPort.guardar(idUsuarioDueno, complejo);

        return ComplejoResponseDTO.builder()
                .nombre(complejo.getNombre())
                .direccion(complejo.getDireccion())
                .telefono(complejo.getTelefono())
                .prestaciones(complejo.getPrestaciones())
                .build();
    }
}
