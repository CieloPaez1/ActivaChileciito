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
    private Long canchaId;

    private HorarioDisponibilidad(Long id, Integer diaSemana, LocalTime horaInicio, LocalTime horaFin, Long canchaId) {
        this.id = id;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.canchaId = canchaId;
    }

    public static HorarioDisponibilidad crear(Integer diaSemana, LocalTime horaInicio, LocalTime horaFin, Long canchaId) {
        validarRangoHorario(diaSemana, horaInicio, horaFin);
        return new HorarioDisponibilidad(null, diaSemana, horaInicio, horaFin, canchaId);
    }

    public static HorarioDisponibilidad crear(Integer diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        return crear(diaSemana, horaInicio, horaFin, null);
    }

    public static HorarioDisponibilidad restaurar(Long id, Integer diaSemana, LocalTime horaInicio, LocalTime horaFin, Long canchaId) {
        return new HorarioDisponibilidad(id, diaSemana, horaInicio, horaFin, canchaId);
    }

    public static void validarRangoHorario(Integer diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        if (diaSemana == null || diaSemana < 1 || diaSemana > 7) {
            throw new ComplejoException("El dia de la semana debe estar comprendido entre 1 (Lunes) y 7 (Domingo).");
        }
        if (horaInicio == null || horaFin == null) {
            throw new ComplejoException("Las horas de inicio y fin no pueden ser nulas.");
        }
        if (!horaInicio.isBefore(horaFin)) {
            throw new ComplejoException("La hora de inicio debe ser estrictamente anterior a la hora de fin.");
        }
    }

    public void asignarCanchaId(Long canchaId) {
        this.canchaId = canchaId;
    }
}
