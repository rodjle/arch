// Arquivo: src/adapters/gateways/TrabalhoGatewayMemoria.java
package adapters.gateways;

import entities.Trabalho;
import usecases.gateways.ITrabalhoGateway;

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
 *   Implementa a interface ITrabalhoGateway definida nos Use Cases.
 *
 * POSIÇÃO NA CLEAN ARCHITECTURE:
 *   ○ Entities
 *   ○ Use Cases (interface ITrabalhoGateway está aqui)
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
 *   Em Hexagonal: TrabalhoRepositorioMemoria (Driven Adapter)
 *   Em Clean: TrabalhoGatewayMemoria (Gateway Implementation)
 *   
 *   Clean separa mais claramente:
 *   - Interface está nos Use Cases (ITrabalhoGateway)
 *   - Implementação está nos Adapters (TrabalhoGatewayMemoria)
 *   - Isso torna a inversão de dependência mais explícita
 *
 * INVERSÃO DE DEPENDÊNCIA:
 *   Esta classe IMPLEMENTA ITrabalhoGateway (definida nos Use Cases)
 *   Ou seja: Adapters dependem de Use Cases, não o contrário!
 * ============================================================
 */

/**
 * 🎓 GATEWAY IMPLEMENTATION: TRABALHO (MEMÓRIA)
 * 
 * Implementação concreta da persistência de Trabalhos usando HashMap.
 * 
 * RESPONSABILIDADE:
 * - Armazenar e recuperar Trabalhos (em memória para fins didáticos)
 * - Implementar a interface ITrabalhoGateway
 * 
 * POR QUE EM MEMÓRIA (HASHMAP)?
 * - Para fins didáticos (simples de entender)
 * - Em produção, seria TrabalhoGatewayMySQL, TrabalhoGatewayMongoDB, etc
 * - A interface ITrabalhoGateway é a mesma, apenas a implementação muda
 * 
 * PLUG & PLAY:
 * - Podemos trocar esta implementação sem afetar os Use Cases
 * - Use Cases dependem da INTERFACE, não da IMPLEMENTAÇÃO
 * - Isso é Dependency Inversion Principle em ação!
 */
public class TrabalhoGatewayMemoria implements ITrabalhoGateway {
    
    // Armazenamento em memória (HashMap)
    private final Map<String, Trabalho> trabalhos = new HashMap<>();

    /**
     * Salva um trabalho no repositório em memória.
     * 
     * NOTA: HashMap.put() substitui se já existir com mesmo ID.
     * Isso permite atualizar trabalhos (ex: adicionar avaliações).
     * 
     * @param trabalho Trabalho a ser salvo
     */
    @Override
    public void salvar(Trabalho trabalho) {
        trabalhos.put(trabalho.getId(), trabalho);
    }

    /**
     * Busca um trabalho pelo seu ID.
     * 
     * @param id ID do trabalho
     * @return Trabalho encontrado, ou null se não existir
     */
    @Override
    public Trabalho buscarPorId(String id) {
        return trabalhos.get(id);
    }

    /**
     * Verifica se um trabalho existe no repositório.
     * 
     * @param id ID do trabalho
     * @return true se o trabalho existe, false caso contrário
     */
    @Override
    public boolean existe(String id) {
        return trabalhos.containsKey(id);
    }
}
