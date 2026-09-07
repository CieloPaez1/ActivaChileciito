package com.activachilecito.usuarios.entity.data;

import jakarta.persistence.*;
import lombok.*;
import model.RolUsuario;

@Data // Esta anotación mágica genera todos los getters, setters, toString, equals y hashCode
@NoArgsConstructor // Genera el constructor vacío (obligatorio para JPA)
@AllArgsConstructor // Genera el constructor con todos los parámetros
@Entity
@Table(name = "usuarios")
public class UsuarioData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    // El email debe ser único en la base de datos
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    // Le decimos a JPA que guarde el Enum como texto (String) y no como un número
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolUsuario rol;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false)
    private boolean activo;


}