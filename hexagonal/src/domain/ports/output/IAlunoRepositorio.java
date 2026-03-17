// Arquivo: src/domain/ports/output/IAlunoRepositorio.java
package domain.ports.output;

import domain.model.Aluno;
import java.util.List;

/*
 * ============================================================
 * CAMADA: DOMAIN - PORTS - OUTPUT (Driven Port)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Define o CONTRATO de saída para persistência de Alunos.
 *   Especifica quais operações de armazenamento são necessárias.
 *
 * O QUE É UMA "DRIVEN PORT" (PORTA DE SAÍDA)?
 *   É uma INTERFACE que define como nossa aplicação precisa
 *   de serviços externos (banco de dados, APIs, etc.).
 *   A aplicação DIRIGE/COMANDA o mundo externo através desta porta.
 *
 * POR QUE É UMA INTERFACE?
 *   Para aplicar INVERSÃO DE DEPENDÊNCIA (Dependency Inversion Principle)!
 *   
 *   ❌ ERRADO (Arquitetura em Camadas Tradicional):
 *   Application → AlunoRepositorio (classe concreta do Infrastructure)
 *   
 *   ✅ CORRETO (Arquitetura Hexagonal):
 *   Application → IAlunoRepositorio (interface do Domain) ← AlunoRepositorioMemoria (Adapter)
 *
 * QUEM DEFINE ESTA INTERFACE?
 *   O DOMÍNIO/APPLICATION! Não é o repositório concreto que dita
 *   quais métodos existem. É o DOMÍNIO que diz "eu preciso de
 *   salvar, buscar e listar". O repositório concreto se adapta a isso.
 *
 * QUEM IMPLEMENTA ESTA INTERFACE?
 *   Os Driven Adapters de persistência:
 *   - AlunoRepositorioMemoria (implementação em memória)
 *   - AlunoRepositorioJDBC (implementação com banco SQL) - futuro
 *   - AlunoRepositorioMongoDB (implementação NoSQL) - futuro
 *
 * QUEM USA ESTA INTERFACE?
 *   A camada Application (GerenciadorAvaliacoes) declara dependência
 *   desta interface. Em tempo de execução, recebe uma implementação
 *   concreta via injeção de dependência.
 *
 * HEXAGONAL ARCHITECTURE - INVERSÃO DE DEPENDÊNCIA:
 *   Este é o conceito mais importante da Arquitetura Hexagonal!
 *   
 *   Fluxo:
 *   [Application] → [Driven Port (esta interface)] ← [Driven Adapter]
 *   
 *   Antes (Camadas): Application DEPENDIA de AlunoRepositorio concreto
 *   Depois (Hexagonal): Application DEPENDE de IAlunoRepositorio (interface)
 *   
 *   Resultado: O DOMÍNIO COMANDA, a INFRAESTRUTURA OBEDECE!
 *
 * BENEFÍCIOS:
 *   1. TESTABILIDADE: Posso criar um FakeAlunoRepositorio para testes
 *   2. FLEXIBILIDADE: Posso trocar de HashMap para PostgreSQL sem
 *      alterar uma linha do Domain ou Application
 *   3. INDEPENDÊNCIA: O núcleo hexagonal não conhece JDBC, Hibernate,
 *      ou qualquer framework de persistência
 * ============================================================
 */

/**
 * Porta de saída para persistência de Alunos.
 * Define as operações necessárias para armazenar e recuperar alunos.
 * 
 * OPERAÇÕES:
 * - Salvar aluno
 * - Buscar aluno por ID
 * - Listar todos os alunos
 */
public interface IAlunoRepositorio {
    
    /**
     * Persiste um aluno no armazenamento.
     * 
     * @param aluno Aluno a ser salvo
     * 
     * CONTEXTO HEXAGONAL:
     * O Application não sabe e não se importa com:
     * - Se vai para um HashMap, arquivo, ou banco de dados
     * - Se é síncrono ou assíncrono
     * - Se usa JDBC, JPA, ou MongoDB
     * 
     * Ele apenas chama salvar() e confia que o Adapter implementará
     * corretamente. Isso é INVERSÃO DE DEPENDÊNCIA em ação!
     */
    void salvar(Aluno aluno);
    
    /**
     * Busca um aluno pelo seu identificador.
     * 
     * @param id ID do aluno
     * @return Aluno encontrado, ou null se não existir
     * 
     * CONTEXTO HEXAGONAL:
     * O Application define o QUE precisa (buscar por ID).
     * O Adapter decide COMO fazer (query SQL, get do HashMap, etc.).
     * 
     * Esta separação permite que mudemos a tecnologia de persistência
     * sem reescrever a lógica de negócio!
     */
    Aluno buscarPorId(String id);
    
    /**
     * Lista todos os alunos cadastrados.
     * 
     * @return Lista de todos os alunos
     * 
     * CONTEXTO HEXAGONAL:
     * Mesmo que tenhamos 10 milhões de alunos, o contrato é o mesmo.
     * O Adapter de banco de dados pode implementar paginação, cache,
     * ou otimizações sem que o Application precise saber.
     */
    List<Aluno> listarTodos();
}
