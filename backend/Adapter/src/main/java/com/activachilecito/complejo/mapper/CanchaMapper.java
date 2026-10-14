package com.activachilecito.complejo.mapper;

import com.activachilecito.complejo.entity.data.CanchaData;
import com.activachilecito.complejo.entity.data.HorarioDisponibilidadData;
import com.activachilecito.core.complejo.model.Cancha;
import com.activachilecito.core.complejo.model.HorarioDisponibilidad;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CanchaMapper {

    public Cancha toDomain(CanchaData entity) {
        if (entity == null) {
            return null;
        }

        List<HorarioDisponibilidad> horarios = entity.getHorarios() != null
                ? entity.getHorarios().stream()
                .map(this::toDomainHorario)
                .collect(Collectors.toList())
                : new ArrayList<>();

        return Cancha.restaurar(
                entity.getId(),
                entity.getComplejoId(),
                entity.getNombre(),
                entity.getDeporte(),
                entity.getTipoSuelo(),
                entity.getPrecioTurno(),
                entity.getActiva(),
                horarios
        );
    }

    public CanchaData toData(Cancha model) {
        if (model == null) {
            return null;
        }

        CanchaData entity = new CanchaData();
        entity.setId(model.getId());
        entity.setComplejoId(model.getComplejoId());
        entity.setNombre(model.getNombre());
        entity.setDeporte(model.getDeporte());
        entity.setTipoSuelo(model.getTipoSuelo());
        entity.setPrecioTurno(model.getPrecioTurno());
        entity.setActiva(model.getActiva() != null ? model.getActiva() : true);

        if (model.getHorariosDisponibilidad() != null) {
            for (HorarioDisponibilidad h : model.getHorariosDisponibilidad()) {
                HorarioDisponibilidadData hData = toDataHorario(h);
                entity.agregarHorario(hData);
            }
        }

        return entity;
    }

    public HorarioDisponibilidad toDomainHorario(HorarioDisponibilidadData data) {
        if (data == null) {
            return null;
        }
        Long canchaId = data.getCancha() != null ? data.getCancha().getId() : null;
        return HorarioDisponibilidad.restaurar(
                data.getId(),
                data.getDiaSemana(),
                data.getHoraInicio(),
                data.getHoraFin(),
                canchaId
        );
    }

    public HorarioDisponibilidadData toDataHorario(HorarioDisponibilidad model) {
        if (model == null) {
            return null;
        }
        HorarioDisponibilidadData data = new HorarioDisponibilidadData();
        data.setId(model.getId());
        data.setDiaSemana(model.getDiaSemana());
        data.setHoraInicio(model.getHoraInicio());
        data.setHoraFin(model.getHoraFin());
        return data;
    }
}
