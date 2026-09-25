import os

# 1. pom.xml
pom_path = 'backend/Adapter/pom.xml'
with open(pom_path, 'r', encoding='utf-8') as f:
    pom_content = f.read()

mail_dependency = '''
        <!-- Spring Boot Mail -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
'''
if 'spring-boot-starter-mail' not in pom_content:
    pom_content = pom_content.replace('</dependencies>', mail_dependency + '    </dependencies>')
    with open(pom_path, 'w', encoding='utf-8') as f:
        f.write(pom_content)

# 2. EmailSenderAdapter.java
new_email_path = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/adapter/output/EmailSenderAdapter.java'
email_content = '''package com.activachilecito.usuarios.adapter.output;

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
            message.setText("Hola,\\n\\nPara restablecer tu contrasena utiliza el siguiente token:\\n\\n" + token + "\\n\\nEste token expira en 2 horas.");
            mailSender.send(message);
            logger.info("Correo de recuperacion enviado exitosamente a: {}", email);
        } catch (Exception e) {
            logger.error("Error al enviar el correo a: {}", email, e);
        }
    }
}
'''
with open(new_email_path, 'w', encoding='utf-8') as f:
    f.write(email_content)

old_email_path = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/adapter/output/DummyEmailSenderAdapter.java'
if os.path.exists(old_email_path):
    os.remove(old_email_path)

# 3. TokenRecuperacionData.java
token_data_path = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/entity/data/TokenRecuperacionData.java'
token_data_content = '''package com.activachilecito.usuarios.entity.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens_recuperacion")
@Getter
@Setter
@NoArgsConstructor
public class TokenRecuperacionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    public TokenRecuperacionData(Long usuarioId, String token, LocalDateTime fechaExpiracion) {
        this.usuarioId = usuarioId;
        this.token = token;
        this.fechaExpiracion = fechaExpiracion;
    }
}
'''
with open(token_data_path, 'w', encoding='utf-8') as f:
    f.write(token_data_content)

# 4. TokenRecuperacionRepository.java
token_repo_path = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/persistence/TokenRecuperacionRepository.java'
token_repo_content = '''package com.activachilecito.usuarios.persistence;

import com.activachilecito.usuarios.entity.data.TokenRecuperacionData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacionData, Long> {
    Optional<TokenRecuperacionData> findByToken(String token);
    void deleteByToken(String token);
}
'''
with open(token_repo_path, 'w', encoding='utf-8') as f:
    f.write(token_repo_content)

# 5. TokenRecuperacionAdapter.java
new_token_adapter = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/adapter/output/TokenRecuperacionAdapter.java'
token_adapter_content = '''package com.activachilecito.usuarios.adapter.output;

import com.activachilecito.usuarios.entity.data.TokenRecuperacionData;
import com.activachilecito.usuarios.persistence.TokenRecuperacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import output.TokenRecuperacionPort;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenRecuperacionAdapter implements TokenRecuperacionPort {

    private final TokenRecuperacionRepository repository;

    @Override
    @Transactional
    public String generarToken(Long usuarioId) {
        String token = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        TokenRecuperacionData data = new TokenRecuperacionData(
                usuarioId,
                token,
                LocalDateTime.now().plusHours(2)
        );
        repository.save(data);
        return token;
    }

    @Override
    public boolean esTokenValido(String token) {
        Optional<TokenRecuperacionData> tokenOpt = repository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            return false;
        }
        return tokenOpt.get().getFechaExpiracion().isAfter(LocalDateTime.now());
    }

    @Override
    public Long obtenerUsuarioIdPorToken(String token) {
        return repository.findByToken(token)
                .map(TokenRecuperacionData::getUsuarioId)
                .orElse(null);
    }

    @Override
    @Transactional
    public void invalidarToken(String token) {
        repository.deleteByToken(token);
    }
}
'''
with open(new_token_adapter, 'w', encoding='utf-8') as f:
    f.write(token_adapter_content)

old_token_adapter = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/adapter/output/DummyTokenRecuperacionAdapter.java'
if os.path.exists(old_token_adapter):
    os.remove(old_token_adapter)

# 6. application.properties
props_path = 'backend/Adapter/src/main/resources/application.properties'
with open(props_path, 'r', encoding='utf-8') as f:
    props_content = f.read()

if 'spring.mail.host' not in props_content:
    with open(props_path, 'a', encoding='utf-8') as f:
        f.write('\n# Spring Mail Configuration (Recuperacion de Password)\n')
        f.write('spring.mail.host=smtp.gmail.com\n')
        f.write('spring.mail.port=587\n')
        f.write('spring.mail.username=tu_correo@gmail.com\n')
        f.write('spring.mail.password=TU_CONTRASENA_DE_GMAIL_AQUI\n')
        f.write('spring.mail.properties.mail.smtp.auth=true\n')
        f.write('spring.mail.properties.mail.smtp.starttls.enable=true\n')

