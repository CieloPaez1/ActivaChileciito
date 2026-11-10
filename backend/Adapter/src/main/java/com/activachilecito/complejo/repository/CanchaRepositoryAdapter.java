package com.activachilecito.complejo.repository;

import com.activachilecito.complejo.crud.CanchaCrud;
import com.activachilecito.complejo.entity.data.CanchaData;
import com.activachilecito.complejo.entity.data.HorarioDisponibilidadData;
import com.activachilecito.complejo.mapper.CanchaMapper;
import com.activachilecito.core.complejo.model.Cancha;
import com.activachilecito.core.complejo.model.HorarioDisponibilidad;
import com.activachilecito.core.complejo.output.CanchaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CanchaRepositoryAdapter implements CanchaRepositoryPort {

    private final CanchaCrud canchaCrud;
    private final CanchaMapper canchaMapper;

    public CanchaRepositoryAdapter(CanchaCrud canchaCrud, CanchaMapper canchaMapper) {
        this.canchaCrud = canchaCrud;
        this.canchaMapper = canchaMapper;
    }

    @Override
    @Transactional
    public Cancha guardar(Cancha cancha) {
        CanchaData data = canchaMapper.toData(cancha);
        CanchaData guardada = canchaCrud.save(data);
        return canchaMapper.toDomain(guardada);
    }

    @Override
    @Transactional
    public Cancha actualizar(Long id, Cancha cancha) {
        CanchaData existente = canchaCrud.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada con ID: " + id));

        existente.setNombre(cancha.getNombre());
        existente.setDeporte(cancha.getDeporte());
        existente.setTipoSuelo(cancha.getTipoSuelo());
        existente.setPrecioTurno(cancha.getPrecioTurno());
        existente.setActiva(cancha.getActiva() != null ? cancha.getActiva() : true);

        // Reemplazar horarios (orphanRemoval elimina los viejos)
        existente.getHorarios().clear();
        if (cancha.getHorariosDisponibilidad() != null) {
            for (HorarioDisponibilidad h : cancha.getHorariosDisponibilidad()) {
                HorarioDisponibilidadData hData = canchaMapper.toDataHorario(h);
                existente.agregarHorario(hData);
            }
        }

        CanchaData guardada = canchaCrud.save(existente);
        return canchaMapper.toDomain(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cancha> buscarPorId(Long id) {
        return canchaCrud.findById(id)
                .map(canchaMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cancha> buscarPorComplejoId(Long complejoId) {
        return canchaCrud.findByComplejoId(complejoId).stream()
                .map(canchaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cancha> buscarPorComplejoIdYActiva(Long complejoId, boolean activa) {
        return canchaCrud.findByComplejoIdAndActiva(complejoId, activa).stream()
                .map(canchaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombreYComplejoId(String nombre, Long complejoId) {
        return canchaCrud.existsByNombreIgnoreCaseAndComplejoId(nombre, complejoId);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        canchaCrud.deleteById(id);
    }
}
