import os
import re

file_path = 'backend/Adapter/src/main/java/com/activachilecito/complejo/mapper/ComplejoMapper.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

content = content.replace('entity.getPrestaciones()', 'entity.getPrestaciones(),\n                new java.util.ArrayList<>()')

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

os.remove('backend/Adapter/src/main/java/com/activachilecito/usuarios/adapter/output/DummyUsuarioOutputAdapter.java')
