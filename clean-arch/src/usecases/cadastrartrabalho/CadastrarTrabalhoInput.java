// Arquivo: src/usecases/cadastrartrabalho/CadastrarTrabalhoInput.java
package usecases.cadastrartrabalho;

/**
 * 🎓 DTO DE ENTRADA: CADASTRAR TRABALHO
 * 
 * Este é um Data Transfer Object (DTO) que carrega os dados
 * necessários para executar o Use Case de cadastrar trabalho.
 * 
 * POR QUE USAR DTOs?
 * - Desacopla os Use Cases dos detalhes de apresentação
 * - Controller pode estar em REST, GraphQL, Console, etc
 * - Use Case não precisa saber de onde vieram os dados
 * - Facilita testes (criar Input é simples)
 * 
 * DIFERENÇA DE HEXAGONAL:
 * - Em Hexagonal, DTOs são opcionais
 * - Em Clean Architecture, são fortemente recomendados nas fronteiras
 * - Uncle Bob enfatiza: "Dados cruzam fronteiras como structs simples"
 * 
 * VALIDAÇÕES NO INPUT:
 * - Apenas validações de FORMATO (null, vazio)
 * - NÃO validações de NEGÓCIO (essas ficam nas Entities)
 * - Exemplo: Verificar se ID não é null (formato) vs Verificar se aluno existe (negócio)
 */
public class CadastrarTrabalhoInput {
    private final String trabalhoId;
    private final String titulo;
    private final String alunoId;

    /**
     * Construtor com validações básicas de formato.
     * 
     * @param trabalhoId ID do trabalho a ser cadastrado
     * @param titulo Título do trabalho
     * @param alunoId ID do aluno autor
     */
    public CadastrarTrabalhoInput(String trabalhoId, String titulo, String alunoId) {
        // Validações de FORMATO (não de NEGÓCIO)
        if (trabalhoId == null || trabalhoId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do trabalho não pode ser vazio");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título não pode ser vazio");
        }
        if (alunoId == null || alunoId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do aluno não pode ser vazio");
        }
        
        this.trabalhoId = trabalhoId;
        this.titulo = titulo;
        this.alunoId = alunoId;
    }

    public String getTrabalhoId() {
        return trabalhoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAlunoId() {
        return alunoId;
    }
}
