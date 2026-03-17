package presentation;

/*
 * ============================================================================
 * DIAGRAMA DE DEPENDÊNCIAS (leia de cima para baixo):
 *
 * presentation/  →  application/  →  domain/
 *                                 ↘  infrastructure/  →  domain/
 *
 * REGRA DE OURO: a seta NUNCA aponta para cima.
 * Domain não conhece ninguém.
 * Infrastructure conhece apenas Domain.
 * Application conhece Domain e Infrastructure.
 * Presentation conhece apenas Application.
 *
 * FLUXO DE UMA REQUISIÇÃO (ex: "avaliar um trabalho"):
 *   1. Controller (Presentation) recebe a chamada do usuário
 *   2. Controller chama Gerenciador (Application) passando os dados
 *   3. Gerenciador busca entidades via Repositório (Infrastructure)
 *   4. Gerenciador usa entidades do Domínio para aplicar regras
 *   5. Gerenciador salva o resultado via Repositório (Infrastructure)
 *   6. Controller exibe o resultado para o usuário
 *
 * POR QUE ESSA SEPARAÇÃO É IMPORTANTE?
 *   - Se mudar o banco de dados → só muda Infrastructure
 *   - Se mudar a interface (web → mobile) → só muda Presentation
 *   - Se mudar uma regra de negócio → só muda Domain
 *   - Se mudar o fluxo de um caso de uso → só muda Application
 *
 * LIMITAÇÃO DESTA ARQUITETURA (Camadas Clássica):
 *   - Application depende diretamente da implementação concreta do Repositório.
 *     Isso significa que se quisermos trocar AlunoRepositorio por outro repositório
 *     (ex: AlunoRepositorioPostgres), temos que modificar o Gerenciador ou quem o cria.
 *   - Para resolver isso, usaríamos INTERFACES no Domínio (Ports)
 *     e implementações na Infraestrutura (Adapters) → Hexagonal Architecture.
 *   - Na Clean Architecture de Robert C. Martin, essa inversão é ainda mais
 *     rigorosa, com o uso de Dependency Inversion Principle (DIP).
 * ============================================================================
 */

// Import de GerenciadorAvaliacoes — classe da APLICAÇÃO (application.GerenciadorAvaliacoes).
// O Controller conhece APENAS o Gerenciador. Ele não conhece repositórios nem entidades diretamente.
// Presentation → Application é PERMITIDO (seta aponta para baixo).
// NOTA: Em um cenário didático, importamos também Aluno e os repositórios
// apenas no main() para fazer a montagem inicial (wiring). Quem cria o Gerenciador decide
// quais implementações usar.
import application.GerenciadorAvaliacoes;

// Import de Aluno — classe do DOMÍNIO (domain.Aluno).
// Usado aqui APENAS para criar os dados de teste no main().
// Quem cria o Gerenciador precisa configurar as dependências.
import domain.Aluno;

// Import de AlunoRepositorio — classe da INFRAESTRUTURA (infrastructure.AlunoRepositorio).
// Usado aqui APENAS para criar e configurar o repositório no main() (montagem/wiring).
import infrastructure.AlunoRepositorio;

// Import de TrabalhoRepositorio — classe da INFRAESTRUTURA (infrastructure.TrabalhoRepositorio).
// Usado aqui APENAS para criar e configurar o repositório no main() (montagem/wiring).
import infrastructure.TrabalhoRepositorio;

/*
 * ============================================================
 * CAMADA: Apresentação (Presentation)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Ponto de entrada do sistema. Interface com o mundo exterior.
 *   Em um sistema real com web, aqui ficaria a implementação do Controller HTTP.
 *
 *   Neste exemplo didático, usamos um main() simples para
 *   demonstrar o fluxo completo e simular as "requisições".
 *
 * POR QUE ESTÁ NA CAMADA DE APRESENTAÇÃO?
 *   Porque esta classe lida com a INTERFACE DO SISTEMA:
 *   recebe "entrada do usuário" e exibe "saída para o usuário".
 *   Não tem lógica de negócio (Domínio) nem orquestração (Application).
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - O usuário (via HTTP, terminal, etc.). É o ponto de entrada.
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - application/ (via Gerenciador, para executar casos de uso)
 *   - NUNCA chama domain/ diretamente para regras de negócio
 *   - NUNCA chama infrastructure/ diretamente para persistência
 *   (exceto na montagem/wiring do main(), que é configuração)
 * ============================================================
 */

public class AvaliacaoController {

    /**
     * Método main — ponto de entrada do programa.
     *
     * O QUE FAZ:
     *   Demonstra o fluxo completo do sistema de avaliação de trabalhos,
     *   simulando as "requisições" que viriam de um usuário.
     *
     * POR QUE ESTÁ NESTA CAMADA?
     *   O main() é o ponto de entrada — equivalente a um Controller HTTP.
     *   Ele recebe a "requisição" (simulada) e delega para o Gerenciador.
     *   A única coisa extra que fazemos aqui é a MONTAGEM (wiring) das dependências.
     *
     * NOTA SOBRE OS IMPORTS DE INFRASTRUCTURE NO CONTROLLER:
     *   Aqui fazemos a montagem porque não temos framework para injeção automática.
     *   O objetivo didático é mostrar como as camadas se conectam.
     */
    public static void main(String[] args) {

        System.out.println("=== SISTEMA DE AVALIAÇÃO DE TRABALHOS ===");
        System.out.println("=== Demonstração da Arquitetura em Camadas ===\n");

        // ============================================================
        // PASSO 1: Criar repositórios (CAMADA DE INFRAESTRUTURA)
        // ============================================================
        // Aqui criamos manualmente para demonstrar a montagem.
        AlunoRepositorio alunoRepositorio = new AlunoRepositorio();
        TrabalhoRepositorio trabalhoRepositorio = new TrabalhoRepositorio();

        System.out.println("[INFRA] Repositórios criados (banco em memória)");

        // ============================================================
        // PASSO 2: Criar o Gerenciador (CAMADA DE APLICAÇÃO)
        // ============================================================
        // INJEÇÃO MANUAL DE DEPENDÊNCIA:
        // Passamos os repositórios pelo construtor.
        // O Gerenciador não sabe que os repositórios usam HashMap — ele só os usa.
        // Se quiséssemos trocar para um banco real, mudaríamos apenas aqui.
        GerenciadorAvaliacoes gerenciador = new GerenciadorAvaliacoes(
            trabalhoRepositorio,
            alunoRepositorio
        );

        System.out.println("[APP]   Gerenciador criado com dependências injetadas\n");

        // ============================================================
        // PASSO 3: Cadastrar 3 alunos (simulando requisição HTTP POST)
        // ============================================================
        // Os dados viriam do corpo de uma requisição HTTP.
        // O Controller extrairia os parâmetros e passou para o Gerenciador.
        Aluno alice = new Aluno("a1", "Alice Silva");
        Aluno bob = new Aluno("a2", "Bob Santos");
        Aluno carol = new Aluno("a3", "Carol Oliveira");

        // Salvamos os alunos via repositório 
        alunoRepositorio.salvar(alice);
        alunoRepositorio.salvar(bob);
        alunoRepositorio.salvar(carol);

        System.out.println("[CTRL]  POST /alunos → Cadastrados: Alice, Bob, Carol");

        // ============================================================
        // PASSO 4: Cadastrar 1 trabalho (simulando requisição HTTP POST)
        // ============================================================
        // O Controller chama o Gerenciador (Application), que orquestra a criação.
        // O Controller NÃO cria o Trabalho diretamente — delega para o Gerenciador.
        gerenciador.cadastrarTrabalho("t1", "Arquitetura em Camadas", "a1");

        System.out.println("[CTRL]  POST /trabalhos → 'Arquitetura em Camadas' por Alice");

        // ============================================================
        // PASSO 5: Registrar 3 notas com valores diferentes
        // (simulando requisições HTTP POST)
        // ============================================================
        // Cada chamada ao Gerenciador é como uma requisição HTTP separada.
        // O Controller passa os dados crus; o Gerenciador orquestra; o Domínio valida.
        gerenciador.avaliarTrabalho("n1", "t1", "a2", 8.5);  // Bob dá 8.5
        gerenciador.avaliarTrabalho("n2", "t1", "a3", 9.0);  // Carol dá 9.0
        gerenciador.avaliarTrabalho("n3", "t1", "a1", 7.0);  // Alice dá 7.0

        System.out.println("[CTRL]  POST /avaliacoes → Bob: 8.5, Carol: 9.0, Alice: 7.0");

        // ============================================================
        // PASSO 6: Consultar a média (simulando requisição HTTP GET)
        // ============================================================
        // O Controller pede ao Gerenciador, que busca o Trabalho e
        // DELEGA o cálculo para a entidade de Domínio.
        // O Controller NÃO calcula a média — apenas exibe o resultado.
        double media = gerenciador.consultarMedia("t1");

        System.out.println("\n[CTRL]  GET /trabalhos/t1/media");
        // Simula a "resposta HTTP" com o resultado
        System.out.println("[RESP]  { \"trabalho\": \"Arquitetura em Camadas\", \"media\": " + media + " }");
        System.out.println("\n=== Média esperada: (8.5 + 9.0 + 7.0) / 3 = 8.1666... ===");

        // ============================================================
        // RESUMO DO FLUXO QUE ACABAMOS DE DEMONSTRAR:
        // ============================================================
        System.out.println("\n=== RESUMO DO FLUXO ===");
        System.out.println("Controller (Presentation) → chamou → Gerenciador (Application)");
        System.out.println("Gerenciador (Application) → chamou → Repositório (Infrastructure)");
        System.out.println("Gerenciador (Application) → chamou → Trabalho.calcularMedia() (Domain)");
        System.out.println("A seta NUNCA apontou para cima. Dependência UNIDIRECIONAL mantida.");
    }
}
