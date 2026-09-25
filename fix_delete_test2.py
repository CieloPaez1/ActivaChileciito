import os

file_path = 'backend/Adapter/src/test/java/com/activachilecito/usuarios/controller/UsuarioDeleteControllerTest.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;', 'import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;')
content = content.replace('.with(user(new com.activachilecito.usuarios.config.CustomUserDetails(userId, "test@test.com", "", "DEPORTISTA", true)))', '.principal(new UsernamePasswordAuthenticationToken(new com.activachilecito.usuarios.config.CustomUserDetails(userId, "test@test.com", "", "DEPORTISTA", true), null))')

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
