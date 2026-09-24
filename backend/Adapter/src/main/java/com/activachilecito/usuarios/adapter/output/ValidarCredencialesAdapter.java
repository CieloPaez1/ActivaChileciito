package com.activachilecito.usuarios.adapter.output;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import output.ValidarCredencialesPort;

@Component
@RequiredArgsConstructor
public class ValidarCredencialesAdapter implements ValidarCredencialesPort {

    private final AuthenticationManager authenticationManager;

    @Override
    public void validar(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }
}
