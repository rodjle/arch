package infrastructure;

// Import de HashMap — classe da biblioteca padrão do Java (java.util).
// Usada como "banco de dados em memória" para simular persistência de alunos.
import java.util.HashMap;

// Import de ArrayList — classe da biblioteca padrão do Java (java.util).
// Usada para converter os valores do HashMap em uma lista retornável.
import java.util.ArrayList;

// Import de List — interface da biblioteca padrão do Java (java.util).
// Usada como tipo de retorno do método listarTodos(), programando orientado a interfaces.
import java.util.List;

// Import de Aluno — classe do DOMÍNIO (domain.Aluno).
// A Infraestrutura PODE importar do Domínio (a seta de dependência aponta para baixo).
// A Infraestrutura precisa conhecer as entidades para poder persistí-las.
import domain.Aluno;

/*
 * ============================================================
 * CAMADA: Infraestrutura (Infrastructure)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Simula a persistência de dados de Aluno usando um HashMap em memória.
 *   Em um sistema real, aqui entraria:
 *   - JDBC para acesso direto ao banco de dados
 *   - JPA/Hibernate para mapeamento objeto-relacional
 *   - Chamada a uma API externa (REST, gRPC)
 *   - Leitura/escrita de arquivos
 *
 * POR QUE ESTÁ NA INFRAESTRUTURA?
 *   Porque persistência é um DETALHE TÉCNICO, não uma regra de negócio.
 *   O Domínio define O QUE é um Aluno.
 *   A Infraestrutura define COMO salvar e buscar um Aluno.
 *   Se amanhã trocarmos de HashMap para PostgreSQL, APENAS esta camada muda.
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - application/ (o Service usa o Repositório para orquestrar operações)
 *   - presentation/ (se necessário, mas normalmente via Service)
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - Apenas classes do Domínio (domain/).
 *   - NUNCA importa de application/ ou presentation/.
 *   - A seta: Infrastructure → Domain (permitido)
 *
 * LIMITAÇÃO (importante para entender Hexagonal):
 *   Nesta arquitetura em camadas clássica, o Service depende DIRETAMENTE
 *   desta classe concreta. Na Arquitetura Hexagonal, usaríamos uma
 *   INTERFACE definida no Domínio para inverter essa dependência.
 * ============================================================
 */

public class AlunoRepositorio {

    // "Banco de dados" em memória.
    // HashMap<chave, valor> onde a chave é o ID do aluno e o valor é o objeto Aluno.
    // Em um sistema real, isso seria substituído por uma tabela no banco de dados.
    private HashMap<String, Aluno> banco = new HashMap<>();

    /**
     * Salva um aluno no "banco de dados" (HashMap em memória).
     *
     * O QUE FAZ:
     *   Armazena o aluno usando seu ID como chave no HashMap.
     *   Se já existir um aluno com o mesmo ID, será sobrescrito (comportamento de update).
     *
     * POR QUE ESTÁ NESTA CAMADA E NÃO EM OUTRA?
     *   - Não é regra de negócio → não vai no Domínio
     *   - Não é orquestração → não vai no Service
     *   - Não é exibição → não vai no Controller
     *   - É PERSISTÊNCIA → vai na Infraestrutura
     *
     * @param aluno o aluno a ser salvo
     */
    public void salvar(Aluno aluno) {
        banco.put(aluno.getId(), aluno);
    }

    /**
     * Busca um aluno pelo seu ID.
     *
     * O QUE FAZ:
     *   Procura no HashMap um aluno com o ID fornecido.
     *   Retorna null se não encontrar (em um sistema real, poderíamos lançar exceção).
     *
     * POR QUE ESTÁ NESTA CAMADA E NÃO EM OUTRA?
     *   Buscar dados de um armazenamento é responsabilidade da Infraestrutura.
     *   O Service (Application) vai chamar este método quando precisar de um Aluno.
     *
     * @param id o identificador do aluno desejado
     * @return o Aluno encontrado ou null se não existir
     */
    public Aluno buscarPorId(String id) {
        return banco.get(id);
    }

    /**
     * Lista todos os alunos cadastrados.
     *
     * O QUE FAZ:
     *   Retorna uma lista com todos os alunos armazenados no HashMap.
     *
     * POR QUE ESTÁ NESTA CAMADA E NÃO EM OUTRA?
     *   Listar dados de um armazenamento é responsabilidade da Infraestrutura.
     *   Diferentes implementações poderiam fazer paginação, filtros, etc.
     *
     * @return lista de todos os alunos
     */
    public List<Aluno> listarTodos() {
        return new ArrayList<>(banco.values());
    }
}
