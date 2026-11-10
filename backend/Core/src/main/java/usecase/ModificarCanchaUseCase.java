package usecase;

import com.activachilecito.core.complejo.exception.ComplejoException;
import com.activachilecito.core.complejo.model.Cancha;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.model.HorarioDisponibilidad;
import com.activachilecito.core.complejo.output.CanchaRepositoryPort;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import input.HorarioRequest;
import input.ModificarCanchaRequest;
import lombok.RequiredArgsConstructor;
import output.CanchaResponseDTO;
import output.HorarioResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ModificarCanchaUseCase {

    private final ComplejoRepositoryPort complejoRepositoryPort;
    private final CanchaRepositoryPort canchaRepositoryPort;

    public CanchaResponseDTO modificar(Long idUsuarioAutenticado, Long canchaId, ModificarCanchaRequest request) {
        if (canchaId == null) {
            throw new ComplejoException("El identificador de la cancha no puede ser nulo.");
        }
        if (request == null) {
            throw new ComplejoException("La solicitud de modificacion no puede ser nula.");
        }

        Cancha canchaExistente = canchaRepositoryPort.buscarPorId(canchaId)
                .orElseThrow(() -> new ComplejoException("No se encontro la cancha con ID: " + canchaId));

        Complejo complejo = complejoRepositoryPort.buscarPorUsuarioId(idUsuarioAutenticado)
                .orElseThrow(() -> new ComplejoException("No se encontro ningun complejo registrado para el propietario actual."));

        if (!canchaExistente.getComplejoId().equals(complejo.getId())) {
            throw new ComplejoException("Operacion denegada. La cancha no pertenece al complejo del propietario actual.");
        }

        // Construir la cancha actualizada con los datos nuevos (manteniendo los existentes si no se proveen)
        String nuevoNombre = (request.getNombre() != null && !request.getNombre().trim().isEmpty())
                ? request.getNombre().trim() : canchaExistente.getNombre();
        String nuevoDeporte = (request.getDeporte() != null && !request.getDeporte().trim().isEmpty())
                ? request.getDeporte().trim() : canchaExistente.getDeporte();
        String nuevoTipoSuelo = (request.getTipoSuelo() != null && !request.getTipoSuelo().trim().isEmpty())
                ? request.getTipoSuelo().trim() : canchaExistente.getTipoSuelo();
        java.math.BigDecimal nuevoPrecio = (request.getPrecioTurno() != null && request.getPrecioTurno().compareTo(java.math.BigDecimal.ZERO) > 0)
                ? request.getPrecioTurno() : canchaExistente.getPrecioTurno();
        Boolean nuevaActiva = (request.getActiva() != null) ? request.getActiva() : canchaExistente.getActiva();

        Cancha canchaActualizada = Cancha.restaurar(
                canchaExistente.getId(),
                canchaExistente.getComplejoId(),
                nuevoNombre,
                nuevoDeporte,
                nuevoTipoSuelo,
                nuevoPrecio,
                nuevaActiva,
                new ArrayList<>()
        );

        // Reemplazar horarios si se proveen nuevos o mantener los existentes
        if (request.getHorarios() != null) {
            for (HorarioRequest h : request.getHorarios()) {
                HorarioDisponibilidad horario = HorarioDisponibilidad.crear(
                        h.getDiaSemana(),
                        h.getHoraInicio(),
                        h.getHoraFin()
                );
                canchaActualizada.agregarHorario(horario);
            }
        } else if (canchaExistente.getHorariosDisponibilidad() != null) {
            for (HorarioDisponibilidad h : canchaExistente.getHorariosDisponibilidad()) {
                canchaActualizada.agregarHorario(h);
            }
        }

        Cancha canchaGuardada = canchaRepositoryPort.actualizar(canchaExistente.getId(), canchaActualizada);

        List<HorarioResponseDTO> horariosResponse = canchaGuardada.getHorariosDisponibilidad() != null
                ? canchaGuardada.getHorariosDisponibilidad().stream()
                .map(h -> HorarioResponseDTO.builder()
                        .id(h.getId())
                        .diaSemana(h.getDiaSemana())
                        .horaInicio(h.getHoraInicio())
                        .horaFin(h.getHoraFin())
                        .build())
                .collect(Collectors.toList())
                : new ArrayList<>();

        return CanchaResponseDTO.builder()
                .id(canchaGuardada.getId())
                .complejoId(canchaGuardada.getComplejoId())
                .nombre(canchaGuardada.getNombre())
                .deporte(canchaGuardada.getDeporte())
                .tipoSuelo(canchaGuardada.getTipoSuelo())
                .precioTurno(canchaGuardada.getPrecioTurno())
                .activa(canchaGuardada.getActiva())
                .horarios(horariosResponse)
                .build();
    }
}
