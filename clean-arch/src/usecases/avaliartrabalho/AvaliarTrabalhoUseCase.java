// Arquivo: src/usecases/avaliartrabalho/AvaliarTrabalhoUseCase.java
package usecases.avaliartrabalho;

import entities.Aluno;
import entities.Nota;
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
 *   Em Hexagonal: GerenciadorAvaliacoes.avaliarTrabalho() (um método)
 *   Em Clean: AvaliarTrabalhoUseCase (classe dedicada)
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
 * 🎓 USE CASE: AVALIAR TRABALHO
 * 
 * Responsável por orquestrar a avaliação de um trabalho por um aluno.
 * 
 * FLUXO DO USE CASE:
 * 1. Recebe Input com os dados da avaliação
 * 2. Valida se o trabalho existe (via Gateway)
 * 3. Valida se o aluno avaliador existe (via Gateway)
 * 4. Cria a entidade Nota (regras de negócio são validadas)
 * 5. Adiciona a nota ao trabalho
 * 6. Persiste o trabalho atualizado (via Gateway)
 * 7. Retorna Output com o resultado e nova média
 * 
 * PRINCÍPIO SINGLE RESPONSIBILITY:
 * - Este Use Case faz UMA coisa: avaliar trabalho
 * - Não mistura com cadastrar ou consultar média
 * - Cada operação tem sua própria classe
 */
public class AvaliarTrabalhoUseCase {
    
    private final ITrabalhoGateway trabalhoGateway;
    private final IAlunoGateway alunoGateway;

    /**
     * Construtor com injeção de dependências.
     * 
     * @param trabalhoGateway Gateway para persistência de trabalhos
     * @param alunoGateway Gateway para buscar alunos
     */
    public AvaliarTrabalhoUseCase(ITrabalhoGateway trabalhoGateway, IAlunoGateway alunoGateway) {
        this.trabalhoGateway = trabalhoGateway;
        this.alunoGateway = alunoGateway;
    }

    /**
     * Executa o Use Case de avaliar trabalho.
     * 
     * ORQUESTRAÇÃO:
     * - Coordena o fluxo entre Gateways e Entities
     * - NÃO contém regras de negócio (essas estão nas Entities)
     * - Busca trabalho, busca avaliador, cria nota, adiciona, salva
     * 
     * @param input Dados de entrada (DTO)
     * @return Resultado da operação (DTO)
     */
    public AvaliarTrabalhoOutput executar(AvaliarTrabalhoInput input) {
        try {
            // PASSO 1: Buscar o trabalho via Gateway
            Trabalho trabalho = trabalhoGateway.buscarPorId(input.getTrabalhoId());
            if (trabalho == null) {
                return AvaliarTrabalhoOutput.erro(
                    "Trabalho não encontrado com ID: " + input.getTrabalhoId()
                );
            }

            // PASSO 2: Buscar o aluno avaliador via Gateway
            Aluno avaliador = alunoGateway.buscarPorId(input.getAlunoAvaliadorId());
            if (avaliador == null) {
                return AvaliarTrabalhoOutput.erro(
                    "Aluno avaliador não encontrado com ID: " + input.getAlunoAvaliadorId()
                );
            }

            // PASSO 3: Criar a entidade Nota
            // IMPORTANTE: As regras de negócio (nota entre 0 e 10) estão NA ENTITY!
            Nota nota = new Nota(avaliador.getId(), input.getNota());

            // PASSO 4: Adicionar a nota ao trabalho
            trabalho.adicionarNota(nota);

            // PASSO 5: Persistir o trabalho atualizado via Gateway
            trabalhoGateway.salvar(trabalho);

            // PASSO 6: Calcular a nova média
            // IMPORTANTE: A lógica de cálculo de média está NA ENTITY!
            double novaMedia = trabalho.calcularMedia();

            // PASSO 7: Retornar Output de sucesso
            return AvaliarTrabalhoOutput.sucesso(
                trabalho.getId(),
                trabalho.getTitulo(),
                avaliador.getNome(),
                input.getNota(),
                novaMedia,
                trabalho.getQuantidadeAvaliacoes()
            );

        } catch (IllegalArgumentException e) {
            // Captura erros de validação das Entities (ex: nota inválida)
            return AvaliarTrabalhoOutput.erro(e.getMessage());
        } catch (Exception e) {
            // Captura erros inesperados
            return AvaliarTrabalhoOutput.erro(
                "Erro ao avaliar trabalho: " + e.getMessage()
            );
        }
    }
}
