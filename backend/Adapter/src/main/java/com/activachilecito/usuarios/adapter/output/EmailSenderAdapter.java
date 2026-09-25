package com.activachilecito.usuarios.adapter.output;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import output.EmailSenderPort;

@Component
@RequiredArgsConstructor
public class EmailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailSenderAdapter.class);

    @Value("${spring.mail.password:}")
    private String mailPassword;

    @Override
    public void enviarEmailRecuperacion(String email, String token) {
        if (mailPassword == null || mailPassword.isEmpty() || mailPassword.equals("TU_CONTRASENA_DE_GMAIL_AQUI")) {
            logger.warn("==========================================================");
            logger.warn("Simulando envio de correo de recuperacion a: {}", email);
            logger.warn("TOKEN GENERADO: {}", token);
            logger.warn("Para enviar correos reales, configura spring.mail.password en application.properties");
            logger.warn("==========================================================");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Recuperacion de Contrasena - ActivaChilecito");
            message.setText("Hola,\n\nPara restablecer tu contrasena utiliza el siguiente token:\n\n" + token + "\n\nEste token expira en 2 horas.");
            mailSender.send(message);
            logger.info("Correo de recuperacion enviado exitosamente a: {}", email);
        } catch (Exception e) {
            logger.error("Error al enviar el correo a: {}", email, e);
        }
    }
}
