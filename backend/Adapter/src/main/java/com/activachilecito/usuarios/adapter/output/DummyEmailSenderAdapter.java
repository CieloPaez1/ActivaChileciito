package com.activachilecito.usuarios.adapter.output;

import org.springframework.stereotype.Component;
import output.EmailSenderPort;

@Component
public class DummyEmailSenderAdapter implements EmailSenderPort {
    @Override public void enviarEmailRecuperacion(String email, String token) {}
}
