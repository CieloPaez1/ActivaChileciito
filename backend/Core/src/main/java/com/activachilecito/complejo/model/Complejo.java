package com.activachilecito.complejo.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complejo {
    private Long idComplejo;
    private String nombre;
    private String direccion;
    private String telefono;
    private Long idUsuarioAdmin;
}
