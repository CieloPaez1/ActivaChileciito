package com.activachilecito.usuarios.adapter.output;

import org.springframework.stereotype.Component;
import output.TokenRecuperacionPort;

@Component
public class DummyTokenRecuperacionAdapter implements TokenRecuperacionPort {
    @Override public String generarToken(Long usuarioId) { return null; }
    @Override public boolean esTokenValido(String token) { return false; }
    @Override public Long obtenerUsuarioIdPorToken(String token) { return null; }
    @Override public void invalidarToken(String token) {}
}
