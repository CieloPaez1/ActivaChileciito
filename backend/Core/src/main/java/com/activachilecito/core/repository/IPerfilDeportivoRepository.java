package com.activachilecito.core.repository;

import com.activachilecito.core.model.PerfilDeportivo;
import java.util.Optional;

public interface IPerfilDeportivoRepository {
    Optional<PerfilDeportivo> findByUsuarioId(Long usuarioId);
    PerfilDeportivo save(PerfilDeportivo perfil);
}
