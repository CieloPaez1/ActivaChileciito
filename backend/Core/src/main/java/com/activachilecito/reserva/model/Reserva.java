package com.activachilecito.reserva.model;

import com.activachilecito.reserva.enums.EstadoReserva;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {
    private Long idReserva;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoReserva estado;
    private Long idUsuario;
    private Long idCancha;
}
