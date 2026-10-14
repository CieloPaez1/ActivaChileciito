package com.activachilecito.complejo.entity.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "canchas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CanchaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "complejo_id", nullable = false)
    private Long complejoId;

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "deporte", nullable = false, length = 50)
    private String deporte;

    @Column(name = "tipo_suelo", nullable = false, length = 50)
    private String tipoSuelo;

    @Column(name = "precio_turno", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTurno;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

    @OneToMany(mappedBy = "cancha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HorarioDisponibilidadData> horarios = new ArrayList<>();

    public void agregarHorario(HorarioDisponibilidadData horario) {
        horarios.add(horario);
        horario.setCancha(this);
    }

    public void removerHorario(HorarioDisponibilidadData horario) {
        horarios.remove(horario);
        horario.setCancha(null);
    }
}
