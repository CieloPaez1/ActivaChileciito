package com.activachilecito.repository.impl;

import com.activachilecito.core.model.Usuario;
import com.activachilecito.core.repository.IUsuarioRepository;
import com.activachilecito.entity.UsuarioData;
import com.activachilecito.mapper.UsuarioMapper;
import com.activachilecito.repository.crud.UsuarioCrudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements IUsuarioRepository {

    private final UsuarioCrudRepository crudRepository;
    private final UsuarioMapper mapper;

    @Override
    public Optional<Usuario> findById(Long id) {
        return crudRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return crudRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return crudRepository.existsByEmail(email);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioData data = mapper.toData(usuario);
        UsuarioData saved = crudRepository.save(data);
        return mapper.toDomain(saved);
    }

    @Override
    public List<Usuario> findAll() {
        return crudRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
