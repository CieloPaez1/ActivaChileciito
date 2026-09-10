package com.activachilecito.usuarios.adapter.output;

import com.activachilecito.usuarios.persistence.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import model.Usuario;
import org.springframework.stereotype.Component;
import output.ObtenerUsuarioPorIdPort;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements ObtenerUsuarioPorIdPort {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }
}
