// Arquivo: src/usecases/avaliartrabalho/AvaliarTrabalhoInput.java
package usecases.avaliartrabalho;

/**
 * 🎓 DTO DE ENTRADA: AVALIAR TRABALHO
 * 
 * Carrega os dados necessários para executar o Use Case de avaliar trabalho.
 * 
 * POR QUE DTOs SÃO IMPORTANTES?
 * - Criam uma fronteira clara entre camadas
 * - Controller não precisa conhecer detalhes internos do Use Case
 * - Use Case não precisa conhecer detalhes do Controller
 * - Facilita mudanças (pode trocar Controller sem afetar Use Case)
 */
public class AvaliarTrabalhoInput {
    private final String trabalhoId;
    private final String alunoAvaliadorId;
    private final double nota;

    /**
     * Construtor com validações básicas de formato.
     * 
     * @param trabalhoId ID do trabalho a ser avaliado
     * @param alunoAvaliadorId ID do aluno que está avaliando
     * @param nota Nota atribuída (validação de negócio será na Entity)
     */
    public AvaliarTrabalhoInput(String trabalhoId, String alunoAvaliadorId, double nota) {
        // Validações de FORMATO apenas
        if (trabalhoId == null || trabalhoId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do trabalho não pode ser vazio");
        }
        if (alunoAvaliadorId == null || alunoAvaliadorId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do avaliador não pode ser vazio");
        }
        
        this.trabalhoId = trabalhoId;
        this.alunoAvaliadorId = alunoAvaliadorId;
        this.nota = nota;
    }

    public String getTrabalhoId() {
        return trabalhoId;
    }

    public String getAlunoAvaliadorId() {
        return alunoAvaliadorId;
    }

    public double getNota() {
        return nota;
    }
}
