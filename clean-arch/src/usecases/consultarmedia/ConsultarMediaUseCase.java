// Arquivo: src/usecases/consultarmedia/ConsultarMediaUseCase.java
package usecases.consultarmedia;

import entities.Trabalho;
import usecases.gateways.ITrabalhoGateway;

/*
 * ============================================================
 * CLEAN ARCHITECTURE - CAMADA: USE CASES (Camada 2)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Orquestra o fluxo de dados entre Entities e o mundo externo.
 *   Application Business Rules - regras específicas desta aplicação.
 *
 * POSIÇÃO NA CLEAN ARCHITECTURE:
 *   ○ Entities
 *   ● ← Você está aqui (Use Cases)
 *   ○ Interface Adapters
 *   ○ Frameworks & Drivers
 *
 * QUEM PODE CHAMAR ESTE USE CASE?
 *   - Controllers (Interface Adapters)
 *   - Qualquer Interface Adapter de entrada
 *
 * QUEM ESTE USE CASE PODE CHAMAR?
 *   - Entities (criar, manipular, validar)
 *   - Gateways (interfaces definidas nesta camada)
 *   - NÃO pode chamar Controllers, Presenters, ou Frameworks!
 *
 * DIFERENÇA DE HEXAGONAL:
 *   Em Hexagonal: GerenciadorAvaliacoes.consultarMedia() (um método)
 *   Em Clean: ConsultarMediaUseCase (classe dedicada)
 *   
 *   Vantagens da Clean:
 *   - Use Cases mais coesos (Single Responsibility)
 *   - Mais fácil testar isoladamente
 *   - Mais fácil entender o propósito de cada classe
 *
 * DTOs (Input/Output):
 *   Clean Architecture enfatiza o uso de DTOs nas fronteiras.
 *   Input: Dados vindos do Controller
 *   Output: Dados retornados ao Presenter
 *   Isso desacopla Use Cases de detalhes de apresentação.
 * ============================================================
 */

/**
 * 🎓 USE CASE: CONSULTAR MÉDIA
 * 
 * Responsável por orquestrar a consulta da média de um trabalho.
 * 
 * FLUXO DO USE CASE:
 * 1. Recebe Input com o ID do trabalho
 * 2. Busca o trabalho (via Gateway)
 * 3. Obtém a média calculada pela entidade
 * 4. Retorna Output com as informações do trabalho e média
 * 
 * PRINCÍPIO SINGLE RESPONSIBILITY:
 * - Este Use Case faz UMA coisa: consultar média
 * - Não mistura com cadastrar ou avaliar
 * - Cada operação tem sua própria classe
 * 
 * OBSERVAÇÃO SOBRE SIMPLICIDADE:
 * - Este Use Case é simples (apenas busca e retorna)
 * - Mas ainda assim tem sua própria classe
 * - Isso mantém consistência e facilita evolução futura
 * - Exemplo: No futuro, poderia adicionar ranking, histórico, etc
 */
public class ConsultarMediaUseCase {
    
    private final ITrabalhoGateway trabalhoGateway;

    /**
     * Construtor com injeção de dependências.
     * 
     * @param trabalhoGateway Gateway para buscar trabalhos
     */
    public ConsultarMediaUseCase(ITrabalhoGateway trabalhoGateway) {
        this.trabalhoGateway = trabalhoGateway;
    }

    /**
     * Executa o Use Case de consultar média.
     * 
     * ORQUESTRAÇÃO SIMPLES:
     * - Este caso é simples: busca e retorna
     * - Mas ainda segue o padrão de Use Case
     * - Mantém consistência com outros Use Cases
     * - Facilita testes e manutenção
     * 
     * @param input Dados de entrada (DTO)
     * @return Resultado da operação (DTO)
     */
    public ConsultarMediaOutput executar(ConsultarMediaInput input) {
        try {
            // PASSO 1: Buscar o trabalho via Gateway
            Trabalho trabalho = trabalhoGateway.buscarPorId(input.getTrabalhoId());
            if (trabalho == null) {
                return ConsultarMediaOutput.erro(
                    "Trabalho não encontrado com ID: " + input.getTrabalhoId()
                );
            }

            // PASSO 2: Obter a média calculada pela entidade
            // IMPORTANTE: A lógica de cálculo de média está NA ENTITY!
            // Use Case apenas consulta o resultado
            double media = trabalho.calcularMedia();

            // PASSO 3: Retornar Output de sucesso
            return ConsultarMediaOutput.sucesso(
                trabalho.getId(),
                trabalho.getTitulo(),
                trabalho.getAutor().getNome(),
                media,
                trabalho.getQuantidadeAvaliacoes()
            );

        } catch (Exception e) {
            // Captura erros inesperados
            return ConsultarMediaOutput.erro(
                "Erro ao consultar média: " + e.getMessage()
            );
        }
    }
}
