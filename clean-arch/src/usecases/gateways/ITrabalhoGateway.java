// Arquivo: src/usecases/gateways/ITrabalhoGateway.java
package usecases.gateways;

import entities.Trabalho;

/*
 * ============================================================
 * CLEAN ARCHITECTURE - CAMADA: USE CASES (Camada 2)
 * Gateway Interface (Boundary)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Define o contrato para acesso a dados de Trabalhos.
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
 *   Em Hexagonal: "ITrabalhoRepositorio" (Output Port)
 *   Em Clean: "ITrabalhoGateway" (Gateway Interface)
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
 * 🎓 GATEWAY INTERFACE: TRABALHO
 * 
 * Define as operações necessárias para persistir e recuperar Trabalhos.
 * 
 * IMPORTANTE:
 * - Esta interface é DEFINIDA aqui nos Use Cases
 * - Mas IMPLEMENTADA na camada de Interface Adapters
 * - Isso inverte a dependência! (Dependency Inversion Principle)
 * 
 * QUEM USA ESTA INTERFACE?
 * - Use Cases (CadastrarTrabalhoUseCase, AvaliarTrabalhoUseCase, ConsultarMediaUseCase)
 * 
 * QUEM IMPLEMENTA ESTA INTERFACE?
 * - TrabalhoGatewayMemoria (na camada de Interface Adapters)
 * - Poderia ter TrabalhoGatewayMySQL, TrabalhoGatewayMongoDB, etc
 */
public interface ITrabalhoGateway {
    
    /**
     * Salva um trabalho no repositório de dados.
     * 
     * @param trabalho Trabalho a ser salvo
     */
    void salvar(Trabalho trabalho);
    
    /**
     * Busca um trabalho pelo seu ID.
     * 
     * @param id ID do trabalho
     * @return Trabalho encontrado, ou null se não existir
     */
    Trabalho buscarPorId(String id);
    
    /**
     * Verifica se um trabalho existe no repositório.
     * 
     * @param id ID do trabalho
     * @return true se o trabalho existe, false caso contrário
     */
    boolean existe(String id);
}
