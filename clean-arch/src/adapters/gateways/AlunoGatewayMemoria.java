// Arquivo: src/adapters/gateways/AlunoGatewayMemoria.java
package adapters.gateways;

import entities.Aluno;
import usecases.gateways.IAlunoGateway;

import java.util.HashMap;
import java.util.Map;

/*
 * ============================================================
 * CLEAN ARCHITECTURE - CAMADA: INTERFACE ADAPTERS (Camada 3)
 * Gateway Implementation
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Converte dados entre o formato dos Use Cases e o formato externo.
 *   Implementa a interface IAlunoGateway definida nos Use Cases.
 *
 * POSIÇÃO NA CLEAN ARCHITECTURE:
 *   ○ Entities
 *   ○ Use Cases (interface IAlunoGateway está aqui)
 *   ● ← Você está aqui (Interface Adapters - implementação)
 *   ○ Frameworks & Drivers
 *
 * TIPOS DE ADAPTERS:
 *   - Controllers: Recebem requisições, chamam Use Cases
 *   - Presenters: Formatam Outputs para visualização
 *   - Gateways: Implementam interfaces de persistência ← VOCÊ ESTÁ AQUI
 *
 * REGRA DE DEPENDÊNCIA:
 *   - PODE depender de: Use Cases, Entities
 *   - NÃO PODE depender de: Frameworks & Drivers (camada externa)
 *   - Framework & Drivers é que dependem de Adapters!
 *
 * DIFERENÇA DE HEXAGONAL:
 *   Em Hexagonal: AlunoRepositorioMemoria (Driven Adapter)
 *   Em Clean: AlunoGatewayMemoria (Gateway Implementation)
 *   
 *   Clean separa mais claramente:
 *   - Interface está nos Use Cases (IAlunoGateway)
 *   - Implementação está nos Adapters (AlunoGatewayMemoria)
 *   - Isso torna a inversão de dependência mais explícita
 *
 * INVERSÃO DE DEPENDÊNCIA:
 *   Esta classe IMPLEMENTA IAlunoGateway (definida nos Use Cases)
 *   Ou seja: Adapters dependem de Use Cases, não o contrário!
 * ============================================================
 */

/**
 * 🎓 GATEWAY IMPLEMENTATION: ALUNO (MEMÓRIA)
 * 
 * Implementação concreta da persistência de Alunos usando HashMap.
 * 
 * RESPONSABILIDADE:
 * - Armazenar e recuperar Alunos (em memória para fins didáticos)
 * - Implementar a interface IAlunoGateway
 * 
 * POR QUE EM MEMÓRIA (HASHMAP)?
 * - Para fins didáticos (simples de entender)
 * - Em produção, seria AlunoGatewayMySQL, AlunoGatewayMongoDB, etc
 * - A interface IAlunoGateway é a mesma, apenas a implementação muda
 * 
 * PLUG & PLAY:
 * - Podemos trocar esta implementação sem afetar os Use Cases
 * - Use Cases dependem da INTERFACE, não da IMPLEMENTAÇÃO
 * - Isso é Dependency Inversion Principle em ação!
 */
public class AlunoGatewayMemoria implements IAlunoGateway {
    
    // Armazenamento em memória (HashMap)
    private final Map<String, Aluno> alunos = new HashMap<>();

    /**
     * Salva um aluno no repositório em memória.
     * 
     * NOTA: HashMap.put() substitui se já existir com mesmo ID.
     * 
     * @param aluno Aluno a ser salvo
     */
    @Override
    public void salvar(Aluno aluno) {
        alunos.put(aluno.getId(), aluno);
    }

    /**
     * Busca um aluno pelo seu ID.
     * 
     * @param id ID do aluno
     * @return Aluno encontrado, ou null se não existir
     */
    @Override
    public Aluno buscarPorId(String id) {
        return alunos.get(id);
    }

    /**
     * Verifica se um aluno existe no repositório.
     * 
     * @param id ID do aluno
     * @return true se o aluno existe, false caso contrário
     */
    @Override
    public boolean existe(String id) {
        return alunos.containsKey(id);
    }
}
