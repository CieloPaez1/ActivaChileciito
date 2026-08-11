package com.activachilecito.persistence.complejo.entity;

import com.activachilecito.persistence.usuario.entity.UsuarioEntity;
import com.activachilecito.persistence.cancha.entity.CanchaEntity;
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
public class ComplejoEntity {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_admin", nullable = false)
    private UsuarioEntity administrador;

    @OneToMany(mappedBy = "complejo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CanchaEntity> canchas;
}
