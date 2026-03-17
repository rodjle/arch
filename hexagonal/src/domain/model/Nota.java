// Arquivo: src/domain/model/Nota.java
package domain.model;

/*
 * ============================================================
 * CAMADA: DOMAIN - MODEL (Núcleo Hexagonal)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Representa uma avaliação dada por um aluno a um trabalho.
 *   Valida que a nota deve estar entre 0 e 10.
 *
 * POR QUE ESTÁ NO DOMAIN?
 *   A regra "nota deve estar entre 0 e 10" é uma REGRA DE NEGÓCIO
 *   definida pelo sistema de avaliação acadêmica. Não é um detalhe
 *   técnico de banco de dados ou interface gráfica.
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - Trabalho (entidade que agrega notas)
 *   - Application Layer (GerenciadorAvaliacoes)
 *   - Adapters (para conversão de dados)
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   NINGUÉM - Entidade de domínio pura, sem dependências.
 *
 * HEXAGONAL ARCHITECTURE:
 *   Esta classe está no NÚCLEO DO HEXÁGONO.
 *   É uma ENTIDADE DE DOMÍNIO que encapsula uma regra de negócio
 *   fundamental: o range válido para uma avaliação.
 *   
 * DIFERENÇA DA ARQUITETURA EM CAMADAS:
 *   Na arquitetura em camadas, esta classe também estava no domain.
 *   A diferença conceitual da Hexagonal é a GARANTIA DE ISOLAMENTO:
 *   sabemos com certeza que nada no Domain depende de frameworks,
 *   bancos de dados, ou detalhes de infraestrutura. O Domain é
 *   100% portável e testável isoladamente.
 * ============================================================
 */

/**
 * Representa uma nota atribuída por um avaliador a um trabalho.
 * 
 * VALIDAÇÕES:
 * - Valor deve estar entre 0.0 e 10.0 (escala brasileira)
 * - IDs não podem ser nulos
 */
public class Nota {
    
    private final String id;
    private final String avaliadorId;
    private final double valor;
    
    /**
     * Construtor da entidade Nota.
     * 
     * @param id Identificador único da nota
     * @param avaliadorId ID do aluno que deu a nota
     * @param valor Valor da nota (0.0 a 10.0)
     * @throws IllegalArgumentException se valor não estiver entre 0 e 10
     * 
     * POR QUE A VALIDAÇÃO ESTÁ AQUI?
     * A regra "nota entre 0 e 10" é definida pelo sistema de avaliação
     * acadêmica (o negócio), não pela tecnologia. Se um dia mudarmos
     * de banco de dados ou interface web, essa regra continuará valendo.
     * Por isso está encapsulada na entidade de domínio.
     * 
     * CONTEXTO HEXAGONAL:
     * Esta validação é executada ANTES de qualquer Adapter tentar
     * persistir a nota. Assim, garantimos que dados inválidos jamais
     * chegam aos Adapters de saída (repositórios).
     */
    public Nota(String id, String avaliadorId, double valor) {
        if (valor < 0.0 || valor > 10.0) {
            throw new IllegalArgumentException(
                String.format(
                    "Nota deve estar entre 0 e 10! Valor recebido: %.2f " +
                    "(Esta é uma regra de negócio do sistema de avaliação acadêmica)",
                    valor
                )
            );
        }
        this.id = id;
        this.avaliadorId = avaliadorId;
        this.valor = valor;
    }
    
    /**
     * Retorna o ID único da nota.
     * 
     * @return ID da nota
     */
    public String getId() {
        return id;
    }
    
    /**
     * Retorna o ID do aluno que deu esta avaliação.
     * 
     * @return ID do avaliador
     */
    public String getAvaliadorId() {
        return avaliadorId;
    }
    
    /**
     * Retorna o valor numérico da nota.
     * 
     * @return Valor entre 0.0 e 10.0
     */
    public double getValor() {
        return valor;
    }
    
    /**
     * Representação textual da nota para debug/logging.
     * 
     * @return String no formato "Nota{id='...', avaliador='...', valor=N.N}"
     */
    @Override
    public String toString() {
        return String.format("Nota{id='%s', avaliador='%s', valor=%.2f}", 
                           id, avaliadorId, valor);
    }
}
