package com.activachilecito.core.complejo.model;

import com.activachilecito.core.complejo.exception.ComplejoException;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class HorarioDisponibilidad {
    private Long id;
    private Integer diaSemana; // 1 = Lunes, 7 = Domingo
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Long canchadId;

    private HorarioDisponibilidad(Long id, Integer diaSemana, LocalTime horaInicio, LocalTime horaFin, Long canchadId) {
        this.id = id;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.canchadId = canchadId;
    }

    public static HorarioDisponibilidad crear(Integer diaSemana, LocalTime horaInicio, LocalTime horaFin, Long canchadId) {
        if (diaSemana == null || diaSemana < 1 || diaSemana > 7) {
            throw new ComplejoException("El dÃ­a de la semana debe ser entre 1 (Lunes) y 7 (Domingo).");
        }
        if (horaInicio == null || horaFin == null) {
            throw new ComplejoException("Las horas de inicio y fin no pueden ser nulas.");
        }
        if (horaInicio.isAfter(horaFin)) {
            throw new ComplejoException("La hora de inicio no puede ser posterior a la hora de fin.");
        }
        return new HorarioDisponibilidad(null, diaSemana, horaInicio, horaFin, canchadId);
    }

    public static HorarioDisponibilidad restaurar(Long id, Integer diaSemana, LocalTime horaInicio, LocalTime horaFin, Long canchadId) {
        return new HorarioDisponibilidad(id, diaSemana, horaInicio, horaFin, canchadId);
    }
}
