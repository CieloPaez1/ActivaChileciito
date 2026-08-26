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

    private Complejo(Long id, String nombre, String direccion, String telefono, List<String> prestaciones) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.prestaciones = prestaciones != null ? prestaciones : new ArrayList<>();
    }

    public static Complejo crear(String nombre, String direccion, String telefono, List<String> prestaciones) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ComplejoException("El nombre del complejo no puede ser nulo ni vacío.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new ComplejoException("La dirección del complejo no puede ser nula ni vacía.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ComplejoException("El teléfono del complejo no puede ser nulo ni vacío.");
        }
        return new Complejo(null, nombre, direccion, telefono, prestaciones);
    }

    public static Complejo restaurar(Long id, String nombre, String direccion, String telefono, List<String> prestaciones) {
        return new Complejo(id, nombre, direccion, telefono, prestaciones);
    }

}
