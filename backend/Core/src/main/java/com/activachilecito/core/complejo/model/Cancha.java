package com.activachilecito.core.complejo.model;

import com.activachilecito.core.complejo.exception.ComplejoException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Cancha {
    private Long id;
    private Long complejoId;
    private String nombre; // e.g. "Cancha 1", "Principal"
    private String deporte; // e.g. "Futbol 5", "Padel", "Tenis"
    private String tipoSuelo; // e.g. "Sintetico", "Cemento", "Cesped"
    private BigDecimal precioTurno;
    private Boolean activa;
    private List<HorarioDisponibilidad> horariosDisponibilidad;

    private Cancha(Long id, Long complejoId, String nombre, String deporte, String tipoSuelo, BigDecimal precioTurno, Boolean activa, List<HorarioDisponibilidad> horariosDisponibilidad) {
        this.id = id;
        this.complejoId = complejoId;
        this.nombre = nombre;
        this.deporte = deporte;
        this.tipoSuelo = tipoSuelo;
        this.precioTurno = precioTurno;
        this.activa = activa != null ? activa : true;
        this.horariosDisponibilidad = horariosDisponibilidad != null ? horariosDisponibilidad : new ArrayList<>();
    }

    public static Cancha crear(Long complejoId, String nombre, String deporte, String tipoSuelo, BigDecimal precioTurno) {
        if (complejoId == null) {
            throw new ComplejoException("El identificador del complejo no puede ser nulo.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ComplejoException("El nombre de la cancha no puede estar vacio.");
        }
        if (deporte == null || deporte.trim().isEmpty()) {
            throw new ComplejoException("Debe especificar el deporte de la cancha (ej. Futbol 5, Padel).");
        }
        if (tipoSuelo == null || tipoSuelo.trim().isEmpty()) {
            throw new ComplejoException("Debe especificar el tipo de suelo de la cancha (ej. Sintetico).");
        }
        if (precioTurno == null || precioTurno.compareTo(BigDecimal.ZERO) < 0) {
            throw new ComplejoException("El precio del turno no puede ser nulo ni negativo.");
        }
        return new Cancha(null, complejoId, nombre.trim(), deporte.trim(), tipoSuelo.trim(), precioTurno, true, new ArrayList<>());
    }

    public static Cancha restaurar(Long id, Long complejoId, String nombre, String deporte, String tipoSuelo, BigDecimal precioTurno, Boolean activa, List<HorarioDisponibilidad> horarios) {
        return new Cancha(id, complejoId, nombre, deporte, tipoSuelo, precioTurno, activa, horarios);
    }

    public void agregarHorario(HorarioDisponibilidad horario) {
        if (horario == null) {
            throw new ComplejoException("No se puede agregar un horario de disponibilidad nulo.");
        }
        this.horariosDisponibilidad.add(horario);
    }

    public boolean verificarDisponibilidad(Integer diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        if (diaSemana == null || horaInicio == null || horaFin == null) {
            return false;
        }
        if (!activa) {
            return false;
        }
        return horariosDisponibilidad.stream()
                .anyMatch(h -> h.getDiaSemana().equals(diaSemana)
                        && !horaInicio.isBefore(h.getHoraInicio())
                        && !horaFin.isAfter(h.getHoraFin()));
    }

    public void desactivar() {
        this.activa = false;
    }

    public void activar() {
        this.activa = true;
    }
}
