// Arquivo: src/usecases/gateways/IAlunoGateway.java
package usecases.gateways;

import entities.Aluno;

/*
 * ============================================================
 * CLEAN ARCHITECTURE - CAMADA: USE CASES (Camada 2)
 * Gateway Interface (Boundary)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Define o contrato para acesso a dados de Alunos.
 *   Esta é uma INTERFACE (abstração) que pertence à camada de Use Cases.
 *
 * POSIÇÃO NA CLEAN ARCHITECTURE:
 *   ○ Entities
 *   ● ← Você está aqui (Use Cases - interface definida aqui)
 *   ○ Interface Adapters (implementação concreta está aqui)
 *   ○ Frameworks & Drivers
 *
 * INVERSÃO DE DEPENDÊNCIA:
 *   - Esta interface é DEFINIDA na camada de Use Cases
 *   - Mas é IMPLEMENTADA na camada de Interface Adapters
 *   - Isso faz a dependência apontar PARA DENTRO (dos Adapters para Use Cases)
 *   - Sem isso, Use Cases dependeriam de Adapters (errado!)
 *
 * DIFERENÇA DE HEXAGONAL:
 *   Em Hexagonal: "IAlunoRepositorio" (Output Port)
 *   Em Clean: "IAlunoGateway" (Gateway Interface)
 *   
 *   O conceito é o mesmo (inversão de dependência), mas Clean:
 *   - Chama de "Gateway" (porta de saída para dados)
 *   - Deixa mais explícito que a interface pertence aos Use Cases
 *   - Enfatiza que a implementação está na camada externa
 *
 * POR QUE "GATEWAY" E NÃO "REPOSITORY"?
 *   - Uncle Bob prefere "Gateway" para interfaces de persistência
 *   - "Repository" pode sugerir padrão DDD específico
 *   - "Gateway" é mais genérico (pode ser banco, API, arquivo, etc)
 * ============================================================
 */

/**
 * 🎓 GATEWAY INTERFACE: ALUNO
 * 
 * Define as operações necessárias para persistir e recuperar Alunos.
 * 
 * IMPORTANTE:
 * - Esta interface é DEFINIDA aqui nos Use Cases
 * - Mas IMPLEMENTADA na camada de Interface Adapters
 * - Isso inverte a dependência! (Dependency Inversion Principle)
 * 
 * QUEM USA ESTA INTERFACE?
 * - Use Cases (CadastrarTrabalhoUseCase, AvaliarTrabalhoUseCase)
 * 
 * QUEM IMPLEMENTA ESTA INTERFACE?
 * - AlunoGatewayMemoria (na camada de Interface Adapters)
 * - Poderia ter AlunoGatewayMySQL, AlunoGatewayMongoDB, etc
 */
public interface IAlunoGateway {
    
    /**
     * Salva um aluno no repositório de dados.
     * 
     * @param aluno Aluno a ser salvo
     */
    void salvar(Aluno aluno);
    
    /**
     * Busca um aluno pelo seu ID.
     * 
     * @param id ID do aluno
     * @return Aluno encontrado, ou null se não existir
     */
    Aluno buscarPorId(String id);
    
    /**
     * Verifica se um aluno existe no repositório.
     * 
     * @param id ID do aluno
     * @return true se o aluno existe, false caso contrário
     */
    boolean existe(String id);
}
