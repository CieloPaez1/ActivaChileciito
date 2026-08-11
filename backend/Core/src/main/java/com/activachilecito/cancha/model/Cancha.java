package com.activachilecito.cancha.model;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cancha {
    private Long idCancha;
    private String nombre;
    private String tipoSuperficie;
    private String deporte;
    private BigDecimal precioHora;
    private Long idComplejo;
}
