package com.activachilecito.core.dto;

import lombok.Data;

@Data
public class ActualizarPerfilRequest {
    private String nombre;
    private String apellido;
    private String telefono;
    private String deportePreferido;
    private String nivel;
    private String lesiones;
}
