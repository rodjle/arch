// Arquivo: src/usecases/consultarmedia/ConsultarMediaOutput.java
package usecases.consultarmedia;

/**
 * 🎓 DTO DE SAÍDA: CONSULTAR MÉDIA
 * 
 * Carrega os dados resultantes da execução do Use Case de consultar média.
 * 
 * O QUE O OUTPUT CONTÉM?
 * - Informações sobre o trabalho consultado
 * - A média das avaliações
 * - Quantidade de avaliações recebidas
 * - Mensagem de feedback
 */
public class ConsultarMediaOutput {
    private final String trabalhoId;
    private final String titulo;
    private final String nomeAutor;
    private final double media;
    private final int quantidadeAvaliacoes;
    private final String mensagem;
    private final boolean sucesso;

    /**
     * Construtor completo.
     */
    public ConsultarMediaOutput(String trabalhoId, String titulo, String nomeAutor,
                                 double media, int quantidadeAvaliacoes,
                                 String mensagem, boolean sucesso) {
        this.trabalhoId = trabalhoId;
        this.titulo = titulo;
        this.nomeAutor = nomeAutor;
        this.media = media;
        this.quantidadeAvaliacoes = quantidadeAvaliacoes;
        this.mensagem = mensagem;
        this.sucesso = sucesso;
    }

    /**
     * Factory method para criar Output de sucesso.
     */
    public static ConsultarMediaOutput sucesso(String trabalhoId, String titulo, String nomeAutor,
                                                double media, int quantidadeAvaliacoes) {
        String mensagem;
        if (quantidadeAvaliacoes == 0) {
            mensagem = String.format(
                "ℹ️ Trabalho '%s' ainda não recebeu avaliações.",
                titulo
            );
        } else {
            mensagem = String.format(
                "✅ Trabalho '%s' (Autor: %s) - Média: %.2f (%d avaliações)",
                titulo, nomeAutor, media, quantidadeAvaliacoes
            );
        }
        return new ConsultarMediaOutput(trabalhoId, titulo, nomeAutor, media,
                                         quantidadeAvaliacoes, mensagem, true);
    }

    /**
     * Factory method para criar Output de erro.
     */
    public static ConsultarMediaOutput erro(String mensagem) {
        return new ConsultarMediaOutput(null, null, null, 0.0, 0,
                                         "❌ " + mensagem, false);
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

    public double getMedia() {
        return media;
    }

    public int getQuantidadeAvaliacoes() {
        return quantidadeAvaliacoes;
    }

    public String getMensagem() {
        return mensagem;
    }

    public boolean isSucesso() {
        return sucesso;
    }
}
