package com.activachilecito.core.usecase;

import com.activachilecito.core.dto.ActualizarPerfilRequest;
import com.activachilecito.core.dto.CambiarPasswordRequest;
import com.activachilecito.core.dto.UsuarioResponse;
import com.activachilecito.core.exception.CredencialesInvalidasException;
import com.activachilecito.core.exception.UsuarioNoEncontradoException;
import com.activachilecito.core.model.PerfilDeportivo;
import com.activachilecito.core.model.Usuario;
import com.activachilecito.core.repository.IPasswordEncoder;
import com.activachilecito.core.repository.IPerfilDeportivoRepository;
import com.activachilecito.core.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsuarioUseCase {
    private final IUsuarioRepository usuarioRepository;
    private final IPerfilDeportivoRepository perfilRepository;
    private final IPasswordEncoder passwordEncoder;

    public UsuarioResponse obtenerMiPerfil(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
                
        PerfilDeportivo perfil = perfilRepository.findByUsuarioId(usuario.getId()).orElse(null);
        return mapToResponse(usuario, perfil);
    }

    public UsuarioResponse actualizarMiPerfil(String email, ActualizarPerfilRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if (request.getNombre() != null) usuario.setNombre(request.getNombre());
        if (request.getApellido() != null) usuario.setApellido(request.getApellido());
        if (request.getTelefono() != null) usuario.setTelefono(request.getTelefono());

        usuarioRepository.save(usuario);

        PerfilDeportivo perfil = perfilRepository.findByUsuarioId(usuario.getId())
                .orElse(PerfilDeportivo.builder().usuarioId(usuario.getId()).build());

        if (request.getDeportePreferido() != null) perfil.setDeportePreferido(request.getDeportePreferido());
        if (request.getNivel() != null) perfil.setNivel(request.getNivel());
        if (request.getLesiones() != null) perfil.setLesiones(request.getLesiones());

        perfilRepository.save(perfil);

        return mapToResponse(usuario, perfil);
    }

    public void cambiarMiPassword(String email, CambiarPasswordRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("La contraseña actual es incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(request.getPasswordNueva()));
        usuarioRepository.save(usuario);
    }

    private UsuarioResponse mapToResponse(Usuario usuario, PerfilDeportivo perfil) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .roles(usuario.getRoles())
                .estado(usuario.getEstado())
                .deportePreferido(perfil != null ? perfil.getDeportePreferido() : null)
                .nivel(perfil != null ? perfil.getNivel() : null)
                .lesiones(perfil != null ? perfil.getLesiones() : null)
                .build();
    }
}
