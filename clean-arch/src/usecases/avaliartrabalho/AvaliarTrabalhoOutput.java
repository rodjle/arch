// Arquivo: src/usecases/avaliartrabalho/AvaliarTrabalhoOutput.java
package usecases.avaliartrabalho;

/**
 * 🎓 DTO DE SAÍDA: AVALIAR TRABALHO
 * 
 * Carrega os dados resultantes da execução do Use Case de avaliar trabalho.
 * 
 * O QUE O OUTPUT CONTÉM?
 * - Informações sobre o trabalho avaliado
 * - A nota atribuída
 * - A nova média do trabalho (após a avaliação)
 * - Mensagem de feedback
 */
public class AvaliarTrabalhoOutput {
    private final String trabalhoId;
    private final String tituloTrabalho;
    private final String nomeAvaliador;
    private final double notaAtribuida;
    private final double novaMedia;
    private final int quantidadeAvaliacoes;
    private final String mensagem;
    private final boolean sucesso;

    /**
     * Construtor completo.
     */
    public AvaliarTrabalhoOutput(String trabalhoId, String tituloTrabalho, String nomeAvaliador,
                                  double notaAtribuida, double novaMedia, int quantidadeAvaliacoes,
                                  String mensagem, boolean sucesso) {
        this.trabalhoId = trabalhoId;
        this.tituloTrabalho = tituloTrabalho;
        this.nomeAvaliador = nomeAvaliador;
        this.notaAtribuida = notaAtribuida;
        this.novaMedia = novaMedia;
        this.quantidadeAvaliacoes = quantidadeAvaliacoes;
        this.mensagem = mensagem;
        this.sucesso = sucesso;
    }

    /**
     * Factory method para criar Output de sucesso.
     */
    public static AvaliarTrabalhoOutput sucesso(String trabalhoId, String tituloTrabalho,
                                                  String nomeAvaliador, double notaAtribuida,
                                                  double novaMedia, int quantidadeAvaliacoes) {
        String mensagem = String.format(
            "✅ Trabalho '%s' avaliado por %s com nota %.1f! Nova média: %.2f (%d avaliações)",
            tituloTrabalho, nomeAvaliador, notaAtribuida, novaMedia, quantidadeAvaliacoes
        );
        return new AvaliarTrabalhoOutput(trabalhoId, tituloTrabalho, nomeAvaliador,
                                          notaAtribuida, novaMedia, quantidadeAvaliacoes,
                                          mensagem, true);
    }

    /**
     * Factory method para criar Output de erro.
     */
    public static AvaliarTrabalhoOutput erro(String mensagem) {
        return new AvaliarTrabalhoOutput(null, null, null, 0.0, 0.0, 0,
                                          "❌ " + mensagem, false);
    }

    public String getTrabalhoId() {
        return trabalhoId;
    }

    public String getTituloTrabalho() {
        return tituloTrabalho;
    }

    public String getNomeAvaliador() {
        return nomeAvaliador;
    }

    public double getNotaAtribuida() {
        return notaAtribuida;
    }

    public double getNovaMedia() {
        return novaMedia;
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
