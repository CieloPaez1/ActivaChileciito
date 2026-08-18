package com.activachilecito.usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import output.UsuarioOutput;
import usecase.RegistrarUsuarioUseCase;

@Configuration
public class UsuarioUseCaseConfig {
    // @Bean le dice a Spring que guarde el resultado de este método en su contexto
    // y lo inyecte automáticamente cuando un controlador lo pida (como el UsuarioPostController)
    @Bean
    public RegistrarUsuarioUseCase registrarUsuarioUseCase(UsuarioOutput usuarioOutput) {
        // Aquí instanciamos manualmente el caso de uso puro, pasándole el puerto que se conecta a la BD
        return new RegistrarUsuarioUseCase(usuarioOutput);
    }
}
