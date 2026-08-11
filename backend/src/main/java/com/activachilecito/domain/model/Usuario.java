package com.activachilecito.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol;

    // Relación One-to-Many con Reserva
    // Un usuario puede tener muchas reservas
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;

    // Relación One-to-Many con Actividad
    // Un usuario (Deportista) puede crear muchas actividades
    @OneToMany(mappedBy = "creador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Actividad> actividadesCreadas;

    // Relación One-to-Many con Complejo
    // Un usuario (Administrador) puede administrar muchos complejos
    @OneToMany(mappedBy = "administrador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Complejo> complejosAdministrados;
}
