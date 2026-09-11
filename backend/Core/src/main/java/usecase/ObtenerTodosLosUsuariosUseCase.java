package usecase;

import model.Usuario;
import output.UsuarioOutput;
import java.util.List;

public class ObtenerTodosLosUsuariosUseCase {
    private final UsuarioOutput usuarioOutput;

    public ObtenerTodosLosUsuariosUseCase(UsuarioOutput usuarioOutput) {
        this.usuarioOutput = usuarioOutput;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioOutput.listarTodos();
    }
}
