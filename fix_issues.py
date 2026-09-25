import os

# 1. CustomUserDetails.java
file_path = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/config/CustomUserDetails.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()
content = content.replace('private final String role;', 'private final String role;\n    private final boolean activo;')
content = content.replace('public boolean isEnabled() { return true; }', 'public boolean isEnabled() { return activo; }')
with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

# 2. CustomUserDetailsService.java
file_path = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/config/CustomUserDetailsService.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()
content = content.replace('usuario.getRol().name()', 'usuario.getRol().name(),\n                usuario.isActivo()')
with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

# 3. JwtProviderAdapter.java
file_path = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/adapter/output/JwtProviderAdapter.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()
content = content.replace('claims.put("rol", userDetails.getRole());', 'claims.put("rol", userDetails.getRole());\n        claims.put("activo", userDetails.isEnabled());')
content = content.replace('public Long extraerId(String token) {', 'public boolean extraerActivo(String token) {\n        Object obj = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("activo");\n        return obj != null && (Boolean) obj;\n    }\n    \n    public Long extraerId(String token) {')
with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

# 4. JwtAuthenticationFilter.java
file_path = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/config/JwtAuthenticationFilter.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()
content = content.replace('String rol = jwtProviderAdapter.extraerRol(jwt);', 'String rol = jwtProviderAdapter.extraerRol(jwt);\n                        boolean activo = jwtProviderAdapter.extraerActivo(jwt);')
content = content.replace('new CustomUserDetails(id, email, "", rol);', 'new CustomUserDetails(id, email, "", rol, activo);')
with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

# 5. UsuarioDeleteController.java
file_path = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/controller/UsuarioDeleteController.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()
content = content.replace('("/{id}")', '("/me")')
content = content.replace('eliminar(@PathVariable Long id)', 'eliminar(org.springframework.security.core.Authentication authentication)')
content = content.replace('eliminarUsuarioInput.eliminarUsuario(id);', 'com.activachilecito.usuarios.config.CustomUserDetails userDetails = (com.activachilecito.usuarios.config.CustomUserDetails) authentication.getPrincipal();\n        eliminarUsuarioInput.eliminarUsuario(userDetails.getId());')
with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

