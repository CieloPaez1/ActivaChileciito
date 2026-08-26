package com.activachilecito.core.complejo.usecase;

import com.activachilecito.core.complejo.input.CrearComplejoInput;
import com.activachilecito.core.complejo.model.Complejo;
import com.activachilecito.core.complejo.output.ComplejoRepositoryPort;

public class CrearComplejoUseCase implements CrearComplejoInput {
    
    private final ComplejoRepositoryPort repositoryPort;

    public CrearComplejoUseCase(ComplejoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public void crearComplejo(Complejo complejo) {
        repositoryPort.guardar(complejo);
    }
}
