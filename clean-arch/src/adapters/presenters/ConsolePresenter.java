// Arquivo: src/adapters/presenters/ConsolePresenter.java
package adapters.presenters;

import usecases.cadastrartrabalho.CadastrarTrabalhoOutput;
import usecases.avaliartrabalho.AvaliarTrabalhoOutput;
import usecases.consultarmedia.ConsultarMediaOutput;

/*
 * ============================================================
 * CLEAN ARCHITECTURE - CAMADA: INTERFACE ADAPTERS (Camada 3)
 * Presenter
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Formata os Outputs dos Use Cases para apresentação ao usuário.
 *   Separa a lógica de formatação da lógica de negócio.
 *
 * POSIÇÃO NA CLEAN ARCHITECTURE:
 *   ○ Entities
 *   ○ Use Cases
 *   ● ← Você está aqui (Interface Adapters - Presenter)
 *   ○ Frameworks & Drivers
 *
 * TIPOS DE ADAPTERS:
 *   - Controllers: Recebem requisições, chamam Use Cases
 *   - Presenters: Formatam Outputs para visualização ← VOCÊ ESTÁ AQUI
 *   - Gateways: Implementam interfaces de persistência
 *
 * REGRA DE DEPENDÊNCIA:
 *   - PODE depender de: Use Cases (Outputs), Entities
 *   - NÃO PODE depender de: Frameworks & Drivers (camada externa)
 *
 * DIFERENÇA DE HEXAGONAL:
 *   Em Hexagonal: Formatação geralmente fica no Controller
 *   Em Clean: Presenter separado (responsabilidade única)
 *   
 *   Clean separa mais claramente:
 *   - Controller: Recebe entrada, chama Use Case
 *   - Presenter: Formata saída para exibição
 *   - Isso facilita trocar a forma de apresentação (Console, Web, Mobile)
 *
 * POR QUE SEPARAR PRESENTER DO CONTROLLER?
 *   - Single Responsibility Principle
 *   - Permite trocar apresentação sem afetar controle de fluxo
 *   - Poderia ter: ConsolePresenter, JsonPresenter, HtmlPresenter, etc
 * ============================================================
 */

/**
 * 🎓 PRESENTER: CONSOLE
 * 
 * Responsável por formatar os Outputs dos Use Cases para exibição no console.
 * 
 * RESPONSABILIDADE:
 * - Receber Outputs (DTOs) dos Use Cases
 * - Formatar para exibição no console
 * - NÃO contém lógica de negócio!
 * 
 * PADRÃO PRESENTER:
 * - Use Cases não sabem COMO os dados serão apresentados
 * - Presenter traduz Outputs em formato específico (Console, JSON, HTML, etc)
 * - Isso permite trocar a apresentação sem afetar os Use Cases
 * 
 * EXEMPLO DE FLEXIBILIDADE:
 * - ConsolePresenter: Exibe no terminal
 * - JsonPresenter: Retorna JSON para API REST
 * - HtmlPresenter: Renderiza página HTML
 * - MobilePresenter: Formata para app mobile
 * 
 * Use Cases não mudam, apenas o Presenter!
 */
public class ConsolePresenter {

    /**
     * Apresenta o resultado do cadastro de trabalho.
     * 
     * @param output Resultado do Use Case
     */
    public void apresentarCadastroTrabalho(CadastrarTrabalhoOutput output) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 RESULTADO: CADASTRAR TRABALHO");
        System.out.println("=".repeat(60));
        System.out.println(output.getMensagem());
        
        if (output.isSucesso()) {
            System.out.println("ID: " + output.getTrabalhoId());
            System.out.println("Título: " + output.getTitulo());
            System.out.println("Autor: " + output.getNomeAutor());
        }
        
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Apresenta o resultado da avaliação de trabalho.
     * 
     * @param output Resultado do Use Case
     */
    public void apresentarAvaliacaoTrabalho(AvaliarTrabalhoOutput output) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("⭐ RESULTADO: AVALIAR TRABALHO");
        System.out.println("=".repeat(60));
        System.out.println(output.getMensagem());
        
        if (output.isSucesso()) {
            System.out.println("Trabalho: " + output.getTituloTrabalho());
            System.out.println("Avaliador: " + output.getNomeAvaliador());
            System.out.println("Nota atribuída: " + String.format("%.1f", output.getNotaAtribuida()));
            System.out.println("Nova média: " + String.format("%.2f", output.getNovaMedia()));
            System.out.println("Total de avaliações: " + output.getQuantidadeAvaliacoes());
        }
        
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Apresenta o resultado da consulta de média.
     * 
     * @param output Resultado do Use Case
     */
    public void apresentarConsultaMedia(ConsultarMediaOutput output) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 RESULTADO: CONSULTAR MÉDIA");
        System.out.println("=".repeat(60));
        System.out.println(output.getMensagem());
        
        if (output.isSucesso()) {
            System.out.println("Trabalho: " + output.getTitulo());
            System.out.println("Autor: " + output.getNomeAutor());
            
            if (output.getQuantidadeAvaliacoes() > 0) {
                System.out.println("Média: " + String.format("%.2f", output.getMedia()));
                System.out.println("Avaliações: " + output.getQuantidadeAvaliacoes());
            } else {
                System.out.println("Ainda não possui avaliações.");
            }
        }
        
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Apresenta uma mensagem informativa genérica.
     * 
     * @param titulo Título da mensagem
     * @param mensagem Conteúdo da mensagem
     */
    public void apresentarMensagem(String titulo, String mensagem) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(titulo);
        System.out.println("=".repeat(60));
        System.out.println(mensagem);
        System.out.println("=".repeat(60) + "\n");
    }
}
