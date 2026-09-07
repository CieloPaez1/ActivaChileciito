package com.activachilecito.usuarios.controller;

import com.activachilecito.usuarios.entity.DTO.UsuarioDto;
import com.activachilecito.usuarios.mapper.UsuarioMapper;
import input.ObtenerUsuariosPorRolInput;
import model.RolUsuario;
import model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioGetController {

    private final ObtenerUsuariosPorRolInput obtenerUsuariosPorRolInput;
    private final UsuarioMapper usuarioMapper;

    public UsuarioGetController(ObtenerUsuariosPorRolInput obtenerUsuariosPorRolInput, UsuarioMapper usuarioMapper) {
        this.obtenerUsuariosPorRolInput = obtenerUsuariosPorRolInput;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioDto>> obtenerPorRol(@PathVariable RolUsuario rol) {
        List<Usuario> usuarios = obtenerUsuariosPorRolInput.obtenerUsuariosPorRol(rol);
        
        List<UsuarioDto> dtos = usuarios.stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(dtos); // Status 200 OK
    }
}
