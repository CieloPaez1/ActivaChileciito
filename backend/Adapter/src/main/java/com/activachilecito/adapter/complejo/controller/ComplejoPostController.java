package com.activachilecito.adapter.complejo.controller;

import com.activachilecito.adapter.complejo.entity.dto.ComplejoDto;
import com.activachilecito.core.complejo.input.CrearComplejoInput;
import com.activachilecito.core.complejo.model.Complejo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/complejos")
public class ComplejoPostController {

    private final CrearComplejoInput crearComplejoInput;

    public ComplejoPostController(CrearComplejoInput crearComplejoInput) {
        this.crearComplejoInput = crearComplejoInput;
    }

    @PostMapping
    public ResponseEntity<Void> crearComplejo(@RequestBody ComplejoDto dto) {
        Complejo complejo = Complejo.crear(
                dto.getNombre(),
                dto.getDireccion(),
                dto.getTelefono(),
                dto.getPrestaciones()
        );
        
        crearComplejoInput.crearComplejo(complejo);
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
