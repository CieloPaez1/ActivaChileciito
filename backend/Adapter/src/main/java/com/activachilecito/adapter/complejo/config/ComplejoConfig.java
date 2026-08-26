package com.activachilecito.adapter.complejo.config;

import com.activachilecito.core.complejo.input.CrearComplejoInput;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;
import com.activachilecito.core.complejo.usecase.CrearComplejoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComplejoConfig {

    @Bean
    public CrearComplejoInput crearComplejoInput(ComplejoRepositoryPort repositoryPort) {
        return new CrearComplejoUseCase(repositoryPort);
    }
}
