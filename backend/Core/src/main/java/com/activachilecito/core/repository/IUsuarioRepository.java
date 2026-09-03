package com.activachilecito.core.repository;

import com.activachilecito.core.model.Usuario;
import java.util.Optional;
import java.util.List;

public interface IUsuarioRepository {
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    Usuario save(Usuario usuario);
    List<Usuario> findAll();
}
