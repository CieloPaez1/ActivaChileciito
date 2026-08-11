package com.activachilecito.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "actividades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad")
    private Long idActividad;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "cupo_maximo", nullable = false)
    private Integer cupoMaximo;

    // Relación Many-to-One con Usuario (creador de la actividad)
    // Muchas actividades son creadas por un usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_creador", nullable = false)
    private Usuario creador;

    // Relación Many-to-One con Cancha
    // Muchas actividades se realizan en una cancha
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cancha", nullable = false)
    private Cancha cancha;
}
