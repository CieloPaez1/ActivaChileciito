package com.activachilecito.repository.crud;

import com.activachilecito.entity.PerfilData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilCrudRepository extends JpaRepository<PerfilData, Long> {
    Optional<PerfilData> findByUsuarioId(Long usuarioId);
}
