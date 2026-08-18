package com.activachilecito.usuarios.persistence;

import com.activachilecito.usuarios.crud.UsuarioCrud;
import com.activachilecito.usuarios.entity.data.UsuarioData;
import com.activachilecito.usuarios.mapper.UsuarioMapper;
import jakarta.websocket.server.ServerEndpoint;
import model.Usuario;
import org.springframework.stereotype.Service;
import output.UsuarioOutput;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioRepository implements UsuarioOutput {

    private final UsuarioCrud usuarioCrud;
    private final UsuarioMapper usuarioMapper;

    // Inyección de dependencias
    public UsuarioRepository(UsuarioCrud usuarioCrud, UsuarioMapper usuarioMapper) {
        this.usuarioCrud = usuarioCrud;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        // 1. Traducimos del modelo puro de negocio a la entidad de Base de Datos
        UsuarioData data = usuarioMapper.toData(usuario);

        // 2. Usamos Spring Data para guardar en PostgreSQL
        UsuarioData savedData = usuarioCrud.save(data);

        // 3. Volvemos a traducir al modelo puro y lo retornamos
        return usuarioMapper.toDomain(savedData);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        // Usamos .map() del Optional para traducir si la caja no está vacía
        return usuarioCrud.findById(id)
                .map(usuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioCrud.findByEmail(email)
                .map(usuarioMapper::toDomain);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioCrud.findAll().stream()
                .map(usuarioMapper::toDomain)
                .collect(Collectors.toList());
    }
}

