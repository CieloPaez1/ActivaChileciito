package output;

import model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioOutput {
    // Guarda un usuario nuevo o actualiza uno existente
    Usuario guardar(Usuario usuario);

    // Busca un usuario por su ID (devuelve Optional por si no existe)
    Optional<Usuario> buscarPorId(Long id);

    // Clave para el Login: buscar si el email ya está registrado
    Optional<Usuario> buscarPorEmail(String email);

    // Trae la lista de todos los usuarios (útil para el ADMIN_SISTEMA)
    List<Usuario> listarTodos();

}
