// Arquivo: src/adapters/output/AlunoRepositorioMemoria.java
package adapters.output;

import domain.model.Aluno;
import domain.ports.output.IAlunoRepositorio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * ============================================================
 * CAMADA: ADAPTERS - OUTPUT (Driven Adapter / Secondary Adapter)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   IMPLEMENTA a interface IAlunoRepositorio (Driven Port) usando
 *   um HashMap em memória. Esta é uma implementação DIDÁTICA para
 *   demonstrar o conceito de Arquitetura Hexagonal sem complexidade
 *   de banco de dados real.
 *
 * O QUE É UM "DRIVEN ADAPTER" (ADAPTADOR DIRIGIDO)?
 *   É uma classe que IMPLEMENTA uma porta de saída (Driven Port).
 *   O núcleo da aplicação (Domain + Application) DEFINE a interface
 *   (IAlunoRepositorio), e este Adapter FORNECE a implementação concreta.
 *   
 *   Driven porque é "DIRIGIDO" pelo núcleo - o núcleo chama o adapter,
 *   não o contrário.
 *
 * POR QUE ESTÁ EM ADAPTERS/OUTPUT?
 *   - OUTPUT porque é uma porta de SAÍDA do hexágono (núcleo chama adapter)
 *   - ADAPTER porque ADAPTA o conceito de "repositório" para uma tecnologia
 *     específica (neste caso, HashMap em memória; poderia ser JDBC, JPA, etc)
 *
 * HEXAGONAL ARCHITECTURE - CONCEITO FUNDAMENTAL:
 *   
 *   Estrutura de dependências (A INVERSÃO!):
 *   
 *      [Application] (núcleo - alto nível)
 *            ↓ depende de
 *      [IAlunoRepositorio] (interface no Domain - abstração)
 *            ↑ implementa
 *      [AlunoRepositorioMemoria] (Adapter - baixo nível)
 *   
 *   COMPARE COM ARQUITETURA EM CAMADAS TRADICIONAL:
 *   
 *   🔴 ANTES (Camadas):
 *   -------------------
 *   Application → AlunoRepositorio (classe concreta em Infrastructure)
 *   
 *   PROBLEMA: Application (alto nível) depende de Infrastructure (baixo nível).
 *   Viola o Dependency Inversion Principle.
 *   Difícil de testar e de trocar implementações.
 *   
 *   🟢 DEPOIS (Hexagonal):
 *   ----------------------
 *   Application → IAlunoRepositorio (interface)
 *                      ↑
 *                 implementa
 *                      |
 *            AlunoRepositorioMemoria
 *   
 *   SOLUÇÃO: Application depende de ABSTRAÇÃO (interface).
 *   Adapter depende da ABSTRAÇÃO E a implementa.
 *   Respeita o Dependency Inversion Principle.
 *   Fácil de testar (passar mocks) e trocar (passar outra implementação).
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - NINGUÉM chama diretamente! Esta classe é chamada através da interface.
 *   - Application chama IAlunoRepositorio, e a JVM resolve qual implementação usar.
 *   - O Controller (Driving Adapter) instancia esta classe e a injeta no Application.
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - HashMap (estrutura de dados Java)
 *   - Entidade Aluno (só para retornar objetos)
 *   - NÃO chama nada do Application ou de outros Adapters
 *
 * POR QUE "MEMÓRIA" NO NOME?
 *   Para deixar CLARO que esta é UMA implementação específica usando HashMap.
 *   Poderíamos ter:
 *   - AlunoRepositorioMemoria (HashMap - esta classe)
 *   - AlunoRepositorioJDBC (SQL com JDBC)
 *   - AlunoRepositorioJPA (SQL com Hibernate/JPA)
 *   - AlunoRepositorioMongo (NoSQL MongoDB)
 *   - AlunoRepositorioREST (API externa)
 *   
 *   TODAS implementariam IAlunoRepositorio.
 *   Application NÃO PRECISA MUDAR para usar qualquer uma delas!
 *   Só mudar qual implementação o Controller injeta.
 *
 * TROCAR IMPLEMENTAÇÃO (EXEMPLO):
 *   
 *   // No AvaliacaoController (Driving Adapter):
 *   
 *   // Desenvolvimento: usar memória
 *   IAlunoRepositorio alunoRepo = new AlunoRepositorioMemoria();
 *   
 *   // Produção: usar PostgreSQL
 *   IAlunoRepositorio alunoRepo = new AlunoRepositorioJDBC("jdbc:postgresql://...");
 *   
 *   // Testes: usar mock
 *   IAlunoRepositorio alunoRepo = mock(IAlunoRepositorio.class);
 *   
 *   // Application NÃO MUDA! Só recebe IAlunoRepositorio.
 *   IGerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(trabalhoRepo, alunoRepo);
 *
 * VANTAGENS DESTA ABORDAGEM:
 *   ✅ Isolamento: Domain não conhece HashMap (poderia ser qualquer tecnologia)
 *   ✅ Testabilidade: Fácil criar mock de IAlunoRepositorio
 *   ✅ Flexibilidade: Trocar HashMap por SQL sem alterar Application
 *   ✅ Paralelismo: Múltiplos desenvolvedores podem trabalhar em parallel
 *                   (um no Domain, outro implementando Adapter JDBC)
 *   ✅ SOLID: Dependency Inversion Principle respeitado
 * ============================================================
 */

/**
 * Implementação em memória do repositório de alunos.
 * 
 * <p>Esta classe é um <b>Driven Adapter</b> (Adaptador de Saída) na Arquitetura Hexagonal.
 * Ela IMPLEMENTA a interface {@link IAlunoRepositorio} (Driven Port) definida no Domain,
 * fornecendo persistência em memória usando HashMap.</p>
 * 
 * <h3>Por que "Memoria" no nome da classe?</h3>
 * <p>Para explicitar que esta é UMA implementação específica. Seguindo o padrão de nomenclatura:
 * <ul>
 *   <li><b>IAlunoRepositorio:</b> O QUE precisa ser feito (interface)</li>
 *   <li><b>AlunoRepositorioMemoria:</b> COMO fazer com HashMap (implementação)</li>
 *   <li><b>AlunoRepositorioJDBC:</b> COMO fazer com SQL (outra implementação possível)</li>
 * </ul>
 * </p>
 * 
 * <h3>Como trocar para banco de dados real?</h3>
 * <pre>
 * // 1. Criar nova classe: AlunoRepositorioJDBC implements IAlunoRepositorio
 * // 2. Implementar os métodos usando JDBC/JPA
 * // 3. No Controller, trocar:
 * 
 * // ANTES (memória):
 * IAlunoRepositorio alunoRepo = new AlunoRepositorioMemoria();
 * 
 * // DEPOIS (SQL):
 * IAlunoRepositorio alunoRepo = new AlunoRepositorioJDBC(conexao);
 * 
 * // Application NÃO muda! GerenciadorAvaliacoes continua igual.
 * </pre>
 * 
 * <h3>Estrutura Hexagonal:</h3>
 * <pre>
 *    ┌─────────────────────────────────┐
 *    │     APPLICATION (núcleo)        │
 *    │  usa IAlunoRepositorio          │
 *    └────────────┬────────────────────┘
 *                 │ chama interface
 *                 ↓
 *    ┌─────────────────────────────────┐
 *    │  IAlunoRepositorio (Port)       │ ← Interface no Domain
 *    └────────────┬────────────────────┘
 *                 ↑ implementa
 *                 │
 *    ┌─────────────────────────────────┐
 *    │ AlunoRepositorioMemoria         │ ← Esta classe (Adapter)
 *    │ usa HashMap                     │
 *    └─────────────────────────────────┘
 * </pre>
 * 
 * <p><b>Nota Didática:</b> Em produção, você usaria um banco de dados relacional (PostgreSQL, MySQL)
 * ou NoSQL (MongoDB), mas o conceito é o MESMO: Application não conhece a implementação,
 * só a interface.</p>
 * 
 * @author Sistema de Avaliações - Exemplo Didático
 * @version 2.0 - Arquitetura Hexagonal
 */
public class AlunoRepositorioMemoria implements IAlunoRepositorio {
    
    // ============================================================
    // ATRIBUTO - ARMAZENAMENTO EM MEMÓRIA
    // ============================================================
    
    /**
     * Armazena alunos em memória usando HashMap para busca O(1).
     * 
     * <p><b>ESTRUTURA:</b> Chave = ID do aluno, Valor = objeto Aluno completo.</p>
     * 
     * <p><b>POR QUE HASHMAP?</b></p>
     * <ul>
     *   <li>Busca rápida por ID: O(1) em média</li>
     *   <li>Simples de usar (didático)</li>
     *   <li>Não precisamos configurar banco de dados</li>
     * </ul>
     * 
     * <p><b>LIMITAÇÕES (didáticas, não usar em produção):</b></p>
     * <ul>
     *   <li>❌ Dados perdidos ao reiniciar a aplicação</li>
     *   <li>❌ Não suporta múltiplos processos (não é thread-safe para acessos externos)</li>
     *   <li>❌ Sem controle transacional</li>
     *   <li>❌ Sem índices ou otimizações complexas</li>
     * </ul>
     * 
     * <p><b>EM PRODUÇÃO:</b> Substitu íria por conexão JDBC, EntityManager (JPA), 
     * MongoClient, etc. Mas a INTERFACE permaneceria a mesma!</p>
     * 
     * <p><b>HEXAGONAL:</b> Este HashMap é um DETALHE DE IMPLEMENTAÇÃO.
     * Application não sabe e não precisa saber que estamos usando HashMap.
     * Só sabe que existe um IAlunoRepositorio com métodos salvar/buscar/listar.</p>
     */
    private final Map<String, Aluno> alunos = new HashMap<>();
    
    // ============================================================
    // IMPLEMENTAÇÃO DOS MÉTODOS DA INTERFACE (Driven Port)
    // ============================================================
    
    /**
     * Salva um aluno no repositório em memória.
     * 
     * <p><b>IMPLEMENTAÇÃO:</b> Adiciona/atualiza o aluno no HashMap usando seu ID como chave.</p>
     * 
     * <p><b>COMPORTAMENTO:</b></p>
     * <ul>
     *   <li>Se o aluno é novo: adiciona ao HashMap</li>
     *   <li>Se o aluno já existe (mesmo ID): sobrescreve (atualiza)</li>
     * </ul>
     * 
     * <p><b>HEXAGONAL:</b> Application chama {@code IAlunoRepositorio.salvar()}.
     * Não sabe que por baixo estamos fazendo {@code map.put()}.
     * Se trocarmos para SQL, Application continua chamando {@code salvar()},
     * e por baixo faríamos {@code INSERT/UPDATE} no banco.</p>
     * 
     * <p><b>COMPARAÇÃO COM SQL:</b></p>
     * <pre>
     * // Esta implementação (memória):
     * alunos.put(aluno.getId(), aluno);
     * 
     * // Implementação JDBC (SQL):
     * String sql = "INSERT INTO alunos (id, nome) VALUES (?, ?) 
     *               ON CONFLICT (id) DO UPDATE SET nome = ?";
     * preparedStatement.setString(1, aluno.getId());
     * preparedStatement.setString(2, aluno.getNome());
     * preparedStatement.setString(3, aluno.getNome());
     * preparedStatement.executeUpdate();
     * 
     * // Application não muda! Continua chamando salvar(aluno).
     * </pre>
     * 
     * @param aluno O aluno a ser salvo (não pode ser null)
     * @throws IllegalArgumentException se aluno for null
     */
    @Override
    public void salvar(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser null");
        }
        
        // Operação simples: HashMap.put()
        // Em um banco real: INSERT ou UPDATE
        alunos.put(aluno.getId(), aluno);
        
        System.out.println("💾 [AlunoRepositorioMemoria] Aluno salvo: " + aluno.getId() + " - " + aluno.getNome());
    }
    
    /**
     * Busca um aluno pelo ID.
     * 
     * <p><b>IMPLEMENTAÇÃO:</b> Usa {@code HashMap.get()} para busca O(1).</p>
     * 
     * <p><b>COMPORTAMENTO:</b></p>
     * <ul>
     *   <li>Se encontrado: retorna o objeto Aluno</li>
     *   <li>Se não encontrado: retorna null</li>
     * </ul>
     * 
     * <p><b>HEXAGONAL:</b> Application chama {@code buscarPorId()}
     * sem saber se estamos buscando em HashMap, SQL, cache, API externa, etc.
     * O COMPORTAMENTO (retornar Aluno ou null) é o mesmo em todas as implementações,
     * mas a ESTRATÉGIA de busca pode variar.</p>
     * 
     * <p><b>COMPARAÇÃO COM SQL:</b></p>
     * <pre>
     * // Esta implementação (memória):
     * return alunos.get(id);  // O(1)
     * 
     * // Implementação JDBC (SQL):
     * String sql = "SELECT id, nome FROM alunos WHERE id = ?";
     * preparedStatement.setString(1, id);
     * ResultSet rs = preparedStatement.executeQuery();
     * if (rs.next()) {
     *     return new Aluno(rs.getString("id"), rs.getString("nome"));
     * }
     * return null;
     * 
     * // Application não muda! Continua chamando buscarPorId(id).
     * </pre>
     * 
     * @param id O identificador do aluno
     * @return O aluno encontrado, ou null se não existir
     */
    @Override
    public Aluno buscarPorId(String id) {
        // Operação simples: HashMap.get()
        // Em um banco real: SELECT com WHERE id = ?
        Aluno aluno = alunos.get(id);
        
        if (aluno != null) {
            System.out.println("🔍 [AlunoRepositorioMemoria] Aluno encontrado: " + aluno.getId() + " - " + aluno.getNome());
        } else {
            System.out.println("🔍 [AlunoRepositorioMemoria] Aluno NÃO encontrado: " + id);
        }
        
        return aluno;
    }
    
    /**
     * Lista todos os alunos cadastrados.
     * 
     * <p><b>IMPLEMENTAÇÃO:</b> Retorna todos os valores do HashMap como uma nova lista.</p>
     * 
     * <p><b>COMPORTAMENTO:</b> Sempre retorna lista (nunca null), mas pode ser vazia.</p>
     * 
     * <p><b>POR QUE {@code new ArrayList<>()}?</b><br>
     * HashMap.values() retorna uma Collection não modificável (visão do map).
     * Criamos uma nova ArrayList para:
     * <ul>
     *   <li>Desacoplar: quem recebe pode modificar a lista sem afetar o HashMap</li>
     *   <li>Segurança: evitar que código externo modifique nosso armazenamento interno</li>
     * </ul>
     * </p>
     * 
     * <p><b>HEXAGONAL:</b> Application chama {@code listarTodos()}
     * sem saber se estamos retornando de HashMap, SQL, cache, etc.
     * Diferentes implementações podem ter otimizações diferentes
     * (paginação, lazy loading), mas a interface permanece simples.</p>
     * 
     * <p><b>COMPARAÇÃO COM SQL:</b></p>
     * <pre>
     * // Esta implementação (memória):
     * return new ArrayList<>(alunos.values());
     * 
     * // Implementação JDBC (SQL):
     * List<Aluno> lista = new ArrayList<>();
     * String sql = "SELECT id, nome FROM alunos";
     * ResultSet rs = statement.executeQuery(sql);
     * while (rs.next()) {
     *     lista.add(new Aluno(rs.getString("id"), rs.getString("nome")));
     * }
     * return lista;
     * 
     * // Application não muda! Continua chamando listarTodos().
     * </pre>
     * 
     * <p><b>OTIMIZAÇÃO EM PRODUÇÃO:</b><br>
     * Em um sistema real com milhões de registros, você implementaria:
     * <ul>
     *   <li>Paginação: {@code listarTodos(int pagina, int tamanhoPagina)}</li>
     *   <li>Filtros: {@code listarPorNome(String nome)}</li>
     *   <li>Lazy loading: retornar Stream ou Iterator</li>
     * </ul>
     * Mas a INTERFACE básica permaneceria no Domain, e implementações complexas no Adapter.
     * </p>
     * 
     * @return Lista com todos os alunos (vazia se nenhum cadastrado, mas nunca null)
     */
    @Override
    public List<Aluno> listarTodos() {
        // Operação simples: criar lista com valores do HashMap
        // Em um banco real: SELECT * (com paginação em produção)
        System.out.println("📋 [AlunoRepositorioMemoria] Listando todos os alunos: " + alunos.size() + " encontrado(s)");
        
        return new ArrayList<>(alunos.values());
    }
}
