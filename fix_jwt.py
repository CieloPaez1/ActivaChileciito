import os

file_path = 'backend/Adapter/src/main/java/com/activachilecito/usuarios/adapter/output/JwtProviderAdapter.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('userDetails.getRol()', 'userDetails.getRole()')

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
