// Arquivo: src/frameworks/Main.java
package frameworks;

import adapters.controllers.AvaliacaoController;
import adapters.gateways.AlunoGatewayMemoria;
import adapters.gateways.TrabalhoGatewayMemoria;
import adapters.presenters.ConsolePresenter;
import usecases.cadastrartrabalho.CadastrarTrabalhoUseCase;
import usecases.avaliartrabalho.AvaliarTrabalhoUseCase;
import usecases.consultarmedia.ConsultarMediaUseCase;
import usecases.gateways.IAlunoGateway;
import usecases.gateways.ITrabalhoGateway;

/*
 * ============================================================
 * CLEAN ARCHITECTURE - CAMADA: FRAMEWORKS & DRIVERS (Camada 4 - Externa)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Camada mais externa - frameworks, ferramentas, drivers.
 *   Main() é o "CONSTRUTOR" do sistema (Dependency Injection manual).
 *
 * POSIÇÃO NA CLEAN ARCHITECTURE:
 *   ○ Entities
 *   ○ Use Cases
 *   ○ Interface Adapters
 *   ● ← Você está aqui (Frameworks & Drivers - mais externo)
 *
 * POR QUE MAIN ESTÁ NA CAMADA EXTERNA?
 *   Main() conhece TODAS as outras camadas (é o único permitido).
 *   Mas as camadas internas NÃO conhecem Main.
 *   Main é DESCARTÁVEL - podemos substituir por Spring Boot Config,
 *   e as camadas internas não mudam!
 *
 * RESPONSABILIDADE DO MAIN:
 *   1. Instanciar Gateways concretos
 *   2. Instanciar Use Cases (injetando Gateways)
 *   3. Instanciar Presenters
 *   4. Instanciar Controllers (injetando Use Cases e Presenters)
 *   5. Iniciar a aplicação
 *
 * PADRÃO "MAIN-PARTITION":
 *   Main é "sujo" (conhece tudo), mas isola a sujeira.
 *   TODO o resto do código é limpo, testável, independente.
 *
 * DIFERENÇA DE HEXAGONAL:
 *   Em Hexagonal: AvaliacaoController tinha o main()
 *   Em Clean: Main separado na camada Frameworks & Drivers
 *   
 *   Clean é mais explícito: Main pertence à camada mais externa,
 *   não é um Adapter de entrada.
 *
 * INVERSÃO DE DEPENDÊNCIA EM AÇÃO:
 *   - Main conhece as implementações concretas (AlunoGatewayMemoria)
 *   - Mas injeta através de interfaces (IAlunoGateway)
 *   - Use Cases dependem de interfaces, não de implementações
 *   - Isso permite trocar implementações sem afetar Use Cases
 * ============================================================
 */

/**
 * 🎓 MAIN: PONTO DE ENTRADA E INJEÇÃO DE DEPENDÊNCIAS
 * 
 * Esta classe é responsável por:
 * - Ser o ponto de entrada da aplicação
 * - Instanciar todos os componentes
 * - Fazer a "composição" (Dependency Injection manual)
 * - Iniciar o fluxo da aplicação
 * 
 * POR QUE MAIN ESTÁ SEPARADO?
 * - Em Clean Architecture, Main é parte da camada mais externa
 * - É o único lugar que conhece TODAS as implementações concretas
 * - Todo o resto do código depende de abstrações (interfaces)
 * 
 * PADRÃO DE COMPOSIÇÃO:
 * - Main "monta" o sistema conectando as peças
 * - Cada camada depende apenas de abstrações da camada interna
 * - Main é o único que sabe quais são as implementações concretas
 * 
 * COMPARAÇÃO COM FRAMEWORKS:
 * - Aqui: Dependency Injection MANUAL (explícito, didático)
 * - Em produção: Spring, Guice, Dagger, etc (automático)
 * - O princípio é o mesmo, apenas a forma de fazer muda
 */
public class Main {
    
    /**
     * Ponto de entrada da aplicação.
     * 
     * FLUXO DE CONSTRUÇÃO (de dentro para fora):
     * 1. Camada 3: Instanciar Gateways (implementações concretas)
     * 2. Camada 2: Instanciar Use Cases (injetando interfaces dos Gateways)
     * 3. Camada 3: Instanciar Presenter
     * 4. Camada 3: Instanciar Controller (injetando Use Cases e Presenter)
     * 5. Iniciar aplicação (chamar Controller)
     * 
     * NOTA: Construímos de dentro para fora seguindo a Regra de Dependência!
     */
    public static void main(String[] args) {
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  🏛️  CLEAN ARCHITECTURE - SISTEMA DE AVALIAÇÃO          ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("📦 Iniciando composição do sistema...");
        System.out.println();
        
        // ============================================================
        // PASSO 1: INSTANCIAR GATEWAYS (Camada 3 - Interface Adapters)
        // ============================================================
        // Aqui escolhemos as implementações CONCRETAS
        // Poderíamos trocar por AlunoGatewayMySQL sem afetar os Use Cases!
        
        System.out.println("🔧 [1/5] Instanciando Gateways (Interface Adapters)...");
        IAlunoGateway alunoGateway = new AlunoGatewayMemoria();
        ITrabalhoGateway trabalhoGateway = new TrabalhoGatewayMemoria();
        System.out.println("   ✅ AlunoGatewayMemoria criado");
        System.out.println("   ✅ TrabalhoGatewayMemoria criado");
        System.out.println();
        
        // ============================================================
        // PASSO 2: INSTANCIAR USE CASES (Camada 2 - Use Cases)
        // ============================================================
        // Use Cases recebem INTERFACES (abstrações), não implementações!
        // Isso é Dependency Inversion Principle em ação!
        
        System.out.println("🔧 [2/5] Instanciando Use Cases (Application Business Rules)...");
        CadastrarTrabalhoUseCase cadastrarTrabalhoUseCase = 
            new CadastrarTrabalhoUseCase(trabalhoGateway, alunoGateway);
        System.out.println("   ✅ CadastrarTrabalhoUseCase criado");
        
        AvaliarTrabalhoUseCase avaliarTrabalhoUseCase = 
            new AvaliarTrabalhoUseCase(trabalhoGateway, alunoGateway);
        System.out.println("   ✅ AvaliarTrabalhoUseCase criado");
        
        ConsultarMediaUseCase consultarMediaUseCase = 
            new ConsultarMediaUseCase(trabalhoGateway);
        System.out.println("   ✅ ConsultarMediaUseCase criado");
        System.out.println();
        
        // ============================================================
        // PASSO 3: INSTANCIAR PRESENTER (Camada 3 - Interface Adapters)
        // ============================================================
        // Presenter formata as saídas dos Use Cases
        // Poderíamos ter JsonPresenter, HtmlPresenter, etc
        
        System.out.println("🔧 [3/5] Instanciando Presenter (Interface Adapters)...");
        ConsolePresenter presenter = new ConsolePresenter();
        System.out.println("   ✅ ConsolePresenter criado");
        System.out.println();
        
        // ============================================================
        // PASSO 4: INSTANCIAR CONTROLLER (Camada 3 - Interface Adapters)
        // ============================================================
        // Controller recebe Use Cases e Presenter já prontos
        // Ele orquestra o fluxo da aplicação
        
        System.out.println("🔧 [4/5] Instanciando Controller (Interface Adapters)...");
        AvaliacaoController controller = new AvaliacaoController(
            cadastrarTrabalhoUseCase,
            avaliarTrabalhoUseCase,
            consultarMediaUseCase,
            presenter,
            alunoGateway
        );
        System.out.println("   ✅ AvaliacaoController criado");
        System.out.println();
        
        // ============================================================
        // PASSO 5: EXECUTAR APLICAÇÃO
        // ============================================================
        // Agora que tudo está montado, iniciamos o fluxo
        
        System.out.println("🔧 [5/5] Iniciando aplicação...");
        System.out.println();
        System.out.println("═".repeat(60));
        System.out.println();
        
        controller.executar();
        
        // ============================================================
        // FINALIZAÇÃO
        // ============================================================
        
        System.out.println("═".repeat(60));
        System.out.println();
        System.out.println("✅ Aplicação finalizada com sucesso!");
        System.out.println();
        System.out.println("🎓 OBSERVAÇÕES SOBRE CLEAN ARCHITECTURE:");
        System.out.println("   • Main está na camada MAIS EXTERNA (Frameworks & Drivers)");
        System.out.println("   • Main CONHECE todas as implementações concretas");
        System.out.println("   • Use Cases DEPENDEM de abstrações (interfaces)");
        System.out.println("   • Entities NÃO DEPENDEM de nada (centro do círculo)");
        System.out.println("   • Dependências sempre apontam PARA DENTRO!");
        System.out.println();
        System.out.println("🔄 COMPARAÇÃO COM HEXAGONAL:");
        System.out.println("   Hexagonal: main() estava no Controller (Driving Adapter)");
        System.out.println("   Clean: main() separado em Frameworks & Drivers");
        System.out.println("   Vantagem: Fica claro que Main pertence à camada externa");
        System.out.println();
        System.out.println("🔌 PLUG & PLAY:");
        System.out.println("   • Trocar AlunoGatewayMemoria por AlunoGatewayMySQL?");
        System.out.println("   • Basta mudar aqui no Main! Use Cases não mudam!");
        System.out.println("   • Trocar ConsolePresenter por JsonPresenter?");
        System.out.println("   • Basta mudar aqui no Main! Use Cases não mudam!");
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║           FIM DA DEMONSTRAÇÃO - CLEAN ARCHITECTURE        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }
}
