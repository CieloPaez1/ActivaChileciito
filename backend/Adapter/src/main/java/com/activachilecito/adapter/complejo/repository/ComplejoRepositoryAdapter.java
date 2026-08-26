package com.activachilecito.adapter.complejo.repository;

import com.activachilecito.adapter.complejo.crud.ComplejoCrud;
import com.activachilecito.adapter.complejo.entity.data.ComplejoData;
import com.activachilecito.adapter.complejo.mapper.ComplejoMapper;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import org.springframework.stereotype.Service;

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
}
