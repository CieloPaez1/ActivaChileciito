package com.activachilecito.mapper;

import com.activachilecito.core.model.Usuario;
import com.activachilecito.entity.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public Usuario toDomain(UsuarioData data) {
        if (data == null) return null;
        return Usuario.builder()
                .id(data.getId())
                .nombre(data.getNombre())
                .apellido(data.getApellido())
                .email(data.getEmail())
                .password(data.getPassword())
                .roles(data.getRoles())
                .estado(data.getEstado())
                .fechaRegistro(data.getFechaRegistro())
                .telefono(data.getTelefono())
                .build();
    }

    public UsuarioData toData(Usuario domain) {
        if (domain == null) return null;
        return UsuarioData.builder()
                .id(domain.getId())
                .nombre(domain.getNombre())
                .apellido(domain.getApellido())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .roles(domain.getRoles())
                .estado(domain.getEstado())
                .fechaRegistro(domain.getFechaRegistro())
                .telefono(domain.getTelefono())
                .build();
    }
}
