package usecase;

import exception.ExcepcionUsuario;
import input.RegistrarUsuarioInput;
import model.Usuario;
import output.UsuarioOutput;

public class RegistrarUsuarioUseCase implements RegistrarUsuarioInput {


    private final UsuarioOutput usuarioOutput;

    public RegistrarUsuarioUseCase(UsuarioOutput usuarioOutput) {

        this.usuarioOutput = usuarioOutput;
    }

    @Override
    public Usuario registrarUsuario(Usuario nuevoUsuario) {


        // Regla de Negocio 1: Verificar si el email ya está registrado
        // Aquí vemos el poder del Optional. Preguntamos si la caja "tiene algo".


        if (usuarioOutput.buscarPorEmail(nuevoUsuario.getEmail()).isPresent()) {
            throw new ExcepcionUsuario("El email " + nuevoUsuario.getEmail() + " ya se encuentra registrado.");
        }

        // Regla de Negocio 3: Guardar el usuario validado
        return usuarioOutput.guardar(nuevoUsuario);
    }

}
