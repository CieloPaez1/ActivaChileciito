package usecase;

import exception.ExcepcionUsuario;
import input.RegistrarUsuarioInput;
import model.Usuario;
import output.UsuarioOutput;
import output.PasswordEncoderPort;

public class RegistrarUsuarioUseCase implements RegistrarUsuarioInput {

    private final UsuarioOutput usuarioOutput;
    private final PasswordEncoderPort passwordEncoderPort;

    public RegistrarUsuarioUseCase(UsuarioOutput usuarioOutput, PasswordEncoderPort passwordEncoderPort) {
        this.usuarioOutput = usuarioOutput;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public Usuario registrarUsuario(Usuario nuevoUsuario) {

        // Regla de Negocio 1: Verificar si el email ya estÃ¡ registrado
        if (usuarioOutput.buscarPorEmail(nuevoUsuario.getEmail()).isPresent()) {
            throw new ExcepcionUsuario("El email " + nuevoUsuario.getEmail() + " ya se encuentra registrado.");
        }

        // Encriptar contraseÃna
        nuevoUsuario.setPassword(passwordEncoderPort.encriptar(nuevoUsuario.getPassword()));

        // Regla de Negocio 3: Guardar el usuario validado
        return usuarioOutput.guardar(nuevoUsuario);
    }
}
