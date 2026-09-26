package model;

import exception.ExcepcionUsuario;

public class Usuario {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private RolUsuario rol; // Usamos el Enum que ya tenÃas pensado
    private String telefono;
    private boolean activo;

    public Usuario() {
    }
    public static Usuario restaurar(Long id, String nombre, String apellido, String email, String password, RolUsuario rol, String telefono, boolean activo) {
        // AquÃ­ no validamos nada porque asumimos que los datos en la BD ya son vÃ¡lidos
        return new Usuario(id, nombre, apellido, email, password, rol, telefono, activo);
    }

    Usuario(Long id, String nombre, String apellido, String email, String password, RolUsuario rol, String telefono, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.telefono = telefono;
        this.activo = activo;
    }
    public static Usuario crear(String nombre, String apellido, String email, String password, RolUsuario rol, String telefono) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ExcepcionUsuario("El nombre del usuario no puede estar nulo o vacÃ­o.");
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ExcepcionUsuario("El apellido no puede estar nulo o vacÃ­o.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new ExcepcionUsuario("El email no puede estar nulo o vacÃ­o.");
        }

        if (!email.contains("@") || !email.contains(".")) {
            // Una regla de negocio simple para validar el formato
            throw new ExcepcionUsuario("El formato del correo electrÃ³nico es invÃ¡lido.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new ExcepcionUsuario("La contraseÃna es obligatoria.");
        }

        if (rol == null) {
            throw new ExcepcionUsuario("Debe asignar un rol al usuario.");
        }

        // Si pasa todas las barreras, instanciamos el objeto.
        // El ID nace en null (lo da PostgreSQL) y el estado activo por defecto es true.
        return new Usuario(null, nombre, apellido, email, password, rol, telefono, true);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


}
