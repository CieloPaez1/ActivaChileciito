package usecase;

import com.activachilecito.core.complejo.exception.ComplejoException;
import com.activachilecito.core.complejo.model.Cancha;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.model.HorarioDisponibilidad;
import com.activachilecito.core.complejo.output.CanchaRepositoryPort;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import exception.ExcepcionUsuarioNoEncontrado;
import input.HorarioRequest;
import input.RegistrarCanchaRequest;
import lombok.RequiredArgsConstructor;
import model.RolUsuario;
import model.Usuario;
import output.CanchaResponseDTO;
import output.HorarioResponseDTO;
import output.UsuarioOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RegistrarCanchaUseCase {

    private final ComplejoRepositoryPort complejoRepositoryPort;
    private final CanchaRepositoryPort canchaRepositoryPort;
    private final UsuarioOutput usuarioOutput;

    public CanchaResponseDTO registrar(Long idUsuarioAutenticado, RegistrarCanchaRequest request) {
        if (request == null) {
            throw new ComplejoException("La solicitud para registrar la cancha no puede ser nula.");
        }

        Usuario usuario = usuarioOutput.buscarPorId(idUsuarioAutenticado)
                .orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado con ID: " + idUsuarioAutenticado));

        if (usuario.getRol() != RolUsuario.DUENO_DE_COMPLEJO) {
            throw new ComplejoException("Operacion denegada. Solo los propietarios de complejos pueden registrar canchas.");
        }

        Complejo complejo;
        if (request.getComplejoId() != null) {
            complejo = complejoRepositoryPort.buscarPorId(request.getComplejoId())
                    .orElseThrow(() -> new ComplejoException("No se encontro el complejo con ID: " + request.getComplejoId()));
            if (complejo.getUsuarioId() != null && !complejo.getUsuarioId().equals(idUsuarioAutenticado)) {
                throw new ComplejoException("Operacion denegada. El usuario no es propietario del complejo indicado.");
            }
        } else {
            complejo = complejoRepositoryPort.buscarPorUsuarioId(idUsuarioAutenticado)
                    .orElseThrow(() -> new ComplejoException("No se encontro ningun complejo registrado para este propietario. Debe registrar un complejo previamente."));
        }

        if (canchaRepositoryPort.existePorNombreYComplejoId(request.getNombre(), complejo.getId())) {
            throw new ComplejoException("Ya existe una cancha con el nombre '" + request.getNombre() + "' en este complejo.");
        }

        Cancha cancha = Cancha.crear(
                complejo.getId(),
                request.getNombre(),
                request.getDeporte(),
                request.getTipoSuelo(),
                request.getPrecioTurno()
        );

        if (request.getHorarios() != null && !request.getHorarios().isEmpty()) {
            for (HorarioRequest h : request.getHorarios()) {
                HorarioDisponibilidad horario = HorarioDisponibilidad.crear(
                        h.getDiaSemana(),
                        h.getHoraInicio(),
                        h.getHoraFin()
                );
                cancha.agregarHorario(horario);
            }
        }

        Cancha canchaGuardada = canchaRepositoryPort.guardar(cancha);

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
