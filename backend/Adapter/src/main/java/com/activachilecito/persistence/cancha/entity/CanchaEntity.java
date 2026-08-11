package com.activachilecito.persistence.cancha.entity;

import com.activachilecito.persistence.complejo.entity.ComplejoEntity;
import com.activachilecito.persistence.reserva.entity.ReservaEntity;
import com.activachilecito.persistence.actividad.entity.ActividadEntity;
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
public class CanchaEntity {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_complejo", nullable = false)
    private ComplejoEntity complejo;

    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReservaEntity> reservas;

    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ActividadEntity> actividades;
}
