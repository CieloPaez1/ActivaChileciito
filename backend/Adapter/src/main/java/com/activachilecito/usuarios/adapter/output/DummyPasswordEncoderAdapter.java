package com.activachilecito.usuarios.adapter.output;

import org.springframework.stereotype.Component;
import output.PasswordEncoderPort;

@Component
public class DummyPasswordEncoderAdapter implements PasswordEncoderPort {
    @Override public String encriptar(String password) { return password; }
    @Override public boolean coincide(String passwordPura, String passwordEncriptada) { return passwordPura.equals(passwordEncriptada); }
}
