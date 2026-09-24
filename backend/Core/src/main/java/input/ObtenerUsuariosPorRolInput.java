package input;

import model.RolUsuario;
import model.Usuario;
import java.util.List;

public interface ObtenerUsuariosPorRolInput {
    List<Usuario> obtenerUsuariosPorRol(RolUsuario rol);
}
