package usecase;

import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import lombok.RequiredArgsConstructor;
import output.ComplejoResponseDTO;

import java.util.Optional;

@RequiredArgsConstructor
public class ObtenerMiComplejoUseCase {

    private final ComplejoRepositoryPort complejoRepositoryPort;

    public Optional<ComplejoResponseDTO> obtener(Long idUsuarioDueno) {
        return complejoRepositoryPort.buscarPorDueno(idUsuarioDueno)
                .map(complejo -> ComplejoResponseDTO.builder()
                        .nombre(complejo.getNombre())
                        .direccion(complejo.getDireccion())
                        .telefono(complejo.getTelefono())
                        .prestaciones(complejo.getPrestaciones())
                        .build());
    }
}
