// Arquivo: src/application/GerenciadorAvaliacoes.java
package application;

import domain.model.Aluno;
import domain.model.Nota;
import domain.model.Trabalho;
import domain.ports.input.IGerenciadorAvaliacoes;
import domain.ports.output.IAlunoRepositorio;
import domain.ports.output.ITrabalhoRepositorio;

/*
 * ============================================================
 * CAMADA: APPLICATION (Use Cases / Application Services)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Orquestra os casos de uso do sistema (cadastrar trabalho, avaliar, consultar média).
 *   Coordena entidades do Domain e operações de persistência.
 *   É o "maestro" que faz a aplicação funcionar.
 *
 * O QUE MUDOU DA ARQUITETURA EM CAMADAS?
 *   
 *   🔴 ANTES (Arquitetura em Camadas):
 *   -------------------------------------
 *   public class GerenciadorAvaliacoes {
 *       private TrabalhoRepositorio trabalhoRepo;  // ❌ CLASSE CONCRETA
 *       private AlunoRepositorio alunoRepo;        // ❌ CLASSE CONCRETA
 *       
 *       public GerenciadorAvaliacoes() {
 *           trabalhoRepo = new TrabalhoRepositorio();  // ❌ INSTANCIA DIRETAMENTE
 *           alunoRepo = new AlunoRepositorio();        // ❌ INSTANCIA DIRETAMENTE
 *       }
 *   }
 *   
 *   PROBLEMAS:
 *   - Application acoplada à infraestrutura (conhece classes concretas)
 *   - Difícil testar (não dá pra trocar o repositório por um mock)
 *   - Difícil trocar tecnologia (mudar de HashMap para SQL requer mudar Application)
 *   - Viola o Dependency Inversion Principle (DIP)
 *   
 *   🟢 DEPOIS (Arquitetura Hexagonal):
 *   -------------------------------------
 *   public class GerenciadorAvaliacoes implements IGerenciadorAvaliacoes {
 *       private ITrabalhoRepositorio trabalhoRepo;  // ✅ INTERFACE (abstração)
 *       private IAlunoRepositorio alunoRepo;        // ✅ INTERFACE (abstração)
 *       
 *       public GerenciadorAvaliacoes(ITrabalhoRepositorio trabalhoRepo, 
 *                                     IAlunoRepositorio alunoRepo) {
 *           this.trabalhoRepo = trabalhoRepo;  // ✅ RECEBE POR INJEÇÃO
 *           this.alunoRepo = alunoRepo;        // ✅ RECEBE POR INJEÇÃO
 *       }
 *   }
 *   
 *   BENEFÍCIOS:
 *   ✅ Application depende de ABSTRAÇÕES (interfaces), não de IMPLEMENTAÇÕES
 *   ✅ Fácil testar: basta passar mocks no construtor
 *   ✅ Fácil trocar: passar AlunoRepositorioJDBC em vez de AlunoRepositorioMemoria
 *   ✅ Respeita DIP: módulos de alto nível não dependem de módulos de baixo nível
 *   ✅ Núcleo hexagonal independente de infraestrutura
 *
 * HEXAGONAL ARCHITECTURE:
 *   Esta classe está no NÚCLEO do hexágono (Application layer).
 *   
 *   Estrutura:
 *   - IMPLEMENTA a Driving Port (IGerenciadorAvaliacoes)
 *   - DEPENDE das Driven Ports (IAlunoRepositorio, ITrabalhoRepositorio)
 *   - USA as entidades do Domain (Aluno, Trabalho, Nota)
 *   
 *   Fluxo de dependências (INVERSÃO!):
 *   
 *      [AvaliacaoController]  (Driving Adapter - externo)
 *               ↓ chama
 *      [IGerenciadorAvaliacoes]  (Driving Port - domain)
 *               ↑ implementa
 *      [GerenciadorAvaliacoes]  (Application - núcleo)
 *               ↓ usa
 *      [ITrabalhoRepositorio]  (Driven Port - domain)
 *               ↑ implementa
 *      [TrabalhoRepositorioMemoria]  (Driven Adapter - externo)
 *   
 *   A SETA DE DEPENDÊNCIA FOI INVERTIDA!
 *   Application não conhece Adapters. Adapters é que conhecem Application.
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - Driving Adapters (Controllers, APIs REST, CLI)
 *   - Mas sempre através da interface IGerenciadorAvaliacoes
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - Entidades do Domain (Aluno, Trabalho, Nota)
 *   - Interfaces de Output Ports (IAlunoRepositorio, ITrabalhoRepositorio)
 *   - NUNCA chama Adapters diretamente!
 *
 * INJEÇÃO DE DEPENDÊNCIA:
 *   As implementações concretas dos repositórios são INJETADAS pelo 
 *   Driving Adapter (Controller) quando instancia o GerenciadorAvaliacoes.
 *   
 *   Exemplo no Controller:
 *   IAlunoRepositorio alunoRepo = new AlunoRepositorioMemoria();
 *   ITrabalhoRepositorio trabalhoRepo = new TrabalhoRepositorioMemoria();
 *   IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(trabalhoRepo, alunoRepo);
 *
 * TESTABILIDADE:
 *   Agora é trivial criar testes unitários:
 *   
 *   // Mock para teste
 *   ITrabalhoRepositorio mockRepo = new ITrabalhoRepositorio() {
 *       private Map<String, Trabalho> map = new HashMap<>();
 *       public void salvar(Trabalho t) { map.put(t.getId(), t); }
 *       public Trabalho buscarPorId(String id) { return map.get(id); }
 *       public List<Trabalho> listarTodos() { return new ArrayList<>(map.values()); }
 *   };
 *   
 *   IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(mockRepo, mockAlunoRepo);
 *   // Testar sem banco de dados real!
 * ============================================================
 */

/**
 * Implementação dos casos de uso de avaliação de trabalhos acadêmicos.
 * 
 * <p>Esta classe é o coração da <b>Application Layer</b> na Arquitetura Hexagonal.
 * Ela orquestra operações entre entidades do Domain e portas de persistência,
 * sem conhecer detalhes de implementação (Adapters).</p>
 * 
 * <h3>Por que implementa IGerenciadorAvaliacoes?</h3>
 * <p>Para permitir que Driving Adapters (Controllers) dependam da abstração,
 * não desta implementação concreta. Isso facilita testes e permite múltiplas
 * implementações dos mesmos casos de uso.</p>
 * 
 * <h3>Por que depende de interfaces (IAlunoRepositorio, ITrabalhoRepositorio)?</h3>
 * <p>Isso é a INVERSÃO DE DEPENDÊNCIA em ação! Application (alto nível) não deve
 * depender de Infrastructure (baixo nível). Ambos devem depender de ABSTRAÇÕES.</p>
 * 
 * <ul>
 *   <li>✅ Facilitou testes: podemos injetar mocks</li>
 *   <li>✅ Facilitou manutenção: trocar HashMap por SQL é transparente</li>
 *   <li>✅ Respeitou SOLID: Dependency Inversion Principle</li>
 *   <li>✅ Núcleo hexagonal independente</li>
 * </ul>
 * 
 * @author Sistema de Avaliações - Exemplo Didático  
 * @version 2.0 - Arquitetura Hexagonal
 */
public class GerenciadorAvaliacoes implements IGerenciadorAvaliacoes {
    
    // ============================================================
    // ATRIBUTOS - DEPENDÊNCIAS INVERTIDAS
    // ============================================================
    
    /**
     * Repositório de trabalhos - INTERFACE, não implementação concreta!
     * 
     * <p><b>ANTES (Camadas):</b> {@code private TrabalhoRepositorio trabalhoRepo;}</p>
     * <p><b>DEPOIS (Hexagonal):</b> {@code private ITrabalhoRepositorio trabalhoRepo;}</p>
     * 
     * <p>Esta mudança pequena tem impacto ENORME:
     * - Application não sabe SE é HashMap, SQL, MongoDB, API externa...
     * - Application só sabe QUE EXISTE um repositório com os métodos definidos na interface
     * - Podemos trocar a implementação sem recompilar Application
     * - Podemos testar com mocks triviais</p>
     */
    private final ITrabalhoRepositorio trabalhoRepo;
    
    /**
     * Repositório de alunos - INTERFACE, não implementação concreta!
     * 
     * <p>Mesma lógica do trabalhoRepo. Application desconhece detalhes de infraestrutura.</p>
     */
    private final IAlunoRepositorio alunoRepo;
    
    // ============================================================
    // CONSTRUTOR - INJEÇÃO DE DEPENDÊNCIA
    // ============================================================
    
    /**
     * Construtor com Injeção de Dependência (Dependency Injection).
     * 
     * <p><b>O QUE MUDOU:</b></p>
     * <pre>
     * ANTES (Camadas):
     * public GerenciadorAvaliacoes() {
     *     trabalhoRepo = new TrabalhoRepositorio();  // ❌ Instancia diretamente
     *     alunoRepo = new AlunoRepositorio();        // ❌ Acoplamento forte
     * }
     * 
     * DEPOIS (Hexagonal):
     * public GerenciadorAvaliacoes(ITrabalhoRepositorio trabalhoRepo, 
     *                               IAlunoRepositorio alunoRepo) {
     *     this.trabalhoRepo = trabalhoRepo;  // ✅ Recebe pronto (injetado)
     *     this.alunoRepo = alunoRepo;        // ✅ Não sabe qual implementação
     * }
     * </pre>
     * 
     * <p><b>POR QUE É MELHOR?</b></p>
     * <ul>
     *   <li><b>Testabilidade:</b> Podemos passar mocks nos testes</li>
     *   <li><b>Flexibilidade:</b> Podemos trocar implementações em runtime</li>
     *   <li><b>Separação:</b> Application não conhece Infrastructure</li>
     *   <li><b>SOLID:</b> Dependency Inversion Principle e Open/Closed Principle</li>
     * </ul>
     * 
     * <p><b>HEXAGONAL:</b> O Driving Adapter (Controller) é responsável por 
     * instanciar os Driven Adapters (repositórios) e injetá-los aqui. Application
     * simplesmente RECEBE as dependências prontas.</p>
     * 
     * <p><b>EXEMPLO DE USO NO CONTROLLER:</b></p>
     * <pre>
     * // Controller cria os Adapters concretos
     * IAlunoRepositorio alunoRepo = new AlunoRepositorioMemoria();
     * ITrabalhoRepositorio trabalhoRepo = new TrabalhoRepositorioMemoria();
     * 
     * // Controller injeta os Adapters no Application
     * IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(trabalhoRepo, alunoRepo);
     * 
     * // Application não sabe que está usando implementações "Memoria"!
     * // Poderia ser JDBC, JPA, MongoDB... Application não se importa!
     * </pre>
     * 
     * @param trabalhoRepo Implementação da porta de saída para persistir trabalhos
     * @param alunoRepo Implementação da porta de saída para persistir alunos
     */
    public GerenciadorAvaliacoes(ITrabalhoRepositorio trabalhoRepo, IAlunoRepositorio alunoRepo) {
        this.trabalhoRepo = trabalhoRepo;
        this.alunoRepo = alunoRepo;
    }
    
    // ============================================================
    // CASOS DE USO (implementação dos métodos da Driving Port)
    // ============================================================
    
    /**
     * Caso de Uso: Cadastrar um novo trabalho acadêmico.
     * 
     * <p><b>O QUE FAZ:</b> Cria um trabalho vinculado a um aluno autor e o persiste.</p>
     * 
     * <p><b>REGRAS DE NEGÓCIO:</b></p>
     * <ul>
     *   <li>O aluno autor deve existir no sistema (validação)</li>
     *   <li>Trabalho deve ter título não vazio (validado pela entidade Trabalho)</li>
     * </ul>
     * 
     * <p><b>ORQUESTRAÇÃO (responsabilidade desta camada):</b></p>
     * <ol>
     *   <li>Buscar o aluno autor usando {@code alunoRepo.buscarPorId()}</li>
     *   <li>Validar que o aluno existe</li>
     *   <li>Criar entidade Trabalho (que faz auto-validação)</li>
     *   <li>Persistir o trabalho usando {@code trabalhoRepo.salvar()}</li>
     * </ol>
     * 
     * <p><b>POR QUE ESTÁ NA APPLICATION?</b><br>
     * Esta lógica de orquestração não pertence ao Domain (entidades são independentes)
     * nem aos Adapters (que só implementam operações técnicas de I/O).
     * É responsabilidade da Application coordenar entidades e portas.</p>
     * 
     * <p><b>HEXAGONAL:</b><br>
     * Este método é da Driving Port (entrada do hexágono). Ele USA Driven Ports
     * (saída do hexágono) para persistência. O hexágono orquestra, mas não implementa
     * detalhes técnicos.</p>
     * 
     * <p><b>DIFERENÇA DA ARQUITETURA EM CAMADAS:</b><br>
     * O CÓDIGO É IDÊNTICO! A diferença está nas DEPENDÊNCIAS:
     * - Antes: chamava {@code TrabalhoRepositorio.salvar()} (classe concreta)
     * - Depois: chama {@code ITrabalhoRepositorio.salvar()} (interface)
     * 
     * Mudança sutil no código, mas impacto arquitetural enorme!</p>
     * 
     * @param id Identificador único do trabalho
     * @param titulo Título do trabalho acadêmico
     * @param alunoId ID do aluno autor do trabalho
     * @throws IllegalArgumentException se o aluno não existir ou dados forem inválidos
     */
    @Override
    public void cadastrarTrabalho(String id, String titulo, String alunoId) {
        // 1. Buscar o aluno autor (usa Driven Port)
        Aluno autor = alunoRepo.buscarPorId(alunoId);
        
        // 2. Validar existência do aluno (regra de negócio)
        if (autor == null) {
            throw new IllegalArgumentException("Aluno com ID '" + alunoId + "' não encontrado.");
        }
        
        // 3. Criar entidade Trabalho (Domain - auto-validação)
        Trabalho trabalho = new Trabalho(id, titulo, autor);
        
        // 4. Persistir (usa Driven Port)
        trabalhoRepo.salvar(trabalho);
        
        System.out.println("✅ Trabalho cadastrado: " + trabalho.getTitulo() + 
                           " (Autor: " + autor.getNome() + ")");
    }
    
    /**
     * Caso de Uso: Avaliar um trabalho acadêmico.
     * 
     * <p><b>O QUE FAZ:</b> Registra uma nota dada por um avaliador a um trabalho.</p>
     * 
     * <p><b>REGRAS DE NEGÓCIO:</b></p>
     * <ul>
     *   <li>O trabalho deve existir</li>
     *   <li>O avaliador (aluno) deve existir</li>
     *   <li>A nota deve estar entre 0 e 10 (validada pela entidade Nota)</li>
     * </ul>
     * 
     * <p><b>ORQUESTRAÇÃO:</b></p>
     * <ol>
     *   <li>Buscar o trabalho a ser avaliado</li>
     *   <li>Buscar o aluno avaliador</li>
     *   <li>Validar existências</li>
     *   <li>Criar entidade Nota (auto-validação do valor 0-10)</li>
     *   <li>Adicionar a nota ao trabalho (método do Domain)</li>
     *   <li>Persistir o trabalho atualizado</li>
     * </ol>
     * 
     * <p><b>HEXAGONAL:</b><br>
     * Application orquestra chamadas para:
     * - Driven Ports (buscar/salvar dados)
     * - Entidades Domain (criar Nota, adicionar ao Trabalho)
     * 
     * Mas não conhece COMO os dados são persistidos (HashMap? SQL? NoSQL?).
     * Só conhece O QUE fazer através das interfaces.</p>
     * 
     * <p><b>IMPORTANTE:</b><br>
     * A lógica de CALCULAR A MÉDIA não está aqui! Está no Domain (Trabalho.calcularMedia).
     * Application só ORQUESTRA; Domain contém REGRAS DE NEGÓCIO puras.</p>
     * 
     * @param notaId Identificador único da nota
     * @param trabalhoId ID do trabalho sendo avaliado
     * @param avaliadorId ID do aluno que está avaliando
     * @param valor Valor numérico da nota (0 a 10)
     * @throws IllegalArgumentException se trabalho/avaliador não existir ou nota inválida
     */
    @Override
    public void avaliarTrabalho(String notaId, String trabalhoId, String avaliadorId, double valor) {
        // 1. Buscar trabalho (usa Driven Port)
        Trabalho trabalho = trabalhoRepo.buscarPorId(trabalhoId);
        if (trabalho == null) {
            throw new IllegalArgumentException("Trabalho com ID '" + trabalhoId + "' não encontrado.");
        }
        
        // 2. Buscar avaliador (usa Driven Port)
        Aluno avaliador = alunoRepo.buscarPorId(avaliadorId);
        if (avaliador == null) {
            throw new IllegalArgumentException("Avaliador com ID '" + avaliadorId + "' não encontrado.");
        }
        
        // 3. Criar nota (Domain - auto-validação de 0-10)
        Nota nota = new Nota(notaId, avaliador, valor);
        
        // 4. Adicionar nota ao trabalho (método do Domain)
        trabalho.adicionarNota(nota);
        
        // 5. Persistir trabalho atualizado (usa Driven Port)
        trabalhoRepo.salvar(trabalho);
        
        System.out.println("✅ Nota " + valor + " registrada por " + avaliador.getNome() + 
                           " no trabalho '" + trabalho.getTitulo() + "'");
    }
    
    /**
     * Caso de Uso: Consultar a média de avaliações de um trabalho.
     * 
     * <p><b>O QUE FAZ:</b> Retorna a média aritmética das notas recebidas por um trabalho.</p>
     * 
     * <p><b>ORQUESTRAÇÃO:</b></p>
     * <ol>
     *   <li>Buscar o trabalho</li>
     *   <li>Validar existência</li>
     *   <li>Delegar cálculo da média ao Domain (Trabalho.calcularMedia)</li>
     * </ol>
     * 
     * <p><b>POR QUE O CÁLCULO NÃO ESTÁ AQUI?</b><br>
     * Calcular média é uma REGRA DE NEGÓCIO pura, não depende de infraestrutura.
     * Logo, pertence ao DOMAIN (Trabalho.calcularMedia). Application só ORQUESTRA.</p>
     * 
     * <p><b>HEXAGONAL:</b><br>
     * Application:
     * - USA Driven Port para buscar dados (trabalhoRepo.buscarPorId)
     * - USA Domain para executar lógica de negócio (trabalho.calcularMedia)
     * - NÃO implementa lógica de negócio nem detalhes técnicos
     * 
     * Isso mantém a separação de responsabilidades clara.</p>
     * 
     * <p><b>ARQUITETURA EM CAMADAS vs HEXAGONAL:</b><br>
     * O código deste método é IDÊNTICO nas duas arquiteturas!
     * A diferença está na ESTRUTURA GERAL e nas DEPENDÊNCIAS (interfaces vs classes).</p>
     * 
     * @param trabalhoId ID do trabalho
     * @return A média das notas (0.0 se não houver notas)
     * @throws IllegalArgumentException se o trabalho não existir
     */
    @Override
    public double consultarMedia(String trabalhoId) {
        // 1. Buscar trabalho (usa Driven Port)
        Trabalho trabalho = trabalhoRepo.buscarPorId(trabalhoId);
        if (trabalho == null) {
            throw new IllegalArgumentException("Trabalho com ID '" + trabalhoId + "' não encontrado.");
        }
        
        // 2. Calcular média (delega ao Domain)
        double media = trabalho.calcularMedia();
        
        System.out.println("📊 Média do trabalho '" + trabalho.getTitulo() + "': " + media);
        
        return media;
    }
}
