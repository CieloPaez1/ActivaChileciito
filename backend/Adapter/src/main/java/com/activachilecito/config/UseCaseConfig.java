package com.activachilecito.config;

import com.activachilecito.core.repository.IJwtService;
import com.activachilecito.core.repository.IPasswordEncoder;
import com.activachilecito.core.repository.IPerfilDeportivoRepository;
import com.activachilecito.core.repository.IUsuarioRepository;
import com.activachilecito.core.usecase.AdminUseCase;
import com.activachilecito.core.usecase.AuthUseCase;
import com.activachilecito.core.usecase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public AuthUseCase authUseCase(IUsuarioRepository usuarioRepository, IPasswordEncoder passwordEncoder, IJwtService jwtService) {
        return new AuthUseCase(usuarioRepository, passwordEncoder, jwtService);
    }

    @Bean
    public UsuarioUseCase usuarioUseCase(IUsuarioRepository usuarioRepository, IPerfilDeportivoRepository perfilRepository, IPasswordEncoder passwordEncoder) {
        return new UsuarioUseCase(usuarioRepository, perfilRepository, passwordEncoder);
    }

    @Bean
    public AdminUseCase adminUseCase(IUsuarioRepository usuarioRepository) {
        return new AdminUseCase(usuarioRepository);
    }
}
