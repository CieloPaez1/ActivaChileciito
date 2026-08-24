package com.activachilecito.usuarios.entity.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.RolUsuario;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("apellido")
    private String apellido;

    @JsonProperty("email")
    private String email;

    // AQUÍ ESTÁ LA MAGIA: Solo se lee desde Angular, nunca se devuelve.
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty("rol")
    private RolUsuario rol;

    @JsonProperty("telefono")
    private String telefono;

    @JsonProperty("activo")
    private boolean activo;

    // Constructor vacío
    public UsuarioDto() {}

    // Constructor COMPLETO (para que el Mapper o el Controlador puedan usarlo sin problemas)
    public UsuarioDto(Long id, String nombre, String apellido, String email, String password, RolUsuario rol, String telefono, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.telefono = telefono;
        this.activo = activo;
    }

    // Getters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public RolUsuario getRol() { return rol; }
    public String getTelefono() { return telefono; }
    public boolean isActivo() { return activo; }
}