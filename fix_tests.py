import os
import glob
import re

test_files = glob.glob('backend/Adapter/src/test/java/com/activachilecito/**/controller/*Test.java', recursive=True)

for file_path in test_files:
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    if '@WebMvcTest' in content and 'JwtProviderAdapter' not in content:
        # Add import
        content = content.replace('import org.springframework.boot.test.mock.mockito.MockBean;', 'import org.springframework.boot.test.mock.mockito.MockBean;\nimport com.activachilecito.usuarios.adapter.output.JwtProviderAdapter;')
        
        # Add MockBean
        content = re.sub(r'(@MockBean\s+private\s+\w+\s+\w+;)', r'\1\n\n    @MockBean\n    private JwtProviderAdapter jwtProviderAdapter;', content, count=1)
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
            
