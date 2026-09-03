package com.activachilecito.core.repository;

import com.activachilecito.core.model.Usuario;

public interface IJwtService {
    String generateToken(Usuario usuario);
}
