package com.activachilecito.core.usecase;

import com.activachilecito.core.dto.LoginRequest;
import com.activachilecito.core.dto.LoginResponse;
import com.activachilecito.core.dto.RegisterRequest;
import com.activachilecito.core.dto.UsuarioResponse;
import com.activachilecito.core.exception.CredencialesInvalidasException;
import com.activachilecito.core.exception.EmailYaRegistradoException;
import com.activachilecito.core.exception.UsuarioNoEncontradoException;
import com.activachilecito.core.exception.UsuarioSuspendidoException;
import com.activachilecito.core.model.EstadoUsuario;
import com.activachilecito.core.model.Rol;
import com.activachilecito.core.model.Usuario;
import com.activachilecito.core.repository.IJwtService;
import com.activachilecito.core.repository.IPasswordEncoder;
import com.activachilecito.core.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
public class AuthUseCase {
    private final IUsuarioRepository usuarioRepository;
    private final IPasswordEncoder passwordEncoder;
    private final IJwtService jwtService;

    public LoginResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailYaRegistradoException("El email ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(Rol.DEPORTISTA)) // Rol por defecto
                .estado(EstadoUsuario.ACTIVO)
                .fechaRegistro(LocalDateTime.now())
                .build();

        usuario = usuarioRepository.save(usuario);
        String token = jwtService.generateToken(usuario);

        return LoginResponse.builder()
                .token(token)
                .usuario(mapToResponse(usuario))
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CredencialesInvalidasException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("Credenciales inválidas");
        }

        if (usuario.getEstado() == EstadoUsuario.SUSPENDIDO || usuario.getEstado() == EstadoUsuario.INACTIVO) {
            throw new UsuarioSuspendidoException("El usuario está suspendido o inactivo");
        }

        String token = jwtService.generateToken(usuario);

        return LoginResponse.builder()
                .token(token)
                .usuario(mapToResponse(usuario))
                .build();
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
