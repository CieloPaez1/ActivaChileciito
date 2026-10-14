package com.activachilecito.complejo.repository;

import com.activachilecito.complejo.crud.ComplejoCrud;
import com.activachilecito.complejo.entity.data.ComplejoData;
import com.activachilecito.complejo.mapper.ComplejoMapper;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComplejoRepositoryAdapter implements ComplejoRepositoryPort {

    private final ComplejoCrud jpaRepository;
    private final ComplejoMapper mapper;

    public ComplejoRepositoryAdapter(ComplejoCrud jpaRepository, ComplejoMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void guardar(Complejo complejo) {
        ComplejoData entity = mapper.toData(complejo);
        jpaRepository.save(entity);
    }

    @Override
    public void guardar(Long idDueno, Complejo complejo) {
        ComplejoData entity = mapper.toData(complejo);
        entity.setUsuarioId(idDueno);
        jpaRepository.save(entity);
    }

    @Override
    public boolean existePorDueno(Long idDueno) {
        return jpaRepository.existsByUsuarioId(idDueno);
    }

    @Override
    public Optional<Complejo> buscarPorUsuarioId(Long idDueno) {
        return jpaRepository.findByUsuarioId(idDueno)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Complejo> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Complejo> obtenerTodos() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
