# Diagramas da Arquitetura em Camadas

Esta pasta contém os diagramas do sistema de Avaliação de Trabalhos.

## Arquivos Disponíveis

### Diagramas Mermaid (.mmd)
- `arquitetura-componentes.mmd` - Diagrama de componentes mostrando as 4 camadas
- `fluxo-sequencia.mmd` - Diagrama de sequência dos 3 casos de uso

### Diagramas PlantUML (.puml)
- `c4-contexto.puml` - Diagrama C4 de contexto
- `c4-componentes.puml` - Diagrama C4 de componentes

## ⚡ FORMA MAIS FÁCIL: Abra o arquivo HTML!

**Recomendado:** Abra o arquivo `diagramas.html` no navegador. Ele renderiza todos os diagramas com cores e permite:
- Salvar cada diagrama como imagem (botão direito → "Salvar imagem como...")
- Imprimir para PDF (Ctrl+P)
- Tirar screenshots (Win+Shift+S)

## Outras  Formas de Visualizar os Diagramas Mermaid

### Opção 1: VS Code
1. Instale a extensão "Markdown Preview Mermaid Support"
2. Crie um arquivo `preview.md` com o conteúdo:
```markdown
# Diagrama de Componentes
```mermaid
[cole aqui o conteúdo de arquitetura-componentes.mmd]
```
```
3. Abra o Preview do Markdown (Ctrl+Shift+V)
4. Clique com botão direito → "Export to HTML" ou tire screenshot

### Opção 2: Mermaid Live Editor (online)
1. Acesse https://mermaid.live
2. Cole o conteúdo de qualquer arquivo `.mmd`
3. Clique em "Download PNG" ou "Download SVG"

### Opção 3: Gerar PNGs via CLI
Se tiver Node.js instalado:
```bash
npx -y @mermaid-js/mermaid-cli -i arquitetura-componentes.mmd -o arquitetura-componentes.png
npx -y @mermaid-js/mermaid-cli -i fluxo-sequencia.mmd -o fluxo-sequencia.png
```

## Como Gerar os Diagramas PlantUML

### Usando Java (já tem plantuml.jar?)
```bash
java -jar plantuml.jar -tjpg c4-contexto.puml
java -jar plantuml.jar -tjpg c4-componentes.puml
```

### Ou baixe o PlantUML:
```bash
curl -L -o plantuml.jar https://github.com/plantuml/plantuml/releases/download/v1.2024.8/plantuml-1.2024.8.jar
java -jar plantuml.jar -tjpg *.puml
```

### Opção Online:
1. Acesse http://www.plantuml.com/plantuml/uml
2. Cole o conteúdo do arquivo `.puml`
3. Copie a imagem gerada

## Legenda de Cores

### Diagrama de Componentes
- 🟦 **Azul** = Camada Presentation (Controller)
- 🟩 **Verde** = Camada Application (Gerenciador)
- 🟨 **Amarelo** = Camada Domain (Entidades)
- 🟥 **Vermelho** = Camada Infrastructure (Repositórios)

### Diagramas C4
- Seguem o padrão C4 Model com cores específicas para Person, System, Component

## ⚠️ Problema Arquitetural Identificado

No diagrama de componentes, você verá setas marcadas com **☠️ PROBLEMA!** indicando onde a arquitetura em camadas clássica tem uma limitação crítica:

**O GerenciadorAvaliacoes (Application) depende diretamente das classes concretas dos repositórios (Infrastructure).**

Consequências:
- ❌ Acoplamento forte entre Application e Infrastructure
- ❌ Dificulta testes unitários (não consegue "mockar" repositórios facilmente)
- ❌ Viola o Dependency Inversion Principle (DIP)
- ❌ Para trocar a implementação do repositório, precisa modificar código

**Solução:** Na Arquitetura Hexagonal (próximo exemplo), esse problema é resolvido com interfaces (Ports) no Domain e implementações (Adapters) na Infrastructure, invertendo a dependência.
