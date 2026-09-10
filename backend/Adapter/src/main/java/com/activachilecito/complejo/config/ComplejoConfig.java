package com.activachilecito.complejo.config;

import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import usecase.RegistrarComplejoUseCase;
import output.UsuarioRepositoryPort;

@Configuration
public class ComplejoConfig {

    @Bean
    public RegistrarComplejoUseCase registrarComplejoUseCase(
            ComplejoRepositoryPort complejoRepositoryPort,
            UsuarioRepositoryPort usuarioRepositoryPort) {
        return new RegistrarComplejoUseCase(complejoRepositoryPort, usuarioRepositoryPort);
    }
}
