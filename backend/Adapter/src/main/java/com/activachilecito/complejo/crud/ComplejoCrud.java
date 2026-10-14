package com.activachilecito.complejo.crud;

import com.activachilecito.complejo.entity.data.ComplejoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComplejoCrud extends JpaRepository<ComplejoData, Long> {
    boolean existsByUsuarioId(Long usuarioId);
    Optional<ComplejoData> findByUsuarioId(Long usuarioId);
}
