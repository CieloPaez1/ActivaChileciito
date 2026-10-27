package com.activachilecito.complejo.config;

import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import usecase.ModificarComplejoUseCase;
import usecase.ObtenerMiComplejoUseCase;
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

    @Bean
    public ObtenerMiComplejoUseCase obtenerMiComplejoUseCase(
            ComplejoRepositoryPort complejoRepositoryPort) {
        return new ObtenerMiComplejoUseCase(complejoRepositoryPort);
    }

    @Bean
    public ModificarComplejoUseCase modificarComplejoUseCase(
            ComplejoRepositoryPort complejoRepositoryPort) {
        return new ModificarComplejoUseCase(complejoRepositoryPort);
    }
}
