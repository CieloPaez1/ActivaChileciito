package usecase;

import exception.ExcepcionCredencialesInvalidas;
import exception.ExcepcionUsuario;
import exception.ExcepcionUsuarioNoEncontrado;
import input.CambiarContrasenaRequest;
import lombok.RequiredArgsConstructor;
import model.Usuario;
import output.PasswordEncoderPort;
import output.UsuarioRepositoryPort;

@RequiredArgsConstructor
public class CambiarContrasenaUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public void cambiarContrasena(Long id, CambiarContrasenaRequest request) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado con ID: " + id));

        if (!passwordEncoderPort.coincide(request.getPasswordActual(), usuario.getPassword())) {
            throw new ExcepcionCredencialesInvalidas("La contraseña actual es incorrecta.");
        }

        if (request.getNuevaPassword() == null || request.getNuevaPassword().length() < 8) {
            throw new ExcepcionUsuario("La nueva contraseña debe tener al menos 8 caracteres.");
        }

        String passwordEncriptada = passwordEncoderPort.encriptar(request.getNuevaPassword());
        usuario.setPassword(passwordEncriptada);

        usuarioRepositoryPort.actualizar(usuario);
    }
}
