package com.activachilecito.usuarios.adapter.output;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import output.TokenRecuperacionPort;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Primary
public class DummyTokenRecuperacionAdapter implements TokenRecuperacionPort {

    private static class TokenData {
        final Long usuarioId;
        final long expirationTime;

        TokenData(Long usuarioId, long expirationTime) {
            this.usuarioId = usuarioId;
            this.expirationTime = expirationTime;
        }
    }

    private final Map<String, TokenData> tokens = new ConcurrentHashMap<>();

    @Override
    public String generarToken(Long usuarioId) {
        String token = UUID.randomUUID().toString();
        // Expira en 30 minutos
        long expiration = System.currentTimeMillis() + (30 * 60 * 1000);
        tokens.put(token, new TokenData(usuarioId, expiration));
        return token;
    }

    @Override
    public boolean esTokenValido(String token) {
        if (token == null || !tokens.containsKey(token)) {
            return false;
        }
        TokenData data = tokens.get(token);
        if (System.currentTimeMillis() > data.expirationTime) {
            tokens.remove(token);
            return false;
        }
        return true;
    }

    @Override
    public Long obtenerUsuarioIdPorToken(String token) {
        TokenData data = tokens.get(token);
        return data != null ? data.usuarioId : null;
    }

    @Override
    public void invalidarToken(String token) {
        if (token != null) {
            tokens.remove(token);
        }
    }
}
