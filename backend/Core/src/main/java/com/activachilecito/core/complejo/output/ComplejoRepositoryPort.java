package com.activachilecito.core.complejo.output;

import com.activachilecito.core.complejo.model.Complejo;
import java.util.Optional;

public interface ComplejoRepositoryPort {
    void guardar(Complejo complejo);
    void guardar(Long idDueno, Complejo complejo);
    boolean existePorDueno(Long idDueno);
    Optional<Complejo> buscarPorDueno(Long idDueno);
}
