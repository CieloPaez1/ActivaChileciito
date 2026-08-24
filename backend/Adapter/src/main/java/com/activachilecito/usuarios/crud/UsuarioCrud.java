package com.activachilecito.usuarios.crud;

import com.activachilecito.usuarios.entity.data.UsuarioData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioCrud extends JpaRepository<UsuarioData, Long> {

    Optional<UsuarioData> findByEmail(String email);
    
    java.util.List<UsuarioData> findByRol(model.RolUsuario rol);
}
