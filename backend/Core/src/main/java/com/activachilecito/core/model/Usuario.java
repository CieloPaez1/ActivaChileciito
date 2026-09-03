package com.activachilecito.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    
    @Builder.Default
    private Set<Rol> roles = new HashSet<>();
    
    @Builder.Default
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;
    
    private LocalDateTime fechaRegistro;
    
    private String telefono;
}
