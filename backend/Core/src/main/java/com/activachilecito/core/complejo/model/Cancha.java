package com.activachilecito.core.complejo.model;

import com.activachilecito.core.complejo.exception.ComplejoException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Cancha {
    private Long id;
    private String nombre; // e.g. "Cancha 1", "Principal"
    private String tipo; // e.g. "FÃºtbol 5", "PÃ¡del", "Tenis"
    private Boolean techada;
    private Long complejoId;
    private List<HorarioDisponibilidad> horariosDisponibilidad;

    private Cancha(Long id, String nombre, String tipo, Boolean techada, Long complejoId, List<HorarioDisponibilidad> horariosDisponibilidad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.techada = techada != null ? techada : false;
        this.complejoId = complejoId;
        this.horariosDisponibilidad = horariosDisponibilidad != null ? horariosDisponibilidad : new ArrayList<>();
    }

    public static Cancha crear(String nombre, String tipo, Boolean techada, Long complejoId) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ComplejoException("El nombre de la cancha no puede estar vacÃ­o.");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new ComplejoException("Debe especificar el tipo de cancha (ej. FÃºtbol 5).");
        }
        return new Cancha(null, nombre, tipo, techada, complejoId, new ArrayList<>());
    }

    public static Cancha restaurar(Long id, String nombre, String tipo, Boolean techada, Long complejoId, List<HorarioDisponibilidad> horarios) {
        return new Cancha(id, nombre, tipo, techada, complejoId, horarios);
    }
    
    public void agregarHorario(HorarioDisponibilidad horario) {
        this.horariosDisponibilidad.add(horario);
    }
}
