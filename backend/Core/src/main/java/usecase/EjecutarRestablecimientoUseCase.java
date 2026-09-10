package usecase;

import exception.ExcepcionTokenInvalido;
import exception.ExcepcionUsuarioNoEncontrado;
import input.EjecutarRestablecimientoRequest;
import lombok.RequiredArgsConstructor;
import model.Usuario;
import output.PasswordEncoderPort;
import output.TokenRecuperacionPort;
import output.UsuarioRepositoryPort;

@RequiredArgsConstructor
public class EjecutarRestablecimientoUseCase {

    private final TokenRecuperacionPort tokenRecuperacionPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public void ejecutar(EjecutarRestablecimientoRequest request) {
        if (!tokenRecuperacionPort.esTokenValido(request.getToken())) {
            throw new ExcepcionTokenInvalido("El token es inválido o ha expirado.");
        }

        if (request.getNuevaClave() == null || request.getNuevaClave().length() < 8) {
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 8 caracteres.");
        }

        Long usuarioId = tokenRecuperacionPort.obtenerUsuarioIdPorToken(request.getToken());
        Usuario usuario = usuarioRepositoryPort.buscarPorId(usuarioId)
                .orElseThrow(() -> new ExcepcionUsuarioNoEncontrado("Usuario no encontrado con el ID asociado al token."));

        String claveEncriptada = passwordEncoderPort.encriptar(request.getNuevaClave());
        usuario.setPassword(claveEncriptada);
        
        usuarioRepositoryPort.actualizar(usuario);
        tokenRecuperacionPort.invalidarToken(request.getToken());
    }
}
