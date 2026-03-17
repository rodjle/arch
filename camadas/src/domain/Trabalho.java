package domain;

// Import de ArrayList — classe da biblioteca padrão do Java (java.util).
// Usada para armazenar a lista mutável de notas deste trabalho.
import java.util.ArrayList;

// Import de List — interface da biblioteca padrão do Java (java.util).
// Usada como tipo do campo 'notas' para programar orientado a interfaces.
import java.util.List;

/*
 * ============================================================
 * CAMADA: Domínio (Domain)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Entidade central do sistema. Representa um trabalho apresentado por um aluno.
 *   Guarda as notas recebidas e contém a LÓGICA DE NEGÓCIO PURA
 *   para calcular a média das notas.
 *
 * POR QUE ESTÁ NO DOMÍNIO?
 *   "Trabalho" é o conceito mais importante do sistema de avaliação.
 *   A regra "calcular média das notas" é uma regra de negócio pura —
 *   não depende de banco, de tela ou de framework.
 *   NÃO sabe como salvar (isso é da Infraestrutura).
 *   NÃO sabe como exibir (isso é da Apresentação/Presentation).
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - Qualquer camada pode usar Trabalho.
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - Apenas outras classes do Domínio (Aluno, Nota).
 *   - NUNCA importa nada de application/, presentation/ ou infrastructure/.
 * ============================================================
 */

public class Trabalho {

    // Identificador único do trabalho
    private String id;

    // Título/tema do trabalho apresentado
    private String titulo;

    // O aluno que apresentou o trabalho (autor)
    private Aluno autor;

    // Lista de notas recebidas por outros alunos
    // Usamos List (interface) em vez de ArrayList (implementação concreta)
    // para seguir o princípio de programar orientado a interfaces.
    private List<Nota> notas;

    /**
     * Construtor do Trabalho.
     *
     * O QUE FAZ:
     *   Cria um trabalho com sua lista de notas vazia.
     *
     * POR QUE ESTÁ NESTA CAMADA?
     *   A criação de uma entidade de domínio pertence ao domínio.
     *   A inicialização da lista vazia é uma decisão de modelagem do negócio.
     *
     * @param id     identificador único do trabalho
     * @param titulo título/tema do trabalho
     * @param autor  o aluno que apresentou
     */
    public Trabalho(String id, String titulo, Aluno autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        // Inicializa a lista de notas como vazia.
        // Notas serão adicionadas depois, conforme outros alunos avaliem.
        this.notas = new ArrayList<>();
    }

    /**
     * Adiciona uma nota ao trabalho.
     *
     * O QUE FAZ:
     *   Registra uma nova nota na lista de notas deste trabalho.
     *
     * POR QUE ESTÁ NESTA CAMADA?
     *   Gerenciar a coleção de notas de um trabalho é responsabilidade
     *   da própria entidade. O trabalho "sabe" que ele possui notas.
     *   Isso é modelagem de domínio — não é orquestração (Service) nem persistência (Infra).
     *
     * @param nota a nota a ser adicionada
     */
    public void adicionarNota(Nota nota) {
        this.notas.add(nota);
    }

    /**
     * Calcula a média das notas recebidas neste trabalho.
     *
     * O QUE FAZ:
     *   Soma todas as notas e divide pelo total.
     *   Se não houver notas, retorna 0.0.
     *
     * POR QUE ESTÁ NESTA CAMADA E NÃO EM OUTRA?
     *   Este é o exemplo CLÁSSICO de lógica que pertence ao Domínio:
     *   - NÃO é orquestração → não vai no Service
     *   - NÃO é persistência → não vai no Repositório
     *   - NÃO é exibição → não vai no Controller
     *   - É uma REGRA DE NEGÓCIO PURA: "a média de um trabalho é a soma
     *     das notas dividida pela quantidade de avaliações"
     *
     *   Se colocássemos no Service, a entidade Trabalho ficaria "anêmica"
     *   (sem comportamento), violando princípios de modelagem rica de domínio.
     *
     * @return média das notas ou 0.0 se não houver avaliações
     */
    public double calcularMedia() {
        // REGRA DE NEGÓCIO: se não há notas, a média é 0.0
        // Evita divisão por zero e define um comportamento claro para o cenário.
        if (notas.isEmpty()) {
            return 0.0;
        }

        double soma = 0.0;
        for (Nota nota : notas) {
            soma += nota.getValor();
        }

        return soma / notas.size();
    }

    /**
     * Retorna o identificador do trabalho.
     *
     * O QUE FAZ: acesso simples ao campo id.
     * POR QUE ESTÁ NESTA CAMADA? Getters pertencem à entidade de domínio.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o título do trabalho.
     *
     * O QUE FAZ: acesso simples ao campo titulo.
     * POR QUE ESTÁ NESTA CAMADA? Getters pertencem à entidade de domínio.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Retorna o aluno autor do trabalho.
     *
     * O QUE FAZ: acesso ao autor do trabalho.
     * POR QUE ESTÁ NESTA CAMADA? Getters pertencem à entidade de domínio.
     */
    public Aluno getAutor() {
        return autor;
    }

    /**
     * Retorna a lista de notas recebidas.
     *
     * O QUE FAZ: acesso à lista de notas.
     * POR QUE ESTÁ NESTA CAMADA? Getters pertencem à entidade de domínio.
     */
    public List<Nota> getNotas() {
        return notas;
    }
}
