// Arquivo: src/adapters/input/AvaliacaoController.java
package adapters.input;

import adapters.output.AlunoRepositorioMemoria;
import adapters.output.TrabalhoRepositorioMemoria;
import application.GerenciadorAvaliacoes;
import domain.model.Aluno;
import domain.ports.input.IGerenciadorAvaliacoes;
import domain.ports.output.IAlunoRepositorio;
import domain.ports.output.ITrabalhoRepositorio;

/*
 * ============================================================
 * CAMADA: ADAPTERS - INPUT (Driving Adapter / Primary Adapter)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Esta classe é o PONTO DE ENTRADA da aplicação. Ela:
 *   1. Instancia os Driven Adapters (implementações dos repositórios)
 *   2. Injeta os Adapters no Application (GerenciadorAvaliacoes)
 *   3. Chama os casos de uso através da interface IGerenciadorAvaliacoes
 *   4. Exibe resultados ao usuário
 *
 * O QUE É UM "DRIVING ADAPTER" (ADAPTADOR DE ENTRADA)?
 *   É uma classe que DIRIGE/COMANDA a aplicação.
 *   Ela representa o MUNDO EXTERNO (usuário, HTTP, CLI, mensageria, etc)
 *   que quer usar as funcionalidades do sistema.
 *   
 *   "Driving" = Dirige/Comanda o hexágono (adapter chama núcleo).
 *
 * POR QUE ESTÁ EM ADAPTERS/INPUT?
 *   - INPUT porque é uma porta de ENTRADA (mundo externo → núcleo)
 *   - ADAPTER porque ADAPTA uma tecnologia específica (neste caso, console/terminal)
 *     para as interfaces que o Domain definiu
 *
 * ARQUITETURA HEXAGONAL - FLUXO COMPLETO:
 *   
 *   [AvaliacaoController] ← Você está aqui! (Driving Adapter)
 *         │
 *         │ 1. Instancia Driven Adapters (repositórios)
 *         │ 2. Injeta no Application via construtor
 *         │ 3. Chama métodos da Driving Port
 *         ↓
 *   [IGerenciadorAvaliacoes] (Driving Port - interface)
 *         ↑ implementa
 *         │
 *   [GerenciadorAvaliacoes] (Application - núcleo)
 *         │
 *         │ Depende de Driven Ports (interfaces)
 *         ↓
 *   [IAlunoRepositorio, ITrabalhoRepositorio] (Driven Ports - interfaces)
 *         ↑ implementam
 *         │
 *   [AlunoRepositorioMemoria, TrabalhoRepositorioMemoria] (Driven Adapters)
 *
 * O QUE MUDOU DA ARQUITETURA EM CAMADAS?
 *   
 *   🔴 ANTES (Arquitetura em Camadas):
 *   -----------------------------------
 *   public class AvaliacaoController {
 *       public static void main(String[] args) {
 *           // Application instanciava os repositórios internamente
 *           GerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes();
 *           
 *           // Controller só chamava métodos
 *           gerenciador.cadastrarTrabalho(...);
 *       }
 *   }
 *   
 *   PROBLEMA: Application tinha dependência FIXA nos repositórios concretos.
 *   Não dava pra trocar implementação ou passar mocks para testes.
 *   
 *   🟢 DEPOIS (Arquitetura Hexagonal):
 *   -----------------------------------
 *   public class AvaliacaoController {
 *       public static void main(String[] args) {
 *           // 1. Controller instancia os Driven Adapters
 *           IAlunoRepositorio alunoRepo = new AlunoRepositorioMemoria();
 *           ITrabalhoRepositorio trabalhoRepo = new TrabalhoRepositorioMemoria();
 *           
 *           // 2. Controller injeta os Adapters no Application
 *           IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(trabalhoRepo, alunoRepo);
 *           
 *           // 3. Controller chama a interface (Driving Port)
 *           gerenciador.cadastrarTrabalho(...);
 *       }
 *   }
 *   
 *   BENEFÍCIO: Controller é responsável por "montar" o sistema!
 *   - Para DEV: usa AlunoRepositorioMemoria (rápido)
 *   - Para PROD: usaria AlunoRepositorioJDBC (persistente)
 *   - Para TESTE: usaria mocks
 *   Application não precisa mudar!
 *
 * RESPONSABILIDADE DO DRIVING ADAPTER:
 *   O Controller é o "CONSTRUTOR" do sistema. Ele:
 *   
 *   1. Conhece TODAS as camadas (é o único que pode)
 *   2. Instancia implementações concretas (Adapters)
 *   3. Faz "Dependency Injection" manual (ou usa framework como Spring)
 *   4. Conecta Adapters ao núcleo
 *   5. Invoca casos de uso
 *   
 *   Isso é o padrão de projeto "MAIN-PARTITION":
 *   Main() é sujo (conhece tudo), mas TODO o resto é limpo (desacoplado).
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - JVM chama o método main() ao iniciar a aplicação
 *   - Em um sistema web, seria um framework como Spring Boot
 *   - Em uma API REST, seria um RestController
 *   - Em um CLI, seria um CommandLineRunner
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - TUDO! Controller é o único lugar onde é permitido conhecer:
 *     * Driven Adapters (AlunoRepositorioMemoria, TrabalhoRepositorioMemoria)
 *     * Application (GerenciadorAvaliacoes)
 *     * Driving Ports (IGerenciadorAvaliacoes)
 *     * Driven Ports (IAlunoRepositorio, ITrabalhoRepositorio)
 *     * Domain (Aluno, Trabalho, Nota)
 *   
 *   Isso NÃO viola Hexagonal porque Controller ESTÁ NA BORDA!
 *   O NÚCLEO (Domain + Application) continua limpo e independente.
 *
 * TROCAR DE CONSOLE PARA WEB (EXEMPLO):
 *   Para criar uma API REST, você criaria outro Driving Adapter:
 *   
 *   @RestController
 *   public class AvaliacaoRestController {
 *       private IGerenciadorAvaliacoes gerenciador;
 *       
 *       // Spring injeta automaticamente
 *       @Autowired
 *       public AvaliacaoRestController(IGerenciadorAvaliacoes gerenciador) {
 *           this.gerenciador = gerenciador;
 *       }
 *       
 *       @PostMapping("/trabalhos")
 *       public ResponseEntity<Void> cadastrar(@RequestBody TrabalhoDTO dto) {
 *           gerenciador.cadastrarTrabalho(dto.id, dto.titulo, dto.alunoId);
 *           return ResponseEntity.created(...).build();
 *       }
 *   }
 *   
 *   Application NÃO MUDA! GerenciadorAvaliacoes funciona com console, web, CLI, etc!
 * ============================================================
 */

/**
 * Controlador de avaliações - ponto de entrada da aplicação.
 * 
 * <p>Esta classe é um <b>Driving Adapter</b> (Adaptador de Entrada) na Arquitetura Hexagonal.
 * Ela representa o MUNDO EXTERNO (neste caso, um console/terminal) que quer usar
 * as funcionalidades do sistema.</p>
 * 
 * <h3>Responsabilidades do Driving Adapter:</h3>
 * <ol>
 *   <li><b>Instanciar Adapters:</b> Criar implementações concretas dos repositórios</li>
 *   <li><b>Injeção de Dependência:</b> Passar Adapters para Application via construtor</li>
 *   <li><b>Orquestrar Fluxo:</b> Chamar casos de uso na ordem correta</li>
 *   <li><b>Apresentar Resultados:</b> Mostrar saída ao usuário (console, web, etc)</li>
 * </ol>
 * 
 * <h3>Por que Controller pode importar TUDO?</h3>
 * <p>Controller está na <b>BORDA</b> da aplicação. É o ponto de "costura" que conecta
 * o núcleo limpo (Domain + Application) aos Adapters sujos (tecnologias específicas).
 * Isso é PERMITIDO porque:</p>
 * <ul>
 *   <li>✅ O NÚCLEO continua independente (não importa Controller)</li>
 *   <li>✅ Controller é descartável (pode criar outro Adapter para web, CLI, etc)</li>
 *   <li>✅ Padrão "Main-Partition": main() é sujo, mas isola a sujeira</li>
 * </ul>
 * 
 * <h3>Diferença de Arquitetura em Camadas:</h3>
 * <table border="1">
 *   <tr>
 *     <th>Aspecto</th>
 *     <th>Camadas (Antes)</th>
 *     <th>Hexagonal (Depois)</th>
 *   </tr>
 *   <tr>
 *     <td>Instanciar Repositórios</td>
 *     <td>❌ Application faz internamente</td>
 *     <td>✅ Controller injeta no Application</td>
 *   </tr>
 *   <tr>
 *     <td>Tipo dos Repositórios</td>
 *     <td>❌ Classes concretas fixas</td>
 *     <td>✅ Interfaces - Controller escolhe implementação</td>
 *   </tr>
 *   <tr>
 *     <td>Testabilidade</td>
 *     <td>❌ Difícil - Application acoplado</td>
 *     <td>✅ Fácil - injetar mocks</td>
 *   </tr>
 *   <tr>
 *     <td>Trocar Tecnologia</td>
 *     <td>❌ Requer alterar Application</td>
 *     <td>✅ Só mudar linha no Controller</td>
 *   </tr>
 * </table>
 * 
 * <h3>Exemplo de Troca de Implementação:</h3>
 * <pre>
 * // ==== DESENVOLVIMENTO (memória rápida) ====
 * IAlunoRepositorio alunoRepo = new AlunoRepositorioMemoria();
 * 
 * // ==== PRODUÇÃO (banco de dados persistente) ====
 * Connection conexao = DriverManager.getConnection("jdbc:postgresql://...");
 * IAlunoRepositorio alunoRepo = new AlunoRepositorioJDBC(conexao);
 * 
 * // ==== TESTES UNITÁRIOS (mock) ====
 * IAlunoRepositorio alunoRepo = mock(IAlunoRepositorio.class);
 * when(alunoRepo.buscarPorId("a1")).thenReturn(new Aluno("a1", "Mock"));
 * 
 * // ==== APPLICATION CONTINUA IGUAL! ====
 * IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(trabalhoRepo, alunoRepo);
 * </pre>
 * 
 * @author Sistema de Avaliações - Exemplo Didático
 * @version 2.0 - Arquitetura Hexagonal
 */
public class AvaliacaoController {
    
    /**
     * Método principal - ponto de entrada da aplicação Java.
     * 
     * <p><b>O QUE FAZ:</b> Demonstra o uso completo do sistema em Arquitetura Hexagonal.
     * Este método representa um cenário de uso real: cadastrar alunos, cadastrar trabalho,
     * receber avaliações, consultar média.</p>
     * 
     * <p><b>ESTRUTURA HEXAGONAL EM 3 PASSOS:</b></p>
     * <ol>
     *   <li><b>INSTANCIAR DRIVEN ADAPTERS:</b> Criar implementações concretas dos repositórios</li>
     *   <li><b>INJETAR NO APPLICATION:</b> Passar Adapters para GerenciadorAvaliacoes (inversão!)</li>
     *   <li><b>USAR DRIVING PORT:</b> Chamar casos de uso através da interface IGerenciadorAvaliacoes</li>
     * </ol>
     * 
     * <p><b>COMPARAÇÃO COM CAMADAS:</b></p>
     * <pre>
     * // === ANTES (Camadas) ===
     * GerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes();
     * // Repositórios instanciados DENTRO do GerenciadorAvaliacoes
     * // ❌ Não dá pra passar mocks
     * // ❌ Não dá pra trocar implementação
     * 
     * // === DEPOIS (Hexagonal) ===
     * IAlunoRepositorio alunoRepo = new AlunoRepositorioMemoria();
     * ITrabalhoRepositorio trabalhoRepo = new TrabalhoRepositorioMemoria();
     * IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(trabalhoRepo, alunoRepo);
     * // ✅ Repositórios injetados (Dependency Injection)
     * // ✅ Fácil trocar: new AlunoRepositorioJDBC()
     * // ✅ Fácil testar: mock(IAlunoRepositorio.class)
     * </pre>
     * 
     * <p><b>INVERSÃO DE DEPENDÊNCIA EM AÇÃO:</b><br>
     * Veja como o fluxo de dependências mudou:
     * <pre>
     * CAMADAS (Antes):
     *   AvaliacaoController → GerenciadorAvaliacoes → TrabalhoRepositorio
     *   [Presentation]      → [Application]         → [Infrastructure]
     *   ❌ Application depende de Infrastructure (acoplamento)
     * 
     * HEXAGONAL (Depois):
     *   AvaliacaoController → IGerenciadorAvaliacoes ← GerenciadorAvaliacoes
     *                                ↑                          ↓
     *                          (interface)                      ↓
     *                                                    ITrabalhoRepositorio
     *                                                            ↑
     *                                                  TrabalhoRepositorioMemoria
     *   
     *   ✅ Application depende de INTERFACES (abstrações), não de implementações
     *   ✅ Infrastructure IMPLEMENTA interfaces do Domain
     *   ✅ Setas de dependência INVERTIDAS!
     * </pre>
     * 
     * <p><b>EXEMPLO DE TESTE UNITÁRIO:</b><br>
     * Com Hexagonal, testar GerenciadorAvaliacoes é trivial:
     * <pre>
     * @Test
     * public void deveCalcularMediaCorretamente() {
     *     // 1. Criar mocks dos repositórios
     *     ITrabalhoRepositorio mockTrabalhoRepo = mock(ITrabalhoRepositorio.class);
     *     IAlunoRepositorio mockAlunoRepo = mock(IAlunoRepositorio.class);
     *     
     *     // 2. Configurar comportamento dos mocks
     *     Aluno aluno = new Aluno("a1", "Alice");
     *     Trabalho trabalho = new Trabalho("t1", "Título", aluno);
     *     when(mockAlunoRepo.buscarPorId("a1")).thenReturn(aluno);
     *     when(mockTrabalhoRepo.buscarPorId("t1")).thenReturn(trabalho);
     *     
     *     // 3. Injetar mocks no Application (Hexagonal facilita isso!)
     *     IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(mockTrabalhoRepo, mockAlunoRepo);
     *     
     *     // 4. Testar caso de uso SEM banco de dados real!
     *     gerenciador.avaliarTrabalho("n1", "t1", "a2", 8.5);
     *     double media = gerenciador.consultarMedia("t1");
     *     
     *     // 5. Verificar resultado
     *     assertEquals(8.5, media, 0.01);
     * }
     * </pre>
     * Na arquitetura em camadas, esse teste seria IMPOSSÍVEL ou muito mais difícil!
     * </p>
     * 
     * @param args Argumentos da linha de comando (não utilizados neste exemplo)
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║  SISTEMA DE AVALIAÇÃO DE TRABALHOS ACADÊMICOS                         ║");
        System.out.println("║  Arquitetura: HEXAGONAL (Ports & Adapters)                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ============================================================
        // PASSO 1: INSTANCIAR DRIVEN ADAPTERS (Implementações Concretas)
        // ============================================================
        
        System.out.println("🔧 [STEP 1] Instanciando Driven Adapters (Repositórios)...");
        System.out.println();
        
        /*
         * HEXAGONAL: Controller é responsável por instanciar os Adapters.
         * 
         * Aqui estamos usando implementações "Memoria" (HashMap).
         * Para produção, trocaríamos para:
         *   new AlunoRepositorioJDBC(conexao)
         *   new TrabalhoRepositorioJPA(entityManager)
         * 
         * E o Application NÃO precisaria mudar!
         */
        IAlunoRepositorio alunoRepo = new AlunoRepositorioMemoria();
        ITrabalhoRepositorio trabalhoRepo = new TrabalhoRepositorioMemoria();
        
        System.out.println("   ✅ AlunoRepositorio: Implementação em MEMÓRIA (HashMap)");
        System.out.println("   ✅ TrabalhoRepositorio: Implementação em MEMÓRIA (HashMap)");
        System.out.println("   💡 Para produção, trocar por: new AlunoRepositorioJDBC(conexao)");
        System.out.println();
        
        // ============================================================
        // PASSO 2: INJETAR ADAPTERS NO APPLICATION (Dependency Injection)
        // ============================================================
        
        System.out.println("💉 [STEP 2] Injetando Adapters no Application (Inversão de Dependência)...");
        System.out.println();
        
        /*
         * INVERSÃO DE DEPENDÊNCIA (DIP) em ação!
         * 
         * Application (GerenciadorAvaliacoes) NÃO conhece as classes concretas
         * AlunoRepositorioMemoria e TrabalhoRepositorioMemoria.
         * 
         * Application só conhece as INTERFACES:
         *   - IAlunoRepositorio
         *   - ITrabalhoRepositorio
         * 
         * Controller INJETA as implementações concretas.
         * Isso permite trocar implementações sem alterar Application!
         */
        IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(trabalhoRepo, alunoRepo);
        
        System.out.println("   ✅ GerenciadorAvaliacoes criado");
        System.out.println("   ✅ Repositórios INJETADOS via construtor");
        System.out.println("   🔄 Application depende de INTERFACES, não de classes concretas");
        System.out.println();
        
        // ============================================================
        // PASSO 3: USAR DRIVING PORT (Chamar Casos de Uso)
        // ============================================================
        
        System.out.println("🚀 [STEP 3] Executando Casos de Uso através da Driving Port...");
        System.out.println();
        
        System.out.println("═══════════════════════════════════════════════════════════════════════");
        System.out.println("📚 CENÁRIO: Sistema de avaliação peer-to-peer de trabalhos");
        System.out.println("═══════════════════════════════════════════════════════════════════════");
        System.out.println();
        
        try {
            // ---------------------------------------------------------------
            // CASO DE USO 1: Cadastrar Alunos
            // ---------------------------------------------------------------
            System.out.println("📝 Cadastrando alunos...");
            System.out.println("──────────────────────────────────────────────────────────────────");
            
            /*
             * Alunos são salvos diretamente no repositório.
             * Isso poderia ser um caso de uso separado (ex: CadastrarAlunoUseCase),
             * mas para simplicidade didática, fazemos aqui.
             */
            Aluno alice = new Aluno("a1", "Alice Silva");
            Aluno bob = new Aluno("a2", "Bob Santos");
            Aluno carol = new Aluno("a3", "Carol Oliveira");
            
            alunoRepo.salvar(alice);
            alunoRepo.salvar(bob);
            alunoRepo.salvar(carol);
            
            System.out.println("   ✅ 3 alunos cadastrados com sucesso!");
            System.out.println();
            
            // ---------------------------------------------------------------
            // CASO DE USO 2: Cadastrar Trabalho Acadêmico
            // ---------------------------------------------------------------
            System.out.println("📄 Cadastrando trabalho acadêmico...");
            System.out.println("──────────────────────────────────────────────────────────────────");
            
            /*
             * HEXAGONAL: Chamamos o método através da INTERFACE (IGerenciadorAvaliacoes).
             * Não sabemos (e não precisamos saber) qual implementação está rodando.
             * Poderia ser GerenciadorAvaliacoes, ou um proxy, ou um cache decorator, etc.
             */
            gerenciador.cadastrarTrabalho(
                "t1",
                "Arquitetura Hexagonal: Teoria e Prática",
                "a1"  // Alice é a autora
            );
            
            System.out.println();
            
            // ---------------------------------------------------------------
            // CASO DE USO 3: Avaliar Trabalho (múltiplas avaliações)
            // ---------------------------------------------------------------
            System.out.println("⭐ Registrando avaliações de colegas...");
            System.out.println("──────────────────────────────────────────────────────────────────");
            
            /*
             * Bob e Carol avaliam o trabalho de Alice.
             * Cada avaliação é uma nota de 0 a 10.
             * A entidade Nota valida automaticamente esse range.
             */
            gerenciador.avaliarTrabalho("n1", "t1", "a2", 9.0);  // Bob avalia: 9.0
            gerenciador.avaliarTrabalho("n2", "t1", "a3", 8.5);  // Carol avalia: 8.5
            
            System.out.println();
            
            // ---------------------------------------------------------------
            // CASO DE USO 4: Consultar Média de Avaliações
            // ---------------------------------------------------------------
            System.out.println("📊 Consultando média das avaliações...");
            System.out.println("──────────────────────────────────────────────────────────────────");
            
            /*
             * A média é calculada pela entidade Trabalho (Domain).
             * Application só ORQUESTRA (busca trabalho, chama calcularMedia, retorna).
             * Isso mantém a separação de responsabilidades clara.
             */
            double media = gerenciador.consultarMedia("t1");
            
            System.out.println();
            System.out.println("   📈 Média final: " + media + " (escala 0-10)");
            System.out.println("   💡 Cálculo: (9.0 + 8.5) / 2 = " + media);
            System.out.println();
            
            // ---------------------------------------------------------------
            // SUCESSO!
            // ---------------------------------------------------------------
            System.out.println("═══════════════════════════════════════════════════════════════════════");
            System.out.println("✅ TODOS OS CASOS DE USO EXECUTADOS COM SUCESSO!");
            System.out.println("═══════════════════════════════════════════════════════════════════════");
            System.out.println();
            
            // ---------------------------------------------------------------
            // DEMONSTRAÇÃO: Benefícios da Arquitetura Hexagonal
            // ---------------------------------------------------------------
            System.out.println("╔════════════════════════════════════════════════════════════════════════╗");
            System.out.println("║  💡 BENEFÍCIOS DA ARQUITETURA HEXAGONAL DEMONSTRADOS:                 ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("1️⃣  INVERSÃO DE DEPENDÊNCIA:");
            System.out.println("   ✅ Application depende de INTERFACES (IAlunoRepositorio, ITrabalhoRepositorio)");
            System.out.println("   ✅ Adapters IMPLEMENTAM essas interfaces");
            System.out.println("   ✅ Setas de dependência apontam para o NÚCLEO (Domain)");
            System.out.println();
            
            System.out.println("2️⃣  TESTABILIDADE:");
            System.out.println("   ✅ Fácil criar mocks das interfaces");
            System.out.println("   ✅ Injetar mocks no Application via construtor");
            System.out.println("   ✅ Testar casos de uso SEM banco de dados real");
            System.out.println();
            
            System.out.println("3️⃣  FLEXIBILIDADE:");
            System.out.println("   ✅ Trocar de HashMap para PostgreSQL:");
            System.out.println("      │ IAlunoRepositorio repo = new AlunoRepositorioJDBC(conexao);");
            System.out.println("      └─ Application NÃO precisa mudar!");
            System.out.println();
            
            System.out.println("4️⃣  MÚLTIPLOS ADAPTERS:");
            System.out.println("   ✅ Console (esta classe - Driving Adapter)");
            System.out.println("   ✅ API REST (futuro: AvaliacaoRestController)");
            System.out.println("   ✅ CLI (futuro: AvaliacaoCommandLine)");
            System.out.println("   └─ TODOS usam o MESMO Application! Sem duplicação de lógica.");
            System.out.println();
            
            System.out.println("5️⃣  INDEPENDÊNCIA DE FRAMEWORK:");
            System.out.println("   ✅ Domain não conhece Spring, Hibernate, Jakarta, etc.");
            System.out.println("   ✅ Application não conhece detalhes de infraestrutura");
            System.out.println("   ✅ Núcleo (Domain + Application) 100% portável");
            System.out.println();
            
            System.out.println("═══════════════════════════════════════════════════════════════════════");
            System.out.println("🎓 ARQUITETURA HEXAGONAL: Clean, Testável, Flexível!");
            System.out.println("═══════════════════════════════════════════════════════════════════════");
            
        } catch (Exception e) {
            // ---------------------------------------------------------------
            // TRATAMENTO DE ERROS
            // ---------------------------------------------------------------
            System.err.println("═══════════════════════════════════════════════════════════════════════");
            System.err.println("❌ ERRO DURANTE EXECUÇÃO");
            System.err.println("═══════════════════════════════════════════════════════════════════════");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println();
            System.err.println("Stack Trace:");
            e.printStackTrace();
            System.err.println("═══════════════════════════════════════════════════════════════════════");
        }
    }
    
    /*
     * ============================================================
     * OBSERVAÇÕES FINAIS SOBRE O DRIVING ADAPTER
     * ============================================================
     *
     * RESUMO DO QUE ESTA CLASSE REPRESENTA:
     * 
     * 1. É um DRIVING ADAPTER (Adaptador de Entrada)
     * 2. Representa o MUNDO EXTERNO (neste caso, console/terminal)
     * 3. INSTANCIA os Driven Adapters (repositórios)
     * 4. INJETA os Adapters no Application (Inversão de Controle)
     * 5. CHAMA casos de uso através da Driving Port (IGerenciadorAvaliacoes)
     * 
     * DIFERENÇA DE ARQUITETURA EM CAMADAS:
     * 
     * CAMADAS (Antes):
     *   - Controller chamava Application diretamente (classe concreta)
     *   - Application instanciava repositórios internamente
     *   - Acoplamento forte
     *   - Difícil testar e trocar implementações
     * 
     * HEXAGONAL (Depois):
     *   - Controller chama Application através de INTERFACE
     *   - Controller INJETA repositórios no Application
     *   - Acoplamento fraco (interfaces)
     *   - Fácil testar (injetar mocks) e trocar (injetar outra implementação)
     * 
     * PADRÃO "MAIN-PARTITION":
     *   O método main() é o ÚNICO lugar "sujo" que pode conhecer todas as camadas.
     *   TODO o resto do sistema é limpo, desacoplado, testável.
     *   Main() é descartável - podemos criar outro Adapter (REST, CLI, etc) sem
     *   alterar o núcleo.
     * 
     * PRÓXIMOS PASSOS (EVOLUÇÃO DO SISTEMA):
     * 
     * 1. Criar AvaliacaoRestController (Driving Adapter para HTTP):
     *    @RestController
     *    public class AvaliacaoRestController {
     *        private IGerenciadorAvaliacoes gerenciador;
     *        // ... endpoints REST ...
     *    }
     * 
     * 2. Criar AlunoRepositorioJDBC (Driven Adapter para PostgreSQL):
     *    public class AlunoRepositorioJDBC implements IAlunoRepositorio {
     *        private Connection conexao;
     *        // ... código SQL ...
     *    }
     * 
     * 3. Configurar Spring Boot para Dependency Injection automática:
     *    @Configuration
     *    public class AppConfig {
     *        @Bean
     *        public IAlunoRepositorio alunoRepo() {
     *            return new AlunoRepositorioJDBC(dataSource());
     *        }
     *        
     *        @Bean
     *        public IGerenciadorAvaliacoes gerenciador(IAlunoRepositorio alunoRepo,
     *                                                   ITrabalhoRepositorio trabalhoRepo) {
     *            return new GerenciadorAvaliacoes(trabalhoRepo, alunoRepo);
     *        }
     *    }
     * 
     * E O MELHOR: Application (GerenciadorAvaliacoes) e Domain (Aluno, Trabalho, Nota)
     * NÃO MUDAM EM NADA! Continuam puros, testáveis, independentes.
     * 
     * Isso é o PODER da Arquitetura Hexagonal! 🚀
     * ============================================================
     */
}
