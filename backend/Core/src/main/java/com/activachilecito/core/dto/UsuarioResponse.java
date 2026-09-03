package com.activachilecito.core.dto;

import com.activachilecito.core.model.EstadoUsuario;
import com.activachilecito.core.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Set<Rol> roles;
    private EstadoUsuario estado;
    
    // Datos del perfil
    private String deportePreferido;
    private String nivel;
    private String lesiones;
}
