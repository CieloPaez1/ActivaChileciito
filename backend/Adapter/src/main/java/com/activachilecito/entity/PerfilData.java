package com.activachilecito.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "perfiles_deportivos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long usuarioId;

    private String deportePreferido;
    private String nivel;
    private String lesiones;
}
