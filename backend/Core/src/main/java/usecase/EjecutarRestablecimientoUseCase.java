package usecase;

import exception.ExcepcionUsuario;
import exception.ExcepcionUsuarioNoEncontrado;
import input.EjecutarRestablecimientoRequest;
import lombok.RequiredArgsConstructor;
import model.Usuario;
import output.PasswordEncoderPort;
import output.TokenRecuperacionPort;
import output.UsuarioOutput;

@RequiredArgsConstructor
public class EjecutarRestablecimientoUseCase {

    private final TokenRecuperacionPort tokenRecuperacionPort;
    private final UsuarioOutput usuarioOutput;
    private final PasswordEncoderPort passwordEncoderPort;

    public void ejecutar(EjecutarRestablecimientoRequest request) {
        if (!tokenRecuperacionPort.esTokenValido(request.getToken())) {
            throw new ExcepcionUsuario("El token de recuperación es inválido o ha expirado.");
        }

        Long usuarioId = tokenRecuperacionPort.obtenerUsuarioIdPorToken(request.getToken());
        Usuario usuario = usuarioOutput.buscarPorId(usuarioId)
                .orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado"));

        String passwordEncriptada = passwordEncoderPort.encriptar(request.getNuevaClave());
        usuario.setPassword(passwordEncriptada);

        usuarioOutput.guardar(usuario);
        tokenRecuperacionPort.invalidarToken(request.getToken());
    }
}
