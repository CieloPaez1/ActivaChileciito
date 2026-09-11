package com.activachilecito.usuarios.adapter.output;

import model.Usuario;
import org.springframework.stereotype.Component;
import output.UsuarioRepositoryPort;
import java.util.Optional;

@Component
public class DummyUsuarioRepositoryPortAdapter implements UsuarioRepositoryPort {
    @Override public Optional<Usuario> buscarPorEmail(String email) { return Optional.empty(); }
    @Override public Optional<Usuario> buscarPorId(Long id) { return Optional.empty(); }
    @Override public void actualizar(Usuario usuario) {}
}
