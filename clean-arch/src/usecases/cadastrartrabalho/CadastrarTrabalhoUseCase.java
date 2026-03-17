// Arquivo: src/usecases/cadastrartrabalho/CadastrarTrabalhoUseCase.java
package usecases.cadastrartrabalho;

import entities.Aluno;
import entities.Trabalho;
import usecases.gateways.IAlunoGateway;
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
 *   Em Hexagonal: GerenciadorAvaliacoes (um serviço com todos os métodos)
 *   Em Clean: Um Use Case para CADA operação (granularidade)
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
 * 🎓 USE CASE: CADASTRAR TRABALHO
 * 
 * Responsável por orquestrar o cadastro de um novo trabalho no sistema.
 * 
 * FLUXO DO USE CASE:
 * 1. Recebe Input com os dados do trabalho
 * 2. Valida se o aluno autor existe (via Gateway)
 * 3. Cria a entidade Trabalho (regras de negócio são validadas)
 * 4. Persiste o trabalho (via Gateway)
 * 5. Retorna Output com o resultado
 * 
 * PRINCÍPIO SINGLE RESPONSIBILITY:
 * - Este Use Case faz UMA coisa: cadastrar trabalho
 * - Não mistura com avaliar trabalho ou consultar média
 * - Em Hexagonal, tudo estava em um "GerenciadorAvaliacoes"
 * - Clean separa cada operação em sua própria classe
 * 
 * DEPENDENCY INVERSION:
 * - Depende de IAlunoGateway e ITrabalhoGateway (abstrações)
 * - NÃO depende de AlunoGatewayMemoria (implementação concreta)
 * - As implementações concretas dependem desta camada, não o contrário!
 */
public class CadastrarTrabalhoUseCase {
    
    private final ITrabalhoGateway trabalhoGateway;
    private final IAlunoGateway alunoGateway;

    /**
     * Construtor com injeção de dependências.
     * 
     * INVERSÃO DE DEPENDÊNCIA:
     * - Recebemos INTERFACES (abstrações)
     * - Não sabemos qual é a implementação concreta
     * - Main.java é quem injeta as implementações concretas
     * 
     * @param trabalhoGateway Gateway para persistência de trabalhos
     * @param alunoGateway Gateway para buscar alunos
     */
    public CadastrarTrabalhoUseCase(ITrabalhoGateway trabalhoGateway, IAlunoGateway alunoGateway) {
        this.trabalhoGateway = trabalhoGateway;
        this.alunoGateway = alunoGateway;
    }

    /**
     * Executa o Use Case de cadastrar trabalho.
     * 
     * ORQUESTRAÇÃO:
     * - Este método coordena o fluxo entre Gateways e Entities
     * - NÃO contém regras de negócio (essas estão nas Entities)
     * - Apenas orquestra: busca aluno, cria trabalho, salva
     * 
     * @param input Dados de entrada (DTO)
     * @return Resultado da operação (DTO)
     */
    public CadastrarTrabalhoOutput executar(CadastrarTrabalhoInput input) {
        try {
            // PASSO 1: Verificar se o trabalho já existe
            if (trabalhoGateway.existe(input.getTrabalhoId())) {
                return CadastrarTrabalhoOutput.erro(
                    "Já existe um trabalho com o ID: " + input.getTrabalhoId()
                );
            }

            // PASSO 2: Buscar o aluno autor via Gateway
            Aluno autor = alunoGateway.buscarPorId(input.getAlunoId());
            if (autor == null) {
                return CadastrarTrabalhoOutput.erro(
                    "Aluno autor não encontrado com ID: " + input.getAlunoId()
                );
            }

            // PASSO 3: Criar a entidade Trabalho
            // IMPORTANTE: As regras de negócio (validações) estão NA ENTITY!
            Trabalho trabalho = new Trabalho(
                input.getTrabalhoId(),
                input.getTitulo(),
                autor
            );

            // PASSO 4: Persistir o trabalho via Gateway
            trabalhoGateway.salvar(trabalho);

            // PASSO 5: Retornar Output de sucesso
            return CadastrarTrabalhoOutput.sucesso(
                trabalho.getId(),
                trabalho.getTitulo(),
                autor.getNome()
            );

        } catch (IllegalArgumentException e) {
            // Captura erros de validação das Entities
            return CadastrarTrabalhoOutput.erro(e.getMessage());
        } catch (Exception e) {
            // Captura erros inesperados
            return CadastrarTrabalhoOutput.erro(
                "Erro ao cadastrar trabalho: " + e.getMessage()
            );
        }
    }
}
