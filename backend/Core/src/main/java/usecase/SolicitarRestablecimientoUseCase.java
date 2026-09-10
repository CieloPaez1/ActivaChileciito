package usecase;

import input.SolicitarRestablecimientoRequest;
import lombok.RequiredArgsConstructor;
import model.Usuario;
import output.EmailSenderPort;
import output.TokenRecuperacionPort;
import output.UsuarioRepositoryPort;

import java.util.Optional;

@RequiredArgsConstructor
public class SolicitarRestablecimientoUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final TokenRecuperacionPort tokenRecuperacionPort;
    private final EmailSenderPort emailSenderPort;

    public void solicitar(SolicitarRestablecimientoRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepositoryPort.buscarPorEmail(request.getEmail());
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String token = tokenRecuperacionPort.generarToken(usuario.getId());
            emailSenderPort.enviarEmailRecuperacion(usuario.getEmail(), token);
        }
        // Si no existe, simulamos éxito silenciosamente.
    }
}
