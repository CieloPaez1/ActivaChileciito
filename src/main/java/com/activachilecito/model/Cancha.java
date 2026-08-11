package com.activachilecito.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "canchas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cancha")
    private Long idCancha;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_superficie", length = 100)
    private String tipoSuperficie;

    @Column(nullable = false, length = 100)
    private String deporte;

    @Column(name = "precio_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioHora;

    // Relación Many-to-One con Complejo
    // Muchas canchas pertenecen a un complejo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_complejo", nullable = false)
    private Complejo complejo;

    // Relación One-to-Many con Reserva
    // Una cancha puede tener muchas reservas
    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;

    // Relación One-to-Many con Actividad
    // Una cancha puede albergar muchas actividades
    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Actividad> actividades;
}
