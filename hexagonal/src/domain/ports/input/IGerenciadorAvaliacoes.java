// Arquivo: src/domain/ports/input/IGerenciadorAvaliacoes.java
package domain.ports.input;

/*
 * ============================================================
 * CAMADA: DOMAIN - PORTS - INPUT (Driving Port)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Define o CONTRATO de entrada para o sistema.
 *   Especifica quais operações (casos de uso) o sistema oferece.
 *
 * O QUE É UMA "DRIVING PORT" (PORTA DE ENTRADA)?
 *   É uma INTERFACE que define como o mundo externo pode
 *   DIRIGIR/COMANDAR nossa aplicação. O Controller (Driving Adapter)
 *   usa esta interface para invocar funcionalidades de negócio.
 *
 * POR QUE É UMA INTERFACE?
 *   Para DESACOPLAR a camada de apresentação (Controller) da
 *   camada de aplicação (GerenciadorAvaliacoes). O Controller
 *   não precisa saber COMO os casos de uso são implementados,
 *   apenas O QUE eles fazem.
 *
 * QUEM DEFINE ESTA INTERFACE?
 *   O DOMÍNIO! Esta interface pertence ao núcleo da aplicação,
 *   não ao Adapter. Quem chama (Controller) deve se adaptar ao
 *   que o domínio define, não o contrário.
 *
 * QUEM IMPLEMENTA ESTA INTERFACE?
 *   A camada Application (GerenciadorAvaliacoes)
 *
 * QUEM USA ESTA INTERFACE?
 *   Os Driving Adapters (Controllers, APIs REST, CLI, etc.)
 *
 * HEXAGONAL ARCHITECTURE - CONCEITO FUNDAMENTAL:
 *   Esta é uma das peças-chave da Hexagonal Architecture!
 *   
 *   Fluxo: 
 *   [Driving Adapter] → [Driving Port (esta interface)] → [Application]
 *   
 *   O hexágono oferece PORTAS (interfaces) para o mundo externo.
 *   Adapters se conectam a essas portas. Esta é a "porta de entrada"
 *   para casos de uso de avaliação de trabalhos.
 *
 * DIFERENÇA DA ARQUITETURA EM CAMADAS:
 *   Na arquitetura em camadas tradicional, NÃO EXISTIA esta interface!
 *   O Controller chamava GerenciadorAvaliacoes diretamente (classe concreta).
 *   
 *   Problema da abordagem antiga:
 *   - Controller acoplado à implementação
 *   - Difícil criar mocks para testes
 *   - Difícil ter múltiplas implementações
 *   
 *   Solução com Hexagonal:
 *   - Controller depende de IGerenciadorAvaliacoes (interface)
 *   - Fácil criar mocks/stubs para testes
 *   - Podemos ter GerenciadorAvaliacoesReal, GerenciadorAvaliacoesFake, etc.
 * ============================================================
 */

/**
 * Porta de entrada para o sistema de avaliação de trabalhos.
 * Define os casos de uso disponíveis.
 * 
 * CASOS DE USO:
 * - Cadastrar trabalho
 * - Avaliar trabalho (adicionar nota)
 * - Consultar média de um trabalho
 */
public interface IGerenciadorAvaliacoes {
    
    /**
     * Cadastra um novo trabalho no sistema.
     * 
     * @param id Identificador único do trabalho
     * @param titulo Título do trabalho
     * @param autorId ID do aluno autor
     * 
     * CONTEXTO HEXAGONAL:
     * Este método faz parte da API pública do hexágono.
     * Qualquer Driving Adapter (web, desktop, mobile, CLI)
     * pode chamar este método através desta interface.
     */
    void cadastrarTrabalho(String id, String titulo, String autorId);
    
    /**
     * Registra uma avaliação para um trabalho.
     * 
     * @param notaId Identificador único da nota
     * @param trabalhoId ID do trabalho sendo avaliado
     * @param avaliadorId ID do aluno que está avaliando
     * @param valor Valor da nota (0.0 a 10.0)
     * 
     * CONTEXTO HEXAGONAL:
     * O Adapter de entrada não precisa saber:
     * - Como a nota é validada (isso está no Domain)
     * - Como o trabalho é buscado (isso é responsabilidade dos Driven Ports)
     * - Como a nota é persistida (isso é responsabilidade dos Driven Adapters)
     * 
     * Ele apenas chama este método e o núcleo hexagonal cuida do resto!
     */
    void avaliarTrabalho(String notaId, String trabalhoId, String avaliadorId, double valor);
    
    /**
     * Consulta a média de notas de um trabalho.
     * 
     * @param trabalhoId ID do trabalho
     * @return Média das avaliações recebidas
     * 
     * CONTEXTO HEXAGONAL:
     * Este é um método de consulta (query). O Adapter recebe
     * um double e pode exibi-lo em JSON (REST API), em HTML
     * (página web), ou no console (CLI). O hexágono não se
     * importa com como os dados são apresentados.
     */
    double consultarMedia(String trabalhoId);
}
