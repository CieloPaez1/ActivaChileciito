package com.activachilecito.core.complejo.output;

import com.activachilecito.core.complejo.model.Cancha;

import java.util.List;
import java.util.Optional;

public interface CanchaRepositoryPort {
    Cancha guardar(Cancha cancha);
    Optional<Cancha> buscarPorId(Long id);
    List<Cancha> buscarPorComplejoId(Long complejoId);
    List<Cancha> buscarPorComplejoIdYActiva(Long complejoId, boolean activa);
    boolean existePorNombreYComplejoId(String nombre, Long complejoId);
    void eliminar(Long id);
}
