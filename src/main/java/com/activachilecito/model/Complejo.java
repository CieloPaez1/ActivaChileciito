package com.activachilecito.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "complejos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complejo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_complejo")
    private Long idComplejo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    // Relación Many-to-One con Usuario (Administrador)
    // Muchos complejos pueden ser administrados por un mismo usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_admin", nullable = false)
    private Usuario administrador;

    // Relación One-to-Many con Cancha
    // Un complejo tiene muchas canchas
    @OneToMany(mappedBy = "complejo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cancha> canchas;
}
