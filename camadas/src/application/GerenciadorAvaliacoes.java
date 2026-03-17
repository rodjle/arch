package application;

// Import de Aluno — classe do DOMÍNIO (domain.Aluno).
// O gerenciador precisa conhecer a entidade Aluno para buscá-la e usá-la na criação de trabalhos.
// Application → Domain é PERMITIDO (seta aponta para baixo).
import domain.Aluno;

// Import de Trabalho — classe do DOMÍNIO (domain.Trabalho).
// O gerenciador precisa criar e manipular trabalhos como parte da orquestração.
// Application → Domain é PERMITIDO.
import domain.Trabalho;

// Import de Nota — classe do DOMÍNIO (domain.Nota).
// O gerenciador precisa criar notas para adicioná-las aos trabalhos.
// Application → Domain é PERMITIDO.
import domain.Nota;

// Import de AlunoRepositorio — classe da INFRAESTRUTURA (infrastructure.AlunoRepositorio).
// O gerenciador precisa do repositório para buscar e salvar alunos.
// Application → Infrastructure é PERMITIDO na arquitetura em camadas clássica.
// NOTA: Na Hexagonal Architecture, usaríamos uma INTERFACE no Domínio em vez disso.
import infrastructure.AlunoRepositorio;

// Import de TrabalhoRepositorio — classe da INFRAESTRUTURA (infrastructure.TrabalhoRepositorio).
// O gerenciador precisa do repositório para buscar e salvar trabalhos.
// Application → Infrastructure é PERMITIDO.
import infrastructure.TrabalhoRepositorio;

/*
 * ============================================================
 * CAMADA: Aplicação (Application)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Gerencia e orquestra as operações do sistema de avaliação de trabalhos.
 *   É o "maestro" da orquestra:
 *   - Não toca nenhum instrumento (não tem regras de negócio — isso é do Domínio)
 *   - Não vende os ingressos (não sabe como exibir dados — isso é da Apresentação)
 *   - Apenas coordena quem faz o quê e em qual ordem.
 *
 * POR QUE ESTA CLASSE NÃO É UM "SERVICE"?
 *   Para evitar confusão com a camada "Application Service".
 *   "Gerenciador" deixa claro que é uma classe que COORDENA, sem confundir com a camada.
 *
 * POR QUE ESTÁ NA CAMADA DE APLICAÇÃO?
 *   Porque esta classe representa CASOS DE USO do sistema:
 *   "cadastrar trabalho", "avaliar trabalho", "consultar média".
 *   Cada método é um caso de uso que coordena chamadas ao Domínio e Infraestrutura.
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - presentation/ (o Controller chama o Gerenciador para executar os casos de uso)
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - domain/ (usa as entidades e suas regras de negócio)
 *   - infrastructure/ (usa os repositórios para persistência)
 *   - NUNCA importa de presentation/
 *   - A seta: Application → Domain (permitido)
 *   - A seta: Application → Infrastructure (permitido)
 * ============================================================
 */

public class GerenciadorAvaliacoes {

    // Repositório de trabalhos — dependência da camada de Infraestrutura.
    // O Gerenciador não sabe (nem se importa) se os dados estão em memória, banco ou API.
    private TrabalhoRepositorio trabalhoRepositorio;

    // Repositório de alunos — dependência da camada de Infraestrutura.
    private AlunoRepositorio alunoRepositorio;

    /**
     * Construtor do Gerenciador com INJEÇÃO MANUAL DE DEPENDÊNCIA.
     *
     * O QUE FAZ:
     *   Recebe as dependências (repositórios) pelo construtor em vez de criá-las internamente.
     *
     * POR QUE ESTÁ NESTA CAMADA?
     *   A montagem (wiring) das dependências é responsabilidade da camada de Aplicação.
     *   O Gerenciador precisa dos repositórios para orquestrar, mas não deve criar instâncias
     *   concretas — quem decide qual repositório usar é quem cria o Gerenciador.
     *
     * POR QUE INJEÇÃO DE DEPENDÊNCIA?
     *   - Passamos as dependências pelo construtor em vez de criar dentro da classe.
     *   - Isso permite trocar o repositório facilmente (ex: usar outro para testes).
     *   - Quem cria o Gerenciador decide quais implementações usar.
     *
     * @param trabalhoRepositorio repositório para persistir trabalhos
     * @param alunoRepositorio    repositório para persistir/buscar alunos
     */
    public GerenciadorAvaliacoes(TrabalhoRepositorio trabalhoRepositorio,
                                  AlunoRepositorio alunoRepositorio) {
        this.trabalhoRepositorio = trabalhoRepositorio;
        this.alunoRepositorio = alunoRepositorio;
    }

    /**
     * Caso de uso: Cadastrar um novo trabalho.
     *
     * O QUE FAZ:
     *   1. Busca o aluno autor pelo ID (via Infraestrutura)
     *   2. Cria a entidade Trabalho (Domínio)
     *   3. Salva o trabalho (via Infraestrutura)
     *   É ORQUESTRAÇÃO PURA — não tem lógica de negócio aqui.
     *
     * POR QUE ESTÁ NESTA CAMADA E NÃO EM OUTRA?
     *   - Não é regra de negócio → não vai no Domínio
     *   - Não é persistência → não vai na Infraestrutura
     *   - Não é exibição → não vai no Controller
     *   - É um CASO DE USO (coordenar busca + criação + salvamento) → vai no Gerenciador
     *
     * @param id       identificador do trabalho
     * @param titulo   título do trabalho
     * @param alunoId  ID do aluno que apresentou o trabalho
     */
    public void cadastrarTrabalho(String id, String titulo, String alunoId) {
        // Passo 1: Buscar o aluno autor no repositório (INFRAESTRUTURA)
        Aluno autor = alunoRepositorio.buscarPorId(alunoId);

        // Passo 2: Criar a entidade Trabalho (DOMÍNIO)
        // A validação interna (se houver) fica dentro da entidade.
        Trabalho trabalho = new Trabalho(id, titulo, autor);

        // Passo 3: Salvar no repositório (INFRAESTRUTURA)
        trabalhoRepositorio.salvar(trabalho);
    }

    /**
     * Caso de uso: Avaliar um trabalho (dar uma nota).
     *
     * O QUE FAZ:
     *   1. Busca o trabalho pelo ID (via Infraestrutura)
     *   2. Busca o aluno avaliador pelo ID (via Infraestrutura)
     *   3. Cria a entidade Nota com o valor (Domínio — valida valor 0-10)
     *   4. Adiciona a nota ao trabalho (Domínio)
     *   Novamente, ORQUESTRAÇÃO PURA.
     *
     * POR QUE ESTÁ NESTA CAMADA E NÃO EM OUTRA?
     *   É um caso de uso: coordena múltiplas operações em sequência.
     *   A validação do valor (0-10) é feita DENTRO da entidade Nota (Domínio),
     *   não aqui no Gerenciador. O Gerenciador apenas monta as peças.
     *
     * @param notaId       ID da nota
     * @param trabalhoId   ID do trabalho sendo avaliado
     * @param avaliadorId  ID do aluno que está dando a nota
     * @param valor        valor da nota (será validado pela entidade Nota)
     */
    public void avaliarTrabalho(String notaId, String trabalhoId,
                                 String avaliadorId, double valor) {
        // Passo 1: Buscar o trabalho (INFRAESTRUTURA)
        Trabalho trabalho = trabalhoRepositorio.buscarPorId(trabalhoId);

        // Passo 2: Buscar o aluno avaliador (INFRAESTRUTURA)
        Aluno avaliador = alunoRepositorio.buscarPorId(avaliadorId);

        // Passo 3: Criar a nota (DOMÍNIO)
        // A validação de valor entre 0 e 10 acontece AQUI, dentro do construtor de Nota.
        // O Gerenciador não precisa saber dessa regra — quem sabe é o Domínio.
        Nota nota = new Nota(notaId, avaliador, valor);

        // Passo 4: Adicionar a nota ao trabalho (DOMÍNIO)
        trabalho.adicionarNota(nota);
    }

    /**
     * Caso de uso: Consultar a média de notas de um trabalho.
     *
     * O QUE FAZ:
     *   1. Busca o trabalho pelo ID (via Infraestrutura)
     *   2. DELEGA o cálculo da média para a entidade Trabalho (Domínio)
     *
     * IMPORTANTE:
     *   O GERENCIADOR NÃO CALCULA A MÉDIA.
     *   Quem calcula é a entidade Trabalho (Domínio).
     *   O Gerenciador apenas ORQUESTRA: busca o dado e pede para a entidade calcular.
     *   Isso é fundamental para manter as regras de negócio no Domínio.
     *
     * POR QUE ESTÁ NESTA CAMADA E NÃO EM OUTRA?
     *   Porque é um caso de uso: "consultar média".
     *   Se o cálculo estivesse aqui no Gerenciador, teríamos um "Domínio Anêmico"
     *   (entidades sem comportamento, apenas com getters/setters).
     *
     * @param trabalhoId ID do trabalho
     * @return a média das notas calculada pela entidade de Domínio
     */
    public double consultarMedia(String trabalhoId) {
        // Passo 1: Buscar o trabalho (INFRAESTRUTURA)
        Trabalho trabalho = trabalhoRepositorio.buscarPorId(trabalhoId);

        // Passo 2: DELEGAR o cálculo para o DOMÍNIO
        // O método calcularMedia() está na entidade Trabalho porque é regra de negócio.
        // O Gerenciador não sabe e não precisa saber COMO a média é calculada.
        return trabalho.calcularMedia();
    }
}
