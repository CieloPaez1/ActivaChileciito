package com.activachilecito.usuarios.adapter.output;

import org.springframework.stereotype.Component;
import output.TokenBlacklistPort;

@Component
public class DummyTokenBlacklistAdapter implements TokenBlacklistPort {
    @Override public void invalidarToken(String token) {}
    @Override public boolean esTokenInvalido(String token) { return false; }
}
