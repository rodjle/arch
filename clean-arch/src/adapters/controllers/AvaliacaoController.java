// Arquivo: src/adapters/controllers/AvaliacaoController.java
package adapters.controllers;

import entities.Aluno;
import usecases.cadastrartrabalho.CadastrarTrabalhoInput;
import usecases.cadastrartrabalho.CadastrarTrabalhoOutput;
import usecases.cadastrartrabalho.CadastrarTrabalhoUseCase;
import usecases.avaliartrabalho.AvaliarTrabalhoInput;
import usecases.avaliartrabalho.AvaliarTrabalhoOutput;
import usecases.avaliartrabalho.AvaliarTrabalhoUseCase;
import usecases.consultarmedia.ConsultarMediaInput;
import usecases.consultarmedia.ConsultarMediaOutput;
import usecases.consultarmedia.ConsultarMediaUseCase;
import usecases.gateways.IAlunoGateway;
import adapters.presenters.ConsolePresenter;

/*
 * ============================================================
 * CLEAN ARCHITECTURE - CAMADA: INTERFACE ADAPTERS (Camada 3)
 * Controller
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Recebe requisições (entrada do usuário), converte em Inputs,
 *   chama Use Cases, recebe Outputs, e passa para Presenter.
 *
 * POSIÇÃO NA CLEAN ARCHITECTURE:
 *   ○ Entities
 *   ○ Use Cases
 *   ● ← Você está aqui (Interface Adapters - Controller)
 *   ○ Frameworks & Drivers
 *
 * TIPOS DE ADAPTERS:
 *   - Controllers: Recebem requisições, chamam Use Cases ← VOCÊ ESTÁ AQUI
 *   - Presenters: Formatam Outputs para visualização
 *   - Gateways: Implementam interfaces de persistência
 *
 * REGRA DE DEPENDÊNCIA:
 *   - PODE depender de: Use Cases, Entities
 *   - NÃO PODE depender de: Frameworks & Drivers (camada externa)
 *   - Porém, Frameworks & Drivers podem chamar o Controller
 *
 * DIFERENÇA DE HEXAGONAL:
 *   Em Hexagonal: AvaliacaoController (Driving Adapter) + tinha main()
 *   Em Clean: AvaliacaoController (Interface Adapter) + SEM main()
 *   
 *   Clean separa mais claramente:
 *   - Controller: Controla fluxo da aplicação (camada 3)
 *   - Main: Ponto de entrada e injeção (camada 4 - Frameworks)
 *   - Presenter: Formatação de saída (camada 3, mas separado)
 *
 * FLUXO DO CONTROLLER:
 *   1. Recebe entrada (console, HTTP, etc)
 *   2. Converte em Input DTO
 *   3. Chama Use Case apropriado
 *   4. Recebe Output DTO
 *   5. Passa para Presenter formatar
 * ============================================================
 */

/**
 * 🎓 CONTROLLER: AVALIAÇÃO
 * 
 * Responsável por controlar o fluxo da aplicação de avaliação de trabalhos.
 * 
 * RESPONSABILIDADE:
 * - Receber requisições do usuário
 * - Converter dados em Input DTOs
 * - Chamar os Use Cases apropriados
 * - Receber Outputs e passar para Presenter
 * 
 * O QUE O CONTROLLER NÃO FAZ:
 * - NÃO contém regras de negócio (estão nas Entities)
 * - NÃO contém lógica de aplicação (está nos Use Cases)
 * - NÃO formata saída (está no Presenter)
 * - NÃO tem main() (está em Frameworks/Main.java)
 * 
 * INJEÇÃO DE DEPENDÊNCIAS:
 * - Recebe Use Cases prontos (injetados)
 * - Não cria Use Cases (isso é feito no Main)
 * - Isso facilita testes (pode injetar mocks dos Use Cases)
 */
public class AvaliacaoController {
    
    private final CadastrarTrabalhoUseCase cadastrarTrabalhoUseCase;
    private final AvaliarTrabalhoUseCase avaliarTrabalhoUseCase;
    private final ConsultarMediaUseCase consultarMediaUseCase;
    private final ConsolePresenter presenter;
    private final IAlunoGateway alunoGateway;

    /**
     * Construtor com injeção de dependências.
     * 
     * INVERSÃO DE DEPENDÊNCIA:
     * - Recebe Use Cases já instanciados
     * - Recebe Presenter já instanciado
     * - Main.java é responsável por criar e injetar
     * 
     * @param cadastrarTrabalhoUseCase Use Case para cadastrar trabalhos
     * @param avaliarTrabalhoUseCase Use Case para avaliar trabalhos
     * @param consultarMediaUseCase Use Case para consultar médias
     * @param presenter Presenter para formatação de saída
     * @param alunoGateway Gateway para cadastrar alunos
     */
    public AvaliacaoController(CadastrarTrabalhoUseCase cadastrarTrabalhoUseCase,
                               AvaliarTrabalhoUseCase avaliarTrabalhoUseCase,
                               ConsultarMediaUseCase consultarMediaUseCase,
                               ConsolePresenter presenter,
                               IAlunoGateway alunoGateway) {
        this.cadastrarTrabalhoUseCase = cadastrarTrabalhoUseCase;
        this.avaliarTrabalhoUseCase = avaliarTrabalhoUseCase;
        this.consultarMediaUseCase = consultarMediaUseCase;
        this.presenter = presenter;
        this.alunoGateway = alunoGateway;
    }

    /**
     * Executa o fluxo principal da aplicação.
     * 
     * FLUXO DEMONSTRATIVO:
     * 1. Cadastra alguns alunos
     * 2. Cadastra trabalhos
     * 3. Alunos avaliam trabalhos
     * 4. Consulta médias dos trabalhos
     */
    public void executar() {
        presenter.apresentarMensagem(
            "🏛️ SISTEMA DE AVALIAÇÃO - CLEAN ARCHITECTURE",
            "Demonstração de Clean Architecture com Use Cases separados"
        );

        // PASSO 1: Cadastrar alunos
        cadastrarAlunos();

        // PASSO 2: Cadastrar trabalhos
        cadastrarTrabalhos();

        // PASSO 3: Avaliar trabalhos
        avaliarTrabalhos();

        // PASSO 4: Consultar médias
        consultarMedias();
    }

    /**
     * Cadastra alunos de exemplo.
     */
    private void cadastrarAlunos() {
        presenter.apresentarMensagem("👥 CADASTRANDO ALUNOS", "Preparando alunos para o sistema...");
        
        // Criando alunos diretamente (poderia ter um Use Case próprio)
        Aluno aluno1 = new Aluno("A001", "João Silva");
        Aluno aluno2 = new Aluno("A002", "Maria Santos");
        Aluno aluno3 = new Aluno("A003", "Pedro Oliveira");
        
        alunoGateway.salvar(aluno1);
        alunoGateway.salvar(aluno2);
        alunoGateway.salvar(aluno3);
        
        System.out.println("✅ Alunos cadastrados: João Silva, Maria Santos, Pedro Oliveira\n");
    }

    /**
     * Cadastra trabalhos de exemplo.
     * 
     * FLUXO:
     * 1. Cria Input DTO com os dados
     * 2. Chama Use Case
     * 3. Recebe Output DTO
     * 4. Passa para Presenter formatar
     */
    private void cadastrarTrabalhos() {
        // Trabalho 1: João
        CadastrarTrabalhoInput input1 = new CadastrarTrabalhoInput(
            "T001",
            "Clean Architecture na Prática",
            "A001"
        );
        CadastrarTrabalhoOutput output1 = cadastrarTrabalhoUseCase.executar(input1);
        presenter.apresentarCadastroTrabalho(output1);

        // Trabalho 2: Maria
        CadastrarTrabalhoInput input2 = new CadastrarTrabalhoInput(
            "T002",
            "Padrões de Projeto com Java",
            "A002"
        );
        CadastrarTrabalhoOutput output2 = cadastrarTrabalhoUseCase.executar(input2);
        presenter.apresentarCadastroTrabalho(output2);
    }

    /**
     * Avalia os trabalhos cadastrados.
     * 
     * FLUXO:
     * 1. Cria Input DTO com os dados da avaliação
     * 2. Chama Use Case de avaliar
     * 3. Recebe Output DTO
     * 4. Passa para Presenter formatar
     */
    private void avaliarTrabalhos() {
        // Maria avalia trabalho de João
        AvaliarTrabalhoInput input1 = new AvaliarTrabalhoInput("T001", "A002", 9.5);
        AvaliarTrabalhoOutput output1 = avaliarTrabalhoUseCase.executar(input1);
        presenter.apresentarAvaliacaoTrabalho(output1);

        // Pedro avalia trabalho de João
        AvaliarTrabalhoInput input2 = new AvaliarTrabalhoInput("T001", "A003", 8.0);
        AvaliarTrabalhoOutput output2 = avaliarTrabalhoUseCase.executar(input2);
        presenter.apresentarAvaliacaoTrabalho(output2);

        // João avalia trabalho de Maria
        AvaliarTrabalhoInput input3 = new AvaliarTrabalhoInput("T002", "A001", 10.0);
        AvaliarTrabalhoOutput output3 = avaliarTrabalhoUseCase.executar(input3);
        presenter.apresentarAvaliacaoTrabalho(output3);

        // Pedro avalia trabalho de Maria
        AvaliarTrabalhoInput input4 = new AvaliarTrabalhoInput("T002", "A003", 9.0);
        AvaliarTrabalhoOutput output4 = avaliarTrabalhoUseCase.executar(input4);
        presenter.apresentarAvaliacaoTrabalho(output4);
    }

    /**
     * Consulta as médias dos trabalhos.
     * 
     * FLUXO:
     * 1. Cria Input DTO com o ID do trabalho
     * 2. Chama Use Case de consultar média
     * 3. Recebe Output DTO
     * 4. Passa para Presenter formatar
     */
    private void consultarMedias() {
        // Consulta média do trabalho de João
        ConsultarMediaInput input1 = new ConsultarMediaInput("T001");
        ConsultarMediaOutput output1 = consultarMediaUseCase.executar(input1);
        presenter.apresentarConsultaMedia(output1);

        // Consulta média do trabalho de Maria
        ConsultarMediaInput input2 = new ConsultarMediaInput("T002");
        ConsultarMediaOutput output2 = consultarMediaUseCase.executar(input2);
        presenter.apresentarConsultaMedia(output2);
    }
}
