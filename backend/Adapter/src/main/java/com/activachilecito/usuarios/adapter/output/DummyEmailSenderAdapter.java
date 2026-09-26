package com.activachilecito.usuarios.adapter.output;

import org.springframework.stereotype.Component;
import output.EmailSenderPort;

@Component
public class DummyEmailSenderAdapter implements EmailSenderPort {
    @Override
    public void enviarEmailRecuperacion(String email, String token) {
        System.out.println("======================================================================");
        System.out.println("📬 [SIMULACION EMAIL] Solicitud de restablecimiento de contraseña");
        System.out.println("Destinatario: " + email);
        System.out.println("Enlace de restablecimiento: http://localhost:4200/reset-password?token=" + token);
        System.out.println("======================================================================");
    }
}
