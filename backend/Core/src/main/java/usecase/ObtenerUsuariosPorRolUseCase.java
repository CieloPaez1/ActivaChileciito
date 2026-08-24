package usecase;

import input.ObtenerUsuariosPorRolInput;
import model.RolUsuario;
import model.Usuario;
import output.UsuarioOutput;

import java.util.List;

public class ObtenerUsuariosPorRolUseCase implements ObtenerUsuariosPorRolInput {

    private final UsuarioOutput usuarioOutput;

    public ObtenerUsuariosPorRolUseCase(UsuarioOutput usuarioOutput) {
        this.usuarioOutput = usuarioOutput;
    }

    @Override
    public List<Usuario> obtenerUsuariosPorRol(RolUsuario rol) {
        return usuarioOutput.obtenerPorRol(rol);
    }
}
