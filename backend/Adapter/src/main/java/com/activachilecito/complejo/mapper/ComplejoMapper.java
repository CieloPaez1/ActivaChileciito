package com.activachilecito.complejo.mapper;

import com.activachilecito.complejo.entity.dto.ComplejoDto;
import com.activachilecito.complejo.entity.data.ComplejoData;
import com.activachilecito.core.complejo.model.Complejo;
import org.springframework.stereotype.Component;

@Component
public class ComplejoMapper {

    public Complejo toDomain(ComplejoData entity) {
        if (entity == null) {
            return null;
        }
        return Complejo.restaurar(
                entity.getId(),
                entity.getNombre(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getPrestaciones(),
                new java.util.ArrayList<>()
        );
    }

    public ComplejoData toData(Complejo model) {
        if (model == null) {
            return null;
        }
        return new ComplejoData(
                model.getId(),
                model.getNombre(),
                model.getDireccion(),
                model.getTelefono(),
                model.getPrestaciones()
        );
    }

    public ComplejoDto toDto(Complejo model) {
        if (model == null) {
            return null;
        }
        return new ComplejoDto(
                model.getNombre(),
                model.getDireccion(),
                model.getTelefono(),
                model.getPrestaciones()
        );
    }
}
