package com.activachilecito.repository.impl;

import com.activachilecito.core.model.PerfilDeportivo;
import com.activachilecito.core.repository.IPerfilDeportivoRepository;
import com.activachilecito.entity.PerfilData;
import com.activachilecito.mapper.PerfilMapper;
import com.activachilecito.repository.crud.PerfilCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PerfilDeportivoRepositoryImpl implements IPerfilDeportivoRepository {

    private final PerfilCrudRepository crudRepository;
    private final PerfilMapper mapper;

    @Override
    public Optional<PerfilDeportivo> findByUsuarioId(Long usuarioId) {
        return crudRepository.findByUsuarioId(usuarioId).map(mapper::toDomain);
    }

    @Override
    public PerfilDeportivo save(PerfilDeportivo perfil) {
        PerfilData data = mapper.toData(perfil);
        PerfilData saved = crudRepository.save(data);
        return mapper.toDomain(saved);
    }
}
