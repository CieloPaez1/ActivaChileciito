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
        validarCampos(nombre, direccion, telefono);
        return new Complejo(null, nombre, direccion, telefono, prestaciones, new ArrayList<>());
    }

    public static Complejo restaurar(Long id, String nombre, String direccion, String telefono, List<String> prestaciones, List<Cancha> canchas) {
        return new Complejo(id, nombre, direccion, telefono, prestaciones, canchas);
    }

    public void actualizar(String nuevoNombre, String nuevaDireccion, String nuevoTelefono, List<String> nuevasPrestaciones) {
        validarCampos(nuevoNombre, nuevaDireccion, nuevoTelefono);
        this.nombre = nuevoNombre;
        this.direccion = nuevaDireccion;
        this.telefono = nuevoTelefono;
        this.prestaciones = nuevasPrestaciones != null ? nuevasPrestaciones : new ArrayList<>();
    }

    private static void validarCampos(String nombre, String direccion, String telefono) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ComplejoException("El nombre del complejo no puede ser nulo ni vacío.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new ComplejoException("La dirección del complejo no puede ser nula ni vacía.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ComplejoException("El teléfono del complejo no puede ser nulo ni vacío.");
        }
    }
    
    public void agregarCancha(Cancha cancha) {
        this.canchas.add(cancha);
    }
}
