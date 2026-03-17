// Arquivo: src/usecases/cadastrartrabalho/CadastrarTrabalhoOutput.java
package usecases.cadastrartrabalho;

/**
 * 🎓 DTO DE SAÍDA: CADASTRAR TRABALHO
 * 
 * Este é um Data Transfer Object (DTO) que carrega os dados
 * resultantes da execução do Use Case de cadastrar trabalho.
 * 
 * POR QUE USAR OUTPUT DTO?
 * - Desacopla os Use Cases dos Presenters
 * - Use Case não precisa saber como os dados serão apresentados
 * - Presenter pode formatar para Console, JSON, HTML, etc
 * - Facilita testes (verificar Output é simples)
 * 
 * O QUE VAI NO OUTPUT?
 * - Dados que o Presenter precisa para exibir o resultado
 * - Pode ser diferente das Entities (é uma "view" dos dados)
 * - Geralmente inclui mensagens de sucesso/erro
 * 
 * DIFERENÇA DE HEXAGONAL:
 * - Em Hexagonal, Application Service pode retornar Entity diretamente
 * - Em Clean, Use Case retorna Output DTO específico
 * - Isso dá mais controle sobre o que é exposto
 */
public class CadastrarTrabalhoOutput {
    private final String trabalhoId;
    private final String titulo;
    private final String nomeAutor;
    private final String mensagem;
    private final boolean sucesso;

    /**
     * Construtor para criar o resultado do Use Case.
     * 
     * @param trabalhoId ID do trabalho cadastrado
     * @param titulo Título do trabalho
     * @param nomeAutor Nome do aluno autor
     * @param mensagem Mensagem de feedback
     * @param sucesso Indica se a operação foi bem-sucedida
     */
    public CadastrarTrabalhoOutput(String trabalhoId, String titulo, String nomeAutor, 
                                    String mensagem, boolean sucesso) {
        this.trabalhoId = trabalhoId;
        this.titulo = titulo;
        this.nomeAutor = nomeAutor;
        this.mensagem = mensagem;
        this.sucesso = sucesso;
    }

    /**
     * Factory method para criar Output de sucesso.
     */
    public static CadastrarTrabalhoOutput sucesso(String trabalhoId, String titulo, String nomeAutor) {
        String mensagem = String.format(
            "✅ Trabalho '%s' cadastrado com sucesso! Autor: %s",
            titulo, nomeAutor
        );
        return new CadastrarTrabalhoOutput(trabalhoId, titulo, nomeAutor, mensagem, true);
    }

    /**
     * Factory method para criar Output de erro.
     */
    public static CadastrarTrabalhoOutput erro(String mensagem) {
        return new CadastrarTrabalhoOutput(null, null, null, "❌ " + mensagem, false);
    }

    public String getTrabalhoId() {
        return trabalhoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public String getMensagem() {
        return mensagem;
    }

    public boolean isSucesso() {
        return sucesso;
    }
}
