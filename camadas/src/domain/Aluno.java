package domain;

/*
 * ============================================================
 * CAMADA: Domínio (Domain)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Representa o conceito puro de "Aluno" no negócio.
 *   Esta classe modela a entidade Aluno como ela existe no mundo real,
 *   sem nenhum conhecimento sobre banco de dados, tela ou HTTP.
 *
 * POR QUE ESTÁ NO DOMÍNIO?
 *   Porque "Aluno" é um conceito fundamental do negócio.
 *   Ele existiria mesmo que não houvesse computador.
 *   O Domínio é a camada mais interna e estável — muda apenas quando
 *   as regras de negócio mudam.
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - Qualquer camada pode usar Aluno (domain, application, infrastructure, presentation)
 *   - Porque todas as setas apontam para o Domínio.
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - NINGUÉM de fora do Domínio.
 *   - Aluno não importa nada de application/, presentation/ ou infrastructure/.
 *   - Isso garante que o Domínio é independente e reutilizável.
 * ============================================================
 */

// Nenhum import externo ao domínio — esta classe é 100% independente.

public class Aluno {

    // Identificador único do aluno (poderia ser matrícula, UUID, etc.)
    private String id;

    // Nome completo do aluno
    private String nome;

    /**
     * Construtor do Aluno.
     *
     * O QUE FAZ:
     *   Cria uma instância de Aluno validando as regras de negócio.
     *
     * POR QUE ESTÁ NESTA CAMADA?
     *   A validação de que "nome não pode ser vazio" é uma REGRA DE NEGÓCIO.
     *   Não importa se o dado vem de um formulário web, de um arquivo CSV ou
     *   de uma API — a regra é a mesma. Por isso fica no Domínio.
     *
     * @param id   identificador único do aluno
     * @param nome nome do aluno (não pode ser nulo ou vazio)
     * @throws IllegalArgumentException se o nome for nulo ou vazio
     */
    public Aluno(String id, String nome) {
        // REGRA DE NEGÓCIO: nome não pode ser vazio
        // Esta validação está aqui (e não no Controller ou Service) porque
        // é uma regra intrínseca do conceito "Aluno" no negócio.
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do aluno não pode ser vazio.");
        }
        this.id = id;
        this.nome = nome;
    }

    /**
     * Retorna o identificador do aluno.
     *
     * O QUE FAZ: acesso simples ao campo id.
     * POR QUE ESTÁ NESTA CAMADA? Getters pertencem à entidade de domínio.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome do aluno.
     *
     * O QUE FAZ: acesso simples ao campo nome.
     * POR QUE ESTÁ NESTA CAMADA? Getters pertencem à entidade de domínio.
     */
    public String getNome() {
        return nome;
    }
}
