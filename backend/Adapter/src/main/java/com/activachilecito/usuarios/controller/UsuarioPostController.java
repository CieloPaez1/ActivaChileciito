package com.activachilecito.usuarios.controller;

import com.activachilecito.usuarios.entity.DTO.UsuarioDto;
import input.RegistrarUsuarioInput;
import model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/usuarios")
public class UsuarioPostController {
    private final RegistrarUsuarioInput registrarUsuarioInput;

    public UsuarioPostController(RegistrarUsuarioInput registrarUsuarioInput) {
        this.registrarUsuarioInput = registrarUsuarioInput;
    }


    @PostMapping
    public ResponseEntity<String> registrar(@RequestBody UsuarioDto dto) {

        // 1. Usamos el DTO para instanciar el Modelo mediante el Factory Method.
        // Si hay un dato vacío o nulo, explotará aquí mismo lanzando la excepción.
        Usuario usuarioPuro = Usuario.crear(
                dto.getNombre(),
                dto.getApellido(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getRol()
        );

        // 2. Le pasamos el modelo validado al Caso de Uso (idéntico a tu proyecto anterior)
        registrarUsuarioInput.registrarUsuario(usuarioPuro);

        // 3. Devolvemos la respuesta de éxito
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado correctamente");
    }
}
