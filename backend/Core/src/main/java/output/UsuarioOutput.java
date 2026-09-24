package output;

import model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioOutput {
    // Guarda un usuario nuevo o actualiza uno existente
    Usuario guardar(Usuario usuario);

    // Busca un usuario por su ID (devuelve Optional por si no existe)
    Optional<Usuario> buscarPorId(Long id);

    // Clave para el Login: buscar si el email ya estÃ¡ registrado
    Optional<Usuario> buscarPorEmail(String email);

    // Trae la lista de todos los usuarios (Ãºtil para el DUENO_DE_COMPLEJO)
    List<Usuario> listarTodos();

    // Elimina un usuario por su ID
    void eliminar(Long id);

    // Obtiene usuarios por su rol
    List<Usuario> obtenerPorRol(model.RolUsuario rol);
}
