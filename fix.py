import os

def clean_file(path):
    try:
        with open(path, 'r', encoding='utf-8') as f:
            content = f.read()
            
        new_content = content.replace('Dueǟo', 'Dueno')\
                             .replace('dueǟo', 'dueno')\
                             .replace('Operaciǟn', 'Operacion')\
                             .replace('ǟ', 'o')\
                             .replace('\u00b1', 'n')
        
        if content != new_content:
            with open(path, 'w', encoding='utf-8') as f:
                f.write(new_content)
    except Exception as e:
        pass

for root, dirs, files in os.walk('backend'):
    for file in files:
        if file.endswith('.java'):
            clean_file(os.path.join(root, file))
