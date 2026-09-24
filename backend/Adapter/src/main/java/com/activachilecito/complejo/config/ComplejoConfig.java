package com.activachilecito.complejo.config;

import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import usecase.RegistrarComplejoUseCase;
import output.UsuarioOutput;

@Configuration
public class ComplejoConfig {

    @Bean
    public RegistrarComplejoUseCase registrarComplejoUseCase(
            ComplejoRepositoryPort complejoRepositoryPort,
            UsuarioOutput usuarioOutput) {
        return new RegistrarComplejoUseCase(complejoRepositoryPort, usuarioOutput);
    }
}
