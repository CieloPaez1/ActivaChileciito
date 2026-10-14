package com.activachilecito.complejo.config;

import com.activachilecito.core.complejo.output.CanchaRepositoryPort;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import output.UsuarioOutput;
import usecase.ObtenerCanchasPorComplejoUseCase;
import usecase.RegistrarCanchaUseCase;
import usecase.RegistrarComplejoUseCase;

@Configuration
public class ComplejoConfig {

    @Bean
    public RegistrarComplejoUseCase registrarComplejoUseCase(
            ComplejoRepositoryPort complejoRepositoryPort,
            UsuarioOutput usuarioOutput) {
        return new RegistrarComplejoUseCase(complejoRepositoryPort, usuarioOutput);
    }

    @Bean
    public RegistrarCanchaUseCase registrarCanchaUseCase(
            ComplejoRepositoryPort complejoRepositoryPort,
            CanchaRepositoryPort canchaRepositoryPort,
            UsuarioOutput usuarioOutput) {
        return new RegistrarCanchaUseCase(complejoRepositoryPort, canchaRepositoryPort, usuarioOutput);
    }

    @Bean
    public ObtenerCanchasPorComplejoUseCase obtenerCanchasPorComplejoUseCase(
            ComplejoRepositoryPort complejoRepositoryPort,
            CanchaRepositoryPort canchaRepositoryPort) {
        return new ObtenerCanchasPorComplejoUseCase(complejoRepositoryPort, canchaRepositoryPort);
    }
}
