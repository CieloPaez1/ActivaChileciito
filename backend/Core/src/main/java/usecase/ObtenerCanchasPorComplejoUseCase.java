package usecase;

import com.activachilecito.core.complejo.exception.ComplejoException;
import com.activachilecito.core.complejo.model.Cancha;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.CanchaRepositoryPort;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import lombok.RequiredArgsConstructor;
import output.CanchaResponseDTO;
import output.HorarioResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ObtenerCanchasPorComplejoUseCase {

    private final ComplejoRepositoryPort complejoRepositoryPort;
    private final CanchaRepositoryPort canchaRepositoryPort;

    public List<CanchaResponseDTO> obtenerPorComplejo(Long complejoId) {
        if (complejoId == null) {
            throw new ComplejoException("El identificador del complejo no puede ser nulo.");
        }

        complejoRepositoryPort.buscarPorId(complejoId)
                .orElseThrow(() -> new ComplejoException("No se encontro el complejo con ID: " + complejoId));

        List<Cancha> canchas = canchaRepositoryPort.buscarPorComplejoId(complejoId);

        return canchas.stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    public List<CanchaResponseDTO> obtenerPorDueno(Long idUsuarioDueno) {
        if (idUsuarioDueno == null) {
            throw new ComplejoException("El identificador del usuario no puede ser nulo.");
        }

        Complejo complejo = complejoRepositoryPort.buscarPorUsuarioId(idUsuarioDueno)
                .orElseThrow(() -> new ComplejoException("No se encontro ningun complejo registrado para el usuario con ID: " + idUsuarioDueno));

        List<Cancha> canchas = canchaRepositoryPort.buscarPorComplejoId(complejo.getId());

        return canchas.stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    private CanchaResponseDTO mapearADto(Cancha cancha) {
        List<HorarioResponseDTO> horariosResponse = cancha.getHorariosDisponibilidad() != null
                ? cancha.getHorariosDisponibilidad().stream()
                .map(h -> HorarioResponseDTO.builder()
                        .id(h.getId())
                        .diaSemana(h.getDiaSemana())
                        .horaInicio(h.getHoraInicio())
                        .horaFin(h.getHoraFin())
                        .build())
                .collect(Collectors.toList())
                : new ArrayList<>();

        return CanchaResponseDTO.builder()
                .id(cancha.getId())
                .complejoId(cancha.getComplejoId())
                .nombre(cancha.getNombre())
                .deporte(cancha.getDeporte())
                .tipoSuelo(cancha.getTipoSuelo())
                .precioTurno(cancha.getPrecioTurno())
                .activa(cancha.getActiva())
                .horarios(horariosResponse)
                .build();
    }
}
