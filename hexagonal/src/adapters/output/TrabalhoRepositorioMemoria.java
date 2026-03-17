// Arquivo: src/adapters/output/TrabalhoRepositorioMemoria.java
package adapters.output;

import domain.model.Trabalho;
import domain.ports.output.ITrabalhoRepositorio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * ============================================================
 * CAMADA: ADAPTERS - OUTPUT (Driven Adapter)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   IMPLEMENTA a interface ITrabalhoRepositorio (Driven Port) usando
 *   HashMap em memória. Fornece persistência simples para trabalhos acadêmicos.
 *
 * O QUE É UM DRIVEN ADAPTER?
 *   É uma implementação CONCRETA de uma interface (Port) definida no Domain.
 *   O núcleo (Application) DEFINE o que precisa (interface),
 *   e o Adapter FORNECE a implementação (HashMap, SQL, NoSQL, API, etc).
 *   
 *   "Driven" = Dirigido pelo núcleo (núcleo chama adapter, não o contrário).
 *
 * ARQUITETURA HEXAGONAL - INVERSÃO DE DEPENDÊNCIA:
 *   
 *   PROBLEMA NA ARQUITETURA EM CAMADAS:
 *   ────────────────────────────────────
 *   
 *   [Application Layer]
 *         │
 *         │ depende diretamente de ↓
 *         ↓
 *   [Infrastructure Layer: TrabalhoRepositorio]
 *   
 *   ❌ PROBLEMAS:
 *   - Application (alto nível) depende de Infrastructure (baixo nível)
 *   - Viola Dependency Inversion Principle
 *   - Difícil de testar (não dá pra trocar repositório por mock facilmente)
 *   - Difícil de mudar tecnologia (trocar HashMap por SQL requer alterar Application)
 *   
 *   SOLUÇÃO COM HEXAGONAL:
 *   ──────────────────────
 *   
 *   [Application Layer]
 *         │
 *         │ depende de interface ↓
 *         ↓
 *   [ITrabalhoRepositorio] ← Interface no DOMAIN
 *         ↑
 *         │ implementa
 *         │
 *   [TrabalhoRepositorioMemoria] ← Esta classe (ADAPTER)
 *   
 *   ✅ SOLUÇÕES:
 *   - Application depende de ABSTRAÇÃO (interface), não de IMPLEMENTAÇÃO
 *   - Respeita Dependency Inversion Principle (DIP)
 *   - Fácil de testar: passar mock da interface no construtor do Application
 *   - Fácil de trocar: substituir TrabalhoRepositorioMemoria por TrabalhoRepositorioJDBC
 *     sem alterar UMA LINHA de código no Application!
 *
 * FLEXIBILIDADE - MÚLTIPLAS IMPLEMENTAÇÕES:
 *   
 *   Podemos ter várias implementações de ITrabalhoRepositorio:
 *   
 *   1. TrabalhoRepositorioMemoria (esta classe)
 *      - Usa HashMap
 *      - Rápido, mas perde dados ao reiniciar
 *      - Ideal para testes e demos
 *   
 *   2. TrabalhoRepositorioJDBC (implementação futura)
 *      - Usa SQL com JDBC
 *      - Persistente, mas mais lento
 *      - Ideal para produção
 *   
 *   3. TrabalhoRepositorioJPA (implementação futura)
 *      - Usa Hibernate/JPA (ORM)
 *      - Abstração sobre SQL
 *      - Ideal para aplicações enterprise
 *   
 *   4. TrabalhoRepositorioMongo (implementação futura)
 *      - Usa MongoDB (NoSQL)
 *      - Escalável, schema-less
 *      - Ideal para big data
 *   
 *   5. Mock para testes:
 *      - Implementação fake que retorna dados pré-definidos
 *      - Ideal para testes unitários
 *   
 *   O MAIS IMPORTANTE: Application NÃO muda para usar qualquer uma dessas!
 *   Só precisamos trocar qual implementação injetamos no construtor.
 *
 * COMO TROCAR IMPLEMENTAÇÃO (EXEMPLO PRÁTICO):
 *   
 *   // No AvaliacaoController (Driving Adapter):
 *   
 *   // ===== MEMÓRIA (desenvolvimento/testes) =====
 *   ITrabalhoRepositorio repo = new TrabalhoRepositorioMemoria();
 *   
 *   // ===== SQL (produção) =====
 *   Connection conexao = DriverManager.getConnection("jdbc:postgresql://...");
 *   ITrabalhoRepositorio repo = new TrabalhoRepositorioJDBC(conexao);
 *   
 *   // ===== MOCK (testes unitários) =====
 *   ITrabalhoRepositorio repo = mock(ITrabalhoRepositorio.class);
 *   when(repo.buscarPorId("t1")).thenReturn(new Trabalho(...));
 *   
 *   // ===== APPLICATION NÃO MUDA! =====
 *   IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(repo, alunoRepo);
 *   gerenciador.cadastrarTrabalho("t1", "Título", "a1");  // Funciona com qualquer implementação!
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - NINGUÉM chama diretamente esta classe!
 *   - Application chama IAlunoRepositorio (interface)
 *   - JVM resolve qual implementação usar (polimorfismo)
 *   - Controller é quem instancia e injeta esta classe no Application
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - HashMap (estrutura de dados Java)
 *   - Entidade Trabalho (para retornar objetos)
 *   - NÃO chama Application, Domain (exceto entidades), ou outros Adapters
 * ============================================================
 */

/**
 * Implementação em memória do repositório de trabalhos acadêmicos.
 * 
 * <p>Esta classe é um <b>Driven Adapter</b> (Adaptador de Saída) na Arquitetura Hexagonal.
 * Ela IMPLEMENTA a interface {@link ITrabalhoRepositorio} (Driven Port) definida no Domain,
 * fornecendo persistência em memória usando HashMap para fins didáticos.</p>
 * 
 * <h3>Por que Hexagonal é melhor que Camadas?</h3>
 * <table border="1">
 *   <tr>
 *     <th>Aspecto</th>
 *     <th>Arquitetura em Camadas</th>
 *     <th>Arquitetura Hexagonal</th>
 *   </tr>
 *   <tr>
 *     <td>Dependência</td>
 *     <td>Application → TrabalhoRepositorio (classe)</td>
 *     <td>Application → ITrabalhoRepositorio (interface)</td>
 *   </tr>
 *   <tr>
 *     <td>Acoplamento</td>
 *     <td>❌ Alto (Application conhece implementação)</td>
 *     <td>✅ Baixo (Application conhece só interface)</td>
 *   </tr>
 *   <tr>
 *     <td>Testabilidade</td>
 *     <td>❌ Difícil (não dá pra passar mock)</td>
 *     <td>✅ Fácil (injeta mock no construtor)</td>
 *   </tr>
 *   <tr>
 *     <td>Trocar Tecnologia</td>
 *     <td>❌ Requer alterar Application</td>
 *     <td>✅ Só trocar qual Adapter instanciar</td>
 *   </tr>
 *   <tr>
 *     <td>SOLID (DIP)</td>
 *     <td>❌ Viola Dependency Inversion</td>
 *     <td>✅ Respeita Dependency Inversion</td>
 *   </tr>
 * </table>
 * 
 * <h3>Exemplo de Como Usar em Produção:</h3>
 * <pre>
 * // 1. Criar implementação SQL:
 * public class TrabalhoRepositorioJDBC implements ITrabalhoRepositorio {
 *     private Connection conexao;
 *     
 *     public void salvar(Trabalho t) {
 *         String sql = "INSERT INTO trabalhos (...) VALUES (...)";
 *         // ... código JDBC ...
 *     }
 *     // ... outros métodos ...
 * }
 * 
 * // 2. No Controller, trocar implementação:
 * // ANTES:
 * ITrabalhoRepositorio repo = new TrabalhoRepositorioMemoria();
 * 
 * // DEPOIS:
 * Connection conexao = obterConexaoPostgreSQL();
 * ITrabalhoRepositorio repo = new TrabalhoRepositorioJDBC(conexao);
 * 
 * // 3. Application continua EXATAMENTE IGUAL:
 * IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(repo, alunoRepo);
 * gerenciador.cadastrarTrabalho(...);  // Funciona!
 * </pre>
 * 
 * @author Sistema de Avaliações - Exemplo Didático
 * @version 2.0 - Arquitetura Hexagonal
 */
public class TrabalhoRepositorioMemoria implements ITrabalhoRepositorio {
    
    // ============================================================
    // ATRIBUTO - ARMAZENAMENTO EM MEMÓRIA
    // ============================================================
    
    /**
     * Armazena trabalhos acadêmicos em memória usando HashMap.
     * 
     * <p><b>ESTRUTURA:</b> Chave = ID do trabalho, Valor = objeto Trabalho completo.</p>
     * 
     * <p><b>COMPLEXIDADE:</b></p>
     * <ul>
     *   <li>Inserção (salvar): O(1) amortizado</li>
     *   <li>Busca (buscarPorId): O(1) amortizado</li>
     *   <li>Listagem (listarTodos): O(n) onde n = quantidade de trabalhos</li>
     * </ul>
     * 
     * <p><b>VANTAGENS DO HASHMAP (para este exemplo didático):</b></p>
     * <ul>
     *   <li>✅ Simples de usar (não precisa configurar banco)</li>
     *   <li>✅ Rápido (busca em tempo constante)</li>
     *   <li>✅ Não requer dependências externas</li>
     *   <li>✅ Ideal para demonstrar conceitos sem complexidade extra</li>
     * </ul>
     * 
     * <p><b>DESVANTAGENS (POR QUE NÃO USAR EM PRODUÇÃO):</b></p>
     * <ul>
     *   <li>❌ Dados perdidos ao reiniciar aplicação (volátil)</li>
     *   <li>❌ Não suporta transações (ACID)</li>
     *   <li>❌ Não escalável (tudo na memória RAM)</li>
     *   <li>❌ Não permite consultas complexas (JOIN, GROUP BY, etc)</li>
     *   <li>❌ Não thread-safe sem sincronização adicional</li>
     * </ul>
     * 
     * <p><b>EM PRODUÇÃO, USE:</b></p>
     * <ul>
     *   <li>PostgreSQL / MySQL (banco relacional)</li>
     *   <li>MongoDB / Cassandra (banco NoSQL)</li>
     *   <li>Redis (cache distribuído)</li>
     * </ul>
     * 
     * <p><b>HEXAGONAL:</b> Este HashMap é um DETALHE DE IMPLEMENTAÇÃO escondido
     * dentro do Adapter. Application não sabe disso! Application só conhece a interface
     * ITrabalhoRepositorio com métodos abstratos salvar/buscar/listar.
     * Podemos trocar HashMap por SQL sem Application perceber.</p>
     */
    private final Map<String, Trabalho> trabalhos = new HashMap<>();
    
    // ============================================================
    // IMPLEMENTAÇÃO DOS MÉTODOS DA INTERFACE (Driven Port)
    // ============================================================
    
    /**
     * Salva um trabalho no repositório em memória.
     * 
     * <p><b>IMPLEMENTAÇÃO ATUAL (HashMap):</b></p>
     * <ul>
     *   <li>Se trabalho novo: adiciona ao HashMap</li>
     *   <li>Se trabalho existente (mesmo ID): atualiza (sobrescreve)</li>
     * </ul>
     * 
     * <p><b>COMPORTAMENTO EM DIFERENTES IMPLEMENTAÇÕES:</b></p>
     * <pre>
     * // === ESTA IMPLEMENTAÇÃO (Memória) ===
     * trabalhos.put(trabalho.getId(), trabalho);
     * 
     * // === IMPLEMENTAÇÃO JDBC (SQL) ===
     * String sql = "INSERT INTO trabalhos (id, titulo, autor_id) VALUES (?, ?, ?) "
     *            + "ON CONFLICT (id) DO UPDATE SET titulo = ?, autor_id = ?";
     * // ... prepared statement ...
     * 
     * // === IMPLEMENTAÇÃO JPA (Hibernate) ===
     * entityManager.merge(trabalho);  // merge = insert or update
     * 
     * // === IMPLEMENTAÇÃO MONGODB ===
     * collection.replaceOne(eq("_id", trabalho.getId()), trabalho, 
     *                       new ReplaceOptions().upsert(true));
     * </pre>
     * 
     * <p><b>IMPORTANTE:</b> Todas essas implementações DIFERENTES têm a MESMA ASSINATURA
     * (mesmo método da interface). Application chama {@code salvar(trabalho)} e funciona
     * com qualquer uma!</p>
     * 
     * <p><b>HEXAGONAL:</b> Método da Driven Port. Application chama este método através
     * da interface, sem saber qual implementação está rodando. Isso é POLIMORFISMO aplicado
     * à arquitetura!</p>
     * 
     * @param trabalho O trabalho acadêmico a ser salvo (não pode ser null)
     * @throws IllegalArgumentException se trabalho for null
     */
    @Override
    public void salvar(Trabalho trabalho) {
        if (trabalho == null) {
            throw new IllegalArgumentException("Trabalho não pode ser null");
        }
        
        // Implementação simples: HashMap.put()
        // Em produção: seria INSERT/UPDATE no banco de dados
        trabalhos.put(trabalho.getId(), trabalho);
        
        System.out.println("💾 [TrabalhoRepositorioMemoria] Trabalho salvo: " + 
                           trabalho.getId() + " - \"" + trabalho.getTitulo() + "\"");
    }
    
    /**
     * Busca um trabalho pelo seu identificador único.
     * 
     * <p><b>IMPLEMENTAÇÃO ATUAL:</b> Usa {@code HashMap.get()} - complexidade O(1).</p>
     * 
     * <p><b>COMPARAÇÃO DE IMPLEMENTAÇÕES:</b></p>
     * <table border="1">
     *   <tr>
     *     <th>Implementação</th>
     *     <th>Código</th>
     *     <th>Complexidade</th>
     *   </tr>
     *   <tr>
     *     <td>Memória (esta)</td>
     *     <td>{@code trabalhos.get(id)}</td>
     *     <td>O(1) - hash lookup</td>
     *   </tr>
     *   <tr>
     *     <td>JDBC (SQL)</td>
     *     <td>{@code SELECT * FROM trabalhos WHERE id = ?}</td>
     *     <td>O(log n) com índice / O(n) sem índice</td>
     *   </tr>
     *   <tr>
     *     <td>MongoDB</td>
     *     <td>{@code collection.find(eq("_id", id)).first()}</td>
     *     <td>O(1) - busca por _id é indexada</td>
     *   </tr>
     *   <tr>
     *     <td>Cache (Redis)</td>
     *     <td>{@code redis.get("trabalho:" + id)}</td>
     *     <td>O(1) - busca em memória distribuída</td>
     *   </tr>
     * </table>
     * 
     * <p><b>HEXAGONAL:</b> Application não se preocupa com complexidade ou estratégia.
     * Só chama {@code buscarPorId(id)} e recebe o resultado. O Adapter escolhe a
     * melhor estratégia para o contexto (memória, banco, cache, etc).</p>
     * 
     * <p><b>CONTRATO:</b> Independente da implementação, o comportamento é o mesmo:
     * <ul>
     *   <li>Se encontrado: retorna {@code Trabalho}</li>
     *   <li>Se não encontrado: retorna {@code null}</li>
     * </ul>
     * Isso garante que Application funcione com qualquer implementação!</p>
     * 
     * @param id O identificador único do trabalho
     * @return O trabalho encontrado, ou null se não existir
     */
    @Override
    public Trabalho buscarPorId(String id) {
        // Implementação simples: HashMap.get()
        // Em produção: seria SELECT com WHERE no banco de dados
        Trabalho trabalho = trabalhos.get(id);
        
        if (trabalho != null) {
            System.out.println("🔍 [TrabalhoRepositorioMemoria] Trabalho encontrado: " + 
                               trabalho.getId() + " - \"" + trabalho.getTitulo() + "\"");
        } else {
            System.out.println("🔍 [TrabalhoRepositorioMemoria] Trabalho NÃO encontrado: " + id);
        }
        
        return trabalho;
    }
    
    /**
     * Lista todos os trabalhos cadastrados no sistema.
     * 
     * <p><b>IMPLEMENTAÇÃO ATUAL:</b> Retorna lista com todos os valores do HashMap.</p>
     * 
     * <p><b>POR QUE {@code new ArrayList<>(trabalhos.values())}?</b></p>
     * <ol>
     *   <li><b>Desacoplamento:</b> Quem recebe a lista pode modifica-la sem afetar nosso HashMap</li>
     *   <li><b>Encapsulamento:</b> Não expor estrutura interna (HashMap) para fora do Adapter</li>
     *   <li><b>Segurança:</b> Evitar que código externo altere nosso armazenamento</li>
     * </ol>
     * 
     * <p><b>IMPLEMENTAÇÕES EM PRODUÇÃO:</b></p>
     * <pre>
     * // === MEMÓRIA (esta implementação) ===
     * return new ArrayList<>(trabalhos.values());  // Retorna tudo
     * 
     * // === SQL COM PAGINAÇÃO ===
     * String sql = "SELECT * FROM trabalhos LIMIT ? OFFSET ?";
     * // Retorna apenas uma página (ex: 20 registros por vez)
     * 
     * // === MONGODB COM FILTRO ===
     * return collection.find().limit(100).into(new ArrayList<>());
     * 
     * // === COM CACHE (ESTRATÉGIA HÍBRIDA) ===
     * List<Trabalho> cached = cache.get("trabalhos:todos");
     * if (cached != null) return cached;
     * List<Trabalho> fromDB = database.listarTodos();
     * cache.put("trabalhos:todos", fromDB, 5 * 60);  // Cache 5 min
     * return fromDB;
     * </pre>
     * 
     * <p><b>NOTA SOBRE ESCALABILIDADE:</b><br>
     * Este método retorna TODOS os trabalhos. Isso funciona para exemplos didáticos
     * (dezenas de registros), mas não escala para produção (milhões de registros).
     * Em produção, você adicionaria:
     * <ul>
     *   <li>Paginação: {@code listarTodos(int pagina, int tamanhoPagina)}</li>
     *   <li>Filtros: {@code listarPorAutor(String alunoId)}</li>
     *   <li>Ordenação: {@code listarTodos(String ordenarPor, boolean crescente)}</li>
     * </ul>
     * 
     * MAS... essas melhorias seriam ADIÇÕES à interface ITrabalhoRepositorio.
     * Application continuaria funcionando com a versão básica!</p>
     * 
     * <p><b>HEXAGONAL:</b> Application chama {@code listarTodos()} e recebe uma lista.
     * Não sabe (e não precisa saber) se veio de HashMap, SQL, MongoDB, cache, API externa...
     * O Adapter esconde todos esses detalhes técnicos.</p>
     * 
     * @return Lista com todos os trabalhos (vazia se nenhum cadastrado, nunca null)
     */
    @Override
    public List<Trabalho> listarTodos() {
        // Implementação simples: converter valores do HashMap em lista
        // Em produção: seria SELECT * (com paginação!)
        System.out.println("📋 [TrabalhoRepositorioMemoria] Listando todos os trabalhos: " + 
                           trabalhos.size() + " encontrado(s)");
        
        return new ArrayList<>(trabalhos.values());
    }
    
    /*
     * ============================================================
     * OBSERVAÇÕES FINAIS SOBRE ARQUITETURA HEXAGONAL
     * ============================================================
     *
     * RESUMO DO QUE ESTA CLASSE REPRESENTA:
     * 
     * 1. É um DRIVEN ADAPTER (Adaptador de Saída)
     * 2. IMPLEMENTA uma DRIVEN PORT (interface ITrabalhoRepositorio)
     * 3. Fornece persistência usando UMA tecnologia específica (HashMap)
     * 4. Pode ser SUBSTITUÍDA por outra implementação (SQL, NoSQL, etc)
     *    SEM alterar o núcleo da aplicação (Domain + Application)
     * 
     * BENEFÍCIOS CONCRETOS QUE OBTEMOS:
     * 
     * ✅ TESTABILIDADE:
     *    Podemos criar um mock de ITrabalhoRepositorio para testes:
     *    
     *    ITrabalhoRepositorio mockRepo = mock(ITrabalhoRepositorio.class);
     *    when(mockRepo.buscarPorId("t1")).thenReturn(trabalhoFake);
     *    IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(mockRepo, alunoRepo);
     *    // Testar sem banco real!
     * 
     * ✅ FLEXIBILIDADE:
     *    Trocar de HashMap para PostgreSQL:
     *    
     *    // ANTES (dev):
     *    ITrabalhoRepositorio repo = new TrabalhoRepositorioMemoria();
     *    
     *    // DEPOIS (prod):
     *    ITrabalhoRepositorio repo = new TrabalhoRepositorioJDBC(conexao);
     *    
     *    // Application não muda!
     * 
     * ✅ MANUTENIBILIDADE:
     *    Podemos adicionar cache, logging, retry sem afetar Application:
     *    
     *    public class TrabalhoRepositorioComCache implements ITrabalhoRepositorio {
     *        private ITrabalhoRepositorio delegado;
     *        private Cache cache;
     *        
     *        public Trabalho buscarPorId(String id) {
     *            Trabalho cached = cache.get(id);
     *            if (cached != null) return cached;
     *            Trabalho fromDB = delegado.buscarPorId(id);
     *            cache.put(id, fromDB);
     *            return fromDB;
     *        }
     *    }
     *    
     *    // Pode empilhar Adapters (Decorator Pattern)!
     * 
     * ✅ SOLID PRINCIPLES:
     *    - Single Responsibility: Adapter só cuida de persistência
     *    - Open/Closed: Fechado para modificação, aberto para extensão (novos Adapters)
     *    - Liskov Substitution: Qualquer ITrabalhoRepositorio pode ser usado
     *    - Interface Segregation: Interface enxuta com só o necessário
     *    - Dependency Inversion: Application depende de abstração, não de implementação
     * 
     * COMPARAÇÃO FINAL: CAMADAS vs HEXAGONAL
     * 
     * CAMADAS (ANTES):
     *   ❌ Application → TrabalhoRepositorio (acoplamento)
     *   ❌ Difícil testar
     *   ❌ Difícil trocar tecnologia
     *   ❌ Viola DIP
     * 
     * HEXAGONAL (DEPOIS):
     *   ✅ Application → ITrabalhoRepositorio ← TrabalhoRepositorioMemoria (inversão!)
     *   ✅ Fácil testar (injetar mocks)
     *   ✅ Fácil trocar (injetar outra implementação)
     *   ✅ Respeita DIP
     * 
     * A mudança de uma linha (interface vs classe concreta) tem
     * IMPACTO ARQUITETURAL GIGANTE na qualidade do software!
     * ============================================================
     */
}
