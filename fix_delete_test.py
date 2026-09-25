import os

file_path = 'backend/Adapter/src/test/java/com/activachilecito/usuarios/controller/UsuarioDeleteControllerTest.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;', 'import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;\nimport static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;')
content = content.replace('delete("/api/usuarios/{id}", userId)', 'delete("/api/usuarios/me").with(user(new com.activachilecito.usuarios.config.CustomUserDetails(userId, "test@test.com", "", "DEPORTISTA", true)))')

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
