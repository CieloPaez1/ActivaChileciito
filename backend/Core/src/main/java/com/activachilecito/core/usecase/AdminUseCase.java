package com.activachilecito.core.usecase;

import com.activachilecito.core.dto.ActualizarRolesRequest;
import com.activachilecito.core.dto.UsuarioResponse;
import com.activachilecito.core.exception.UsuarioNoEncontradoException;
import com.activachilecito.core.model.EstadoUsuario;
import com.activachilecito.core.model.Usuario;
import com.activachilecito.core.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AdminUseCase {
    private final IUsuarioRepository usuarioRepository;

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse cambiarEstadoUsuario(Long id, EstadoUsuario nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        
        usuario.setEstado(nuevoEstado);
        return mapToResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponse actualizarRoles(Long id, ActualizarRolesRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            usuario.setRoles(request.getRoles());
        }
        
        return mapToResponse(usuarioRepository.save(usuario));
    }

    private UsuarioResponse mapToResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .roles(usuario.getRoles())
                .estado(usuario.getEstado())
                .build();
    }
}
