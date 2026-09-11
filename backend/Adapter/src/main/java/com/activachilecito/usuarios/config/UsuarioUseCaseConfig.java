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

    @Bean
    public usecase.EliminarUsuarioUseCase eliminarUsuarioUseCase(UsuarioOutput usuarioOutput) {
        return new usecase.EliminarUsuarioUseCase(usuarioOutput);
    }

    @Bean
    public usecase.ObtenerUsuariosPorRolUseCase obtenerUsuariosPorRolUseCase(UsuarioOutput usuarioOutput) {
        return new usecase.ObtenerUsuariosPorRolUseCase(usuarioOutput);
    }

    @Bean
    public usecase.IniciarSesionUseCase iniciarSesionUseCase(
            output.ValidarCredencialesPort validarCredencialesPort,
            output.JwtProviderPort jwtProviderPort) {
        return new usecase.IniciarSesionUseCase(validarCredencialesPort, jwtProviderPort);
    }

    @Bean
    public usecase.VisualizarCredencialesUseCase visualizarCredencialesUseCase(
            output.ObtenerUsuarioPorIdPort obtenerUsuarioPorIdPort) {
        return new usecase.VisualizarCredencialesUseCase(obtenerUsuarioPorIdPort);
    }

    @Bean
    public usecase.SolicitarRestablecimientoUseCase solicitarRestablecimientoUseCase(
            output.UsuarioOutput usuarioOutput,
            output.TokenRecuperacionPort tokenRecuperacionPort,
            output.EmailSenderPort emailSenderPort) {
        return new usecase.SolicitarRestablecimientoUseCase(usuarioOutput, tokenRecuperacionPort, emailSenderPort);
    }

    @Bean
    public usecase.EjecutarRestablecimientoUseCase ejecutarRestablecimientoUseCase(
            output.TokenRecuperacionPort tokenRecuperacionPort,
            output.UsuarioOutput usuarioOutput,
            output.PasswordEncoderPort passwordEncoderPort) {
        return new usecase.EjecutarRestablecimientoUseCase(tokenRecuperacionPort, usuarioOutput, passwordEncoderPort);
    }

    @Bean
    public usecase.VisualizarPerfilUseCase visualizarPerfilUseCase(UsuarioOutput usuarioOutput) {
        return new usecase.VisualizarPerfilUseCase(usuarioOutput);
    }

    @Bean
    public usecase.CerrarSesionUseCase cerrarSesionUseCase(
            output.TokenBlacklistPort tokenBlacklistPort) {
        return new usecase.CerrarSesionUseCase(tokenBlacklistPort);
    }

    @Bean
    public usecase.ModificarPerfilUseCase modificarPerfilUseCase(UsuarioOutput usuarioOutput) {
        return new usecase.ModificarPerfilUseCase(usuarioOutput);
    }

    @Bean
    public usecase.CambiarContrasenaUseCase cambiarContrasenaUseCase(
            output.UsuarioOutput usuarioOutput,
            output.PasswordEncoderPort passwordEncoderPort) {
        return new usecase.CambiarContrasenaUseCase(usuarioOutput, passwordEncoderPort);
    }

    @Bean
    public usecase.ObtenerTodosLosUsuariosUseCase obtenerTodosLosUsuariosUseCase(UsuarioOutput usuarioOutput) {
        return new usecase.ObtenerTodosLosUsuariosUseCase(usuarioOutput);
    }

    @Bean
    public usecase.ModificarUsuarioAdminUseCase modificarUsuarioAdminUseCase(UsuarioOutput usuarioOutput) {
        return new usecase.ModificarUsuarioAdminUseCase(usuarioOutput);
    }
}
