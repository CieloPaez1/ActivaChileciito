package com.activachilecito.core.complejo.model;

import com.activachilecito.core.complejo.exception.ComplejoException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Complejo {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private List<String> prestaciones;
    private List<Cancha> canchas;

    private Complejo(Long id, String nombre, String direccion, String telefono, List<String> prestaciones, List<Cancha> canchas) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.prestaciones = prestaciones != null ? prestaciones : new ArrayList<>();
        this.canchas = canchas != null ? canchas : new ArrayList<>();
    }

    public static Complejo crear(String nombre, String direccion, String telefono, List<String> prestaciones) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ComplejoException("El nombre del complejo no puede ser nulo ni vacÃ­o.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new ComplejoException("La direcciÃ³n del complejo no puede ser nula ni vacÃ­a.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ComplejoException("El telÃ©fono del complejo no puede ser nulo ni vacÃ­o.");
        }
        return new Complejo(null, nombre, direccion, telefono, prestaciones, new ArrayList<>());
    }

    public static Complejo restaurar(Long id, String nombre, String direccion, String telefono, List<String> prestaciones, List<Cancha> canchas) {
        return new Complejo(id, nombre, direccion, telefono, prestaciones, canchas);
    }
    
    public void agregarCancha(Cancha cancha) {
        this.canchas.add(cancha);
    }
}
