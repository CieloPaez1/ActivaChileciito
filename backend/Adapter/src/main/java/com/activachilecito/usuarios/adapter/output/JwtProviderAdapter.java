package com.activachilecito.usuarios.adapter.output;

import org.springframework.stereotype.Component;
import output.JwtProviderPort;
import java.util.UUID;

@Component
public class JwtProviderAdapter implements JwtProviderPort {

    @Override
    public String generarToken(String email) {
        return "jwt-token-" + email + "-" + UUID.randomUUID().toString();
    }
}
