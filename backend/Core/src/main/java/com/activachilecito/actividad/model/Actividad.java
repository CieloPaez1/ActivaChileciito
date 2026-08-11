package com.activachilecito.actividad.model;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actividad {
    private Long idActividad;
    private String titulo;
    private String descripcion;
    private LocalDateTime fecha;
    private Integer cupoMaximo;
    private Long idCreador;
    private Long idCancha;
}
