package domain;

/*
 * ============================================================
 * CAMADA: Domínio (Domain)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Representa uma nota dada por um aluno avaliador a um trabalho.
 *   Contém a regra de negócio de que o valor da nota deve estar entre 0 e 10.
 *
 * POR QUE ESTÁ NO DOMÍNIO?
 *   "Nota" é um conceito de negócio que existe independentemente de
 *   tecnologia. A restrição de valor entre 0 e 10 é uma REGRA DO NEGÓCIO,
 *   não uma validação de formulário.
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - Qualquer camada pode criar e usar objetos Nota.
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - Apenas outras classes do Domínio (neste caso, usa Aluno).
 *   - NUNCA importa nada de application/, presentation/ ou infrastructure/.
 * ============================================================
 */

// Nenhum import externo ao domínio.
// Aluno está no mesmo pacote (domain), então não precisa de import explícito.

public class Nota {

    // Identificador único da nota
    private String id;

    // O aluno que deu a nota (avaliador)
    private Aluno avaliador;

    // O valor da nota atribuída (deve ser entre 0 e 10)
    private double valor;

    /**
     * Construtor da Nota.
     *
     * O QUE FAZ:
     *   Cria uma nota validando que o valor está no intervalo permitido [0, 10].
     *
     * POR QUE ESTÁ NESTA CAMADA?
     *   A validação de valor entre 0 e 10 é uma REGRA DE NEGÓCIO.
     *   Não importa se o valor veio de um formulário web, de um app mobile
     *   ou de uma planilha — a regra é sempre a mesma.
     *   Por isso, essa validação pertence ao Domínio.
     *
     * @param id        identificador único da nota
     * @param avaliador o aluno que está dando a nota
     * @param valor     o valor da nota (deve estar entre 0 e 10)
     * @throws IllegalArgumentException se o valor estiver fora do intervalo [0, 10]
     */
    public Nota(String id, Aluno avaliador, double valor) {
        // REGRA DE NEGÓCIO: valor da nota deve estar entre 0 e 10.
        // Se colocássemos essa validação no Controller ou no Service,
        // alguém poderia criar uma Nota com valor 999 diretamente no código.
        // Validar aqui garante consistência em qualquer cenário.
        if (valor < 0 || valor > 10) {
            throw new IllegalArgumentException("Valor da nota deve estar entre 0 e 10. Recebido: " + valor);
        }
        this.id = id;
        this.avaliador = avaliador;
        this.valor = valor;
    }

    /**
     * Retorna o identificador da nota.
     *
     * O QUE FAZ: acesso simples ao campo id.
     * POR QUE ESTÁ NESTA CAMADA? Getters pertencem à entidade de domínio.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o aluno avaliador.
     *
     * O QUE FAZ: acesso ao aluno que deu a nota.
     * POR QUE ESTÁ NESTA CAMADA? Getters pertencem à entidade de domínio.
     */
    public Aluno getAvaliador() {
        return avaliador;
    }

    /**
     * Retorna o valor da nota.
     *
     * O QUE FAZ: acesso ao valor da nota.
     * POR QUE ESTÁ NESTA CAMADA? Getters pertencem à entidade de domínio.
     */
    public double getValor() {
        return valor;
    }
}
