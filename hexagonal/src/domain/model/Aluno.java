// Arquivo: src/domain/model/Aluno.java
package domain.model;

/*
 * ============================================================
 * CAMADA: DOMAIN - MODEL (Núcleo Hexagonal)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Representa a entidade Aluno com suas regras de negócio.
 *   Valida que o nome não pode ser vazio.
 *
 * POR QUE ESTÁ NO DOMAIN?
 *   Aluno é um conceito fundamental do negócio. As regras sobre
 *   o que torna um aluno válido são REGRAS DE NEGÓCIO, não
 *   detalhes técnicos de como armazenamos ou exibimos dados.
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - Application Layer (GerenciadorAvaliacoes)
 *   - Adapters (Controllers, Repositórios)
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   NINGUÉM - Entidades de domínio não dependem de outras camadas.
 *   Elas são o centro independente do sistema.
 *
 * HEXAGONAL ARCHITECTURE:
 *   Esta classe está no NÚCLEO DO HEXÁGONO.
 *   - NÃO é uma Port (interface)
 *   - NÃO é um Adapter (implementação externa)
 *   - É uma ENTIDADE DE DOMÍNIO pura
 *   
 * DIFERENÇA DA ARQUITETURA EM CAMADAS:
 *   Na arquitetura em camadas, esta classe também estava no
 *   domain, mas a diferença é que lá as camadas de cima
 *   (Application) dependiam de camadas de baixo (Infrastructure)
 *   diretamente. Na Hexagonal, o Domain é 100% isolado e
 *   independente - nada aqui conhece Infrastructure ou Adapters.
 * ============================================================
 */

/**
 * Representa um aluno no sistema de avaliação de trabalhos.
 * 
 * VALIDAÇÕES:
 * - ID não pode ser nulo
 * - Nome não pode ser vazio ou nulo
 */
public class Aluno {
    
    private final String id;
    private final String nome;
    
    /**
     * Construtor da entidade Aluno.
     * 
     * @param id Identificador único do aluno
     * @param nome Nome completo do aluno
     * @throws IllegalArgumentException se nome for vazio ou nulo
     * 
     * POR QUE A VALIDAÇÃO ESTÁ AQUI?
     * Validar que um aluno deve ter nome é uma REGRA DE NEGÓCIO,
     * não um detalhe técnico. Por isso, a validação fica na entidade
     * do domínio, garantindo que seja impossível criar um aluno
     * inválido em qualquer parte do sistema.
     */
    public Aluno(String id, String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Nome do aluno não pode ser vazio! " +
                "Esta é uma regra de negócio fundamental."
            );
        }
        this.id = id;
        this.nome = nome;
    }
    
    /**
     * Retorna o ID único do aluno.
     * 
     * @return ID do aluno
     */
    public String getId() {
        return id;
    }
    
    /**
     * Retorna o nome do aluno.
     * 
     * @return Nome completo do aluno
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Representação textual do aluno para debug/logging.
     * 
     * @return String no formato "Aluno{id='...', nome='...'}"
     */
    @Override
    public String toString() {
        return "Aluno{id='" + id + "', nome='" + nome + "'}";
    }
}
