package com.activachilecito.core.complejo.output;

import com.activachilecito.core.complejo.model.Complejo;

import java.util.List;
import java.util.Optional;

public interface ComplejoRepositoryPort {
    void guardar(Complejo complejo);
    void guardar(Long idDueno, Complejo complejo);
    boolean existePorDueno(Long idDueno);
    Optional<Complejo> buscarPorUsuarioId(Long idDueno);
    Optional<Complejo> buscarPorId(Long id);
    List<Complejo> obtenerTodos();
}
