package com.activachilecito.complejo.crud;

import com.activachilecito.complejo.entity.data.CanchaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanchaCrud extends JpaRepository<CanchaData, Long> {
    List<CanchaData> findByComplejoId(Long complejoId);
    List<CanchaData> findByComplejoIdAndActiva(Long complejoId, Boolean activa);
    boolean existsByNombreIgnoreCaseAndComplejoId(String nombre, Long complejoId);
}
