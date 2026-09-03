package com.activachilecito.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilDeportivo {
    private Long id;
    private Long usuarioId; // Reference by ID instead of object to keep it decoupled
    private String deportePreferido;
    private String nivel;
    private String lesiones;
}
