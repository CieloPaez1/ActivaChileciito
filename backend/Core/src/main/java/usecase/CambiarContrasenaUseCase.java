package usecase;

import exception.ExcepcionCredencialesInvalidas;
import exception.ExcepcionUsuario;
import exception.ExcepcionUsuarioNoEncontrado;
import input.CambiarContrasenaRequest;
import lombok.RequiredArgsConstructor;
import model.Usuario;
import output.PasswordEncoderPort;
import output.UsuarioOutput;

@RequiredArgsConstructor
public class CambiarContrasenaUseCase {

    private final UsuarioOutput usuarioOutput;
    private final PasswordEncoderPort passwordEncoderPort;

    public void cambiarContrasena(Long id, CambiarContrasenaRequest request) {
        Usuario usuario = usuarioOutput.buscarPorId(id)
                .orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado con ID: " + id));

        if (!passwordEncoderPort.coincide(request.getPasswordActual(), usuario.getPassword())) {
            throw new ExcepcionCredencialesInvalidas("La contraseÃna actual es incorrecta.");
        }

        if (request.getNuevaPassword() == null || request.getNuevaPassword().length() < 8) {
            throw new ExcepcionUsuario("La nueva contraseÃna debe tener al menos 8 caracteres.");
        }

        String passwordEncriptada = passwordEncoderPort.encriptar(request.getNuevaPassword());
        usuario.setPassword(passwordEncriptada);

        usuarioOutput.guardar(usuario);
    }
}
