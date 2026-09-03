package com.activachilecito.mapper;

import com.activachilecito.core.model.PerfilDeportivo;
import com.activachilecito.entity.PerfilData;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {
    public PerfilDeportivo toDomain(PerfilData data) {
        if (data == null) return null;
        return PerfilDeportivo.builder()
                .id(data.getId())
                .usuarioId(data.getUsuarioId())
                .deportePreferido(data.getDeportePreferido())
                .nivel(data.getNivel())
                .lesiones(data.getLesiones())
                .build();
    }

    public PerfilData toData(PerfilDeportivo domain) {
        if (domain == null) return null;
        return PerfilData.builder()
                .id(domain.getId())
                .usuarioId(domain.getUsuarioId())
                .deportePreferido(domain.getDeportePreferido())
                .nivel(domain.getNivel())
                .lesiones(domain.getLesiones())
                .build();
    }
}
