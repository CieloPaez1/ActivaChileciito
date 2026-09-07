package com.activachilecito.usuarios.mapper;

import com.activachilecito.usuarios.entity.DTO.UsuarioDto;
import com.activachilecito.usuarios.entity.data.UsuarioData;
import model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    // 1. Traduce de Base de Datos (Adapter) a Negocio Puro (Core)
    public Usuario toDomain(UsuarioData data) {
        if (data == null) return null;
        return  Usuario.restaurar(
                data.getId(),
                data.getNombre(),
                data.getApellido(),
                data.getEmail(),
                data.getPassword(),
                data.getRol(),
                data.getTelefono(),
                data.isActivo()
        );
    }

    // 2. Traduce de Negocio Puro (Core) a Base de Datos (Adapter)
    public UsuarioData toData(Usuario domain) {
        if (domain == null) return null;
        return new UsuarioData(
                domain.getId(),
                domain.getNombre(),
                domain.getApellido(),
                domain.getEmail(),
                domain.getPassword(),
                domain.getRol(),
                domain.getTelefono(),
                domain.isActivo()
        );
    }

    // 3. Traduce de Negocio Puro (Core) a Respuesta para Angular (Web) - ¡SIN PASSWORD!
    public UsuarioDto toDto(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getRol(),
                usuario.getTelefono(),
                usuario.isActivo()
        );
    }

}
