package com.activachilecito.complejo.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplejoDto {

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("direccion")
    private String direccion;

    @JsonProperty("telefono")
    private String telefono;

    @JsonProperty("prestaciones")
    private List<String> prestaciones;

    public ComplejoDto() {
    }

    public ComplejoDto(String nombre, String direccion, String telefono, List<String> prestaciones) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.prestaciones = prestaciones;
    }

    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public List<String> getPrestaciones() { return prestaciones; }
}
