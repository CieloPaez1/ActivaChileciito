package com.activachilecito.repository.crud;

import com.activachilecito.entity.UsuarioData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioCrudRepository extends JpaRepository<UsuarioData, Long> {
    Optional<UsuarioData> findByEmail(String email);
    boolean existsByEmail(String email);
}
