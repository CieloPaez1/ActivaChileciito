package com.activachilecito.usuarios.persistence;

import com.activachilecito.usuarios.entity.data.TokenRecuperacionData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacionData, Long> {
    Optional<TokenRecuperacionData> findByToken(String token);
    void deleteByToken(String token);
}
