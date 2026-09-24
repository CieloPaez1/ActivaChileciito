package usecase;

import exception.ExcepcionUsuario;
import input.EliminarUsuarioInput;
import output.UsuarioOutput;

public class EliminarUsuarioUseCase implements EliminarUsuarioInput {

    private final UsuarioOutput usuarioOutput;

    public EliminarUsuarioUseCase(UsuarioOutput usuarioOutput) {
        this.usuarioOutput = usuarioOutput;
    }

    @Override
    public void eliminarUsuario(Long idUsuario) {
        if (usuarioOutput.buscarPorId(idUsuario).isEmpty()) {
            throw new ExcepcionUsuario("No se puede eliminar el usuario. El ID " + idUsuario + " no existe.");
        }
        usuarioOutput.eliminar(idUsuario);
    }
}
