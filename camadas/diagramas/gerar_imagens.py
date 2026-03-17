import urllib.request
import urllib.parse
import base64
import os

# Caminho para a pasta de diagramas
diagramas_dir = r"g:\Meu Drive\faccat\Padrões e Arquitetura\2026-1\Aula-03\arq\camadas\diagramas"

# Arquivos Mermaid para converter
mermaid_files = [
    "arquitetura-componentes.mmd",
    "fluxo-sequencia.mmd"
]

print("Gerando imagens PNG a partir dos diagramas Mermaid...")

for mmd_file in mermaid_files:
    mmd_path = os.path.join(diagramas_dir, mmd_file)
    
    # Ler o conteúdo do arquivo Mermaid
    with open(mmd_path, 'r', encoding='utf-8') as f:
        mermaid_code = f.read()
    
    # Codificar em base64
    encoded = base64.b64encode(mermaid_code.encode('utf-8')).decode('utf-8')
    
    # URL da API do mermaid.ink
    url = f"https://mermaid.ink/img/{encoded}"
    
    # Nome do arquivo de saída (PNG)
    png_file = mmd_file.replace('.mmd', '.png')
    png_path = os.path.join(diagramas_dir, png_file)
    
    # Baixar a imagem
    try:
        urllib.request.urlretrieve(url, png_path)
        print(f"✅ {png_file} gerado com sucesso!")
    except Exception as e:
        print(f"❌ Erro ao gerar {png_file}: {e}")

print("\nConversão concluída!")
