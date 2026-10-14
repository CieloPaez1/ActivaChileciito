package com.activachilecito.core.complejo.model;

import com.activachilecito.core.complejo.exception.ComplejoException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Complejo {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String direccion;
    private String telefono;
    private List<String> prestaciones;
    private List<Cancha> canchas;

    private Complejo(Long id, Long usuarioId, String nombre, String direccion, String telefono, List<String> prestaciones, List<Cancha> canchas) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.prestaciones = prestaciones != null ? prestaciones : new ArrayList<>();
        this.canchas = canchas != null ? canchas : new ArrayList<>();
    }

    public static Complejo crear(Long usuarioId, String nombre, String direccion, String telefono, List<String> prestaciones) {
        validarCampos(nombre, direccion, telefono);
        return new Complejo(null, usuarioId, nombre.trim(), direccion.trim(), telefono.trim(), prestaciones, new ArrayList<>());
    }

    public static Complejo crear(String nombre, String direccion, String telefono, List<String> prestaciones) {
        return crear(null, nombre, direccion, telefono, prestaciones);
    }

    public static Complejo restaurar(Long id, Long usuarioId, String nombre, String direccion, String telefono, List<String> prestaciones, List<Cancha> canchas) {
        return new Complejo(id, usuarioId, nombre, direccion, telefono, prestaciones, canchas);
    }

    public static Complejo restaurar(Long id, String nombre, String direccion, String telefono, List<String> prestaciones, List<Cancha> canchas) {
        return new Complejo(id, null, nombre, direccion, telefono, prestaciones, canchas);
    }

    private static void validarCampos(String nombre, String direccion, String telefono) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ComplejoException("El nombre del complejo no puede ser nulo ni vacio.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new ComplejoException("La direccion del complejo no puede ser nula ni vacia.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ComplejoException("El telefono del complejo no puede ser nulo ni vacio.");
        }
    }

    public void asignarUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void agregarCancha(Cancha cancha) {
        if (cancha == null) {
            throw new ComplejoException("No se puede asociar una cancha nula al complejo.");
        }
        this.canchas.add(cancha);
    }
}
