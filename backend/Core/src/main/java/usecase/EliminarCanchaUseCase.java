package usecase;

import com.activachilecito.core.complejo.exception.ComplejoException;
import com.activachilecito.core.complejo.model.Cancha;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.CanchaRepositoryPort;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EliminarCanchaUseCase {

    private final ComplejoRepositoryPort complejoRepositoryPort;
    private final CanchaRepositoryPort canchaRepositoryPort;

    public void eliminar(Long idUsuarioAutenticado, Long canchaId) {
        if (canchaId == null) {
            throw new ComplejoException("El identificador de la cancha no puede ser nulo.");
        }

        Cancha cancha = canchaRepositoryPort.buscarPorId(canchaId)
                .orElseThrow(() -> new ComplejoException("No se encontro la cancha con ID: " + canchaId));

        Complejo complejo = complejoRepositoryPort.buscarPorUsuarioId(idUsuarioAutenticado)
                .orElseThrow(() -> new ComplejoException("No se encontro ningun complejo registrado para el propietario actual."));

        if (!cancha.getComplejoId().equals(complejo.getId())) {
            throw new ComplejoException("Operacion denegada. La cancha no pertenece al complejo del propietario actual.");
        }

        canchaRepositoryPort.eliminar(canchaId);
    }
}
