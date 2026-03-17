// Arquivo: src/domain/ports/output/ITrabalhoRepositorio.java
package domain.ports.output;

import domain.model.Trabalho;
import java.util.List;

/*
 * ============================================================
 * CAMADA: DOMAIN - PORTS - OUTPUT (Driven Port)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Define o CONTRATO de saída para persistência de Trabalhos.
 *   Especifica quais operações de armazenamento são necessárias.
 *
 * O QUE É UMA "DRIVEN PORT" (PORTA DE SAÍDA)?
 *   É uma INTERFACE que define como nossa aplicação precisa
 *   interagir com sistemas externos de persistência.
 *
 * POR QUE É UMA INTERFACE?
 *   ⚠️ ESTE É O PONTO CENTRAL DA CONVERSÃO PARA HEXAGONAL! ⚠️
 *   
 *   PROBLEMA NA ARQUITETURA EM CAMADAS:
 *   GerenciadorAvaliacoes dependia diretamente de TrabalhoRepositorio
 *   (classe concreta da camada Infrastructure). Isso criava:
 *   - Acoplamento forte
 *   - Impossibilidade de testar GerenciadorAvaliacoes isoladamente
 *   - Dificuldade de trocar a implementação
 *   - Violação do Dependency Inversion Principle
 *   
 *   SOLUÇÃO COM ARQUITETURA HEXAGONAL:
 *   GerenciadorAvaliacoes agora depende de ITrabalhoRepositorio
 *   (interface do Domain). A implementação concreta é injetada.
 *   
 *   Antes: Application ──depende──> Infrastructure (RUIM)
 *   Depois: Application ──depende──> Interface ←──implementa── Infrastructure (BOM)
 *
 * QUEM DEFINE ESTA INTERFACE?
 *   O NÚCLEO DA APLICAÇÃO (Domain/Application)!
 *   Quem usa repositórios (Application) define o que precisa.
 *   Quem implementa repositórios (Adapters) se adapta a isso.
 *   
 *   Isso inverte a direção das dependências, daí o nome
 *   "Inversão de Dependência" (o D do SOLID).
 *
 * QUEM IMPLEMENTA ESTA INTERFACE?
 *   Driven Adapters de persistência:
 *   - TrabalhoRepositorioMemoria (atual - HashMap)
 *   - TrabalhoRepositorioPostgreSQL (futuro possível)
 *   - TrabalhoRepositorioMongoDB (futuro possível)
 *   - FakeTrabalhoRepositorio (para testes unitários)
 *
 * QUEM USA ESTA INTERFACE?
 *   GerenciadorAvaliacoes (Application layer) declara:
 *   ```java
 *   private final ITrabalhoRepositorio trabalhoRepositorio;
 *   ```
 *   E recebe a implementação via construtor (Dependency Injection).
 *
 * HEXAGONAL ARCHITECTURE - DEMONSTRAÇÃO PRÁTICA:
 *   
 *   📊 COMO TESTAR GerenciadorAvaliacoes SEM BANCO DE DADOS:
 *   
 *   ```java
 *   // Criar um repositório fake que sempre retorna o mesmo trabalho
 *   ITrabalhoRepositorio fakeRepo = new ITrabalhoRepositorio() {
 *       public void salvar(Trabalho t) { /* não faz nada */ }
 *       public Trabalho buscarPorId(String id) {
 *           return new Trabalho(id, "Trabalho de Teste", "autor1");
 *       }
 *       public List<Trabalho> listarTodos() { return List.of(); }
 *   };
 *   
 *   // Injetar o fake no gerenciador
 *   IGerenciadorAvaliacoes gerenciador = 
 *       new GerenciadorAvaliacoes(fakeRepo, outroFakeRepo);
 *   
 *   // Testar a lógica de negócio SEM banco de dados!
 *   gerenciador.avaliarTrabalho("n1", "t1", "a1", 9.5);
 *   ```
 *   
 *   Isso é IMPOSSÍVEL quando dependemos de classes concretas!
 *
 * BENEFÍCIOS EM RELAÇÃO À ARQUITETURA EM CAMADAS:
 *   ✅ Domain não conhece Implementation Details (JDBC, JPA, etc.)
 *   ✅ Testes unitários rápidos (sem banco de dados)
 *   ✅ Trocar tecnologia sem reescrever lógica de negócio
 *   ✅ Múltiplas implementações simultâneas (cache + DB)
 *   ✅ Código de negócio portável entre projetos
 * ============================================================
 */

/**
 * Porta de saída para persistência de Trabalhos.
 * Define as operações necessárias para armazenar e recuperar trabalhos.
 * 
 * OPERAÇÕES:
 * - Salvar trabalho
 * - Buscar trabalho por ID
 * - Listar todos os trabalhos
 */
public interface ITrabalhoRepositorio {
    
    /**
     * Persiste um trabalho no armazenamento.
     * 
     * @param trabalho Trabalho a ser salvo
     * 
     * CONTEXTO HEXAGONAL:
     * Esta interface está no DOMÍNIO, mas será implementada
     * por um ADAPTER da camada de infraestrutura.
     * 
     * Fluxo de dependências:
     * GerenciadorAvaliacoes → ITrabalhoRepositorio ← TrabalhoRepositorioMemoria
     *    (Application)           (Domain Port)            (Infrastructure Adapter)
     * 
     * Note que as setas de dependência apontam PARA O DOMÍNIO,
     * não para a infraestrutura. Isso é a essência da Hexagonal!
     */
    void salvar(Trabalho trabalho);
    
    /**
     * Busca um trabalho pelo seu identificador.
     * 
     * @param id ID do trabalho
     * @return Trabalho encontrado, ou null se não existir
     * 
     * CONTEXTO HEXAGONAL:
     * O Application usa este método sem saber a implementação:
     * 
     * ```java
     * Trabalho t = trabalhoRepositorio.buscarPorId("t1");
     * ```
     * 
     * Pode ser:
     * - Um get em HashMap (implementação atual)
     * - Um SELECT no PostgreSQL
     * - Uma chamada HTTP para API externa
     * - Um retorno de cache
     * 
     * O GerenciadorAvaliacoes não precisa saber! Esta abstração
     * é o que torna o código flexível e testável.
     */
    Trabalho buscarPorId(String id);
    
    /**
     * Lista todos os trabalhos cadastrados.
     * 
     * @return Lista de todos os trabalhos
     * 
     * CONTEXTO HEXAGONAL:
     * Interface simples, mas permite otimizações complexas
     * na implementação (paginação, lazy loading, etc.) sem
     * afetar o código que usa esta porta.
     */
    List<Trabalho> listarTodos();
}
