// Arquivo: src/usecases/consultarmedia/ConsultarMediaInput.java
package usecases.consultarmedia;

/**
 * 🎓 DTO DE ENTRADA: CONSULTAR MÉDIA
 * 
 * Carrega os dados necessários para executar o Use Case de consultar média.
 * 
 * USO DE DTOs MESMO EM CASOS SIMPLES:
 * - Mesmo que o Input contenha apenas um campo (trabalhoId)
 * - DTOs criam uma fronteira clara e consistente
 * - Facilita evolução futura (adicionar campos sem quebrar assinatura)
 * - Mantém consistência com outros Use Cases
 */
public class ConsultarMediaInput {
    private final String trabalhoId;

    /**
     * Construtor com validações básicas de formato.
     * 
     * @param trabalhoId ID do trabalho para consultar média
     */
    public ConsultarMediaInput(String trabalhoId) {
        // Validação de FORMATO apenas
        if (trabalhoId == null || trabalhoId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do trabalho não pode ser vazio");
        }
        
        this.trabalhoId = trabalhoId;
    }

    public String getTrabalhoId() {
        return trabalhoId;
    }
}
