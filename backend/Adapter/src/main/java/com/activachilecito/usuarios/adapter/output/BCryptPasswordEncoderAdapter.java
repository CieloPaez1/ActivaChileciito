package com.activachilecito.usuarios.adapter.output;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import output.PasswordEncoderPort;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {
    
    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override 
    public String encriptar(String password) { 
        return passwordEncoder.encode(password); 
    }
    
    @Override 
    public boolean coincide(String passwordPura, String passwordEncriptada) { 
        return passwordEncoder.matches(passwordPura, passwordEncriptada); 
    }
}
