package infrastructure;

// Import de HashMap — classe da biblioteca padrão do Java (java.util).
// Usada como "banco de dados em memória" para simular persistência de trabalhos.
import java.util.HashMap;

// Import de ArrayList — classe da biblioteca padrão do Java (java.util).
// Usada para converter os valores do HashMap em uma lista retornável.
import java.util.ArrayList;

// Import de List — interface da biblioteca padrão do Java (java.util).
// Usada como tipo de retorno do método listarTodos(), programando orientado a interfaces.
import java.util.List;

// Import de Trabalho — classe do DOMÍNIO (domain.Trabalho).
// A Infraestrutura PODE importar do Domínio (a seta de dependência aponta para baixo).
// Precisamos conhecer a entidade para saber o que estamos persistindo.
import domain.Trabalho;

/*
 * ============================================================
 * CAMADA: Infraestrutura (Infrastructure)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Simula a persistência de dados de Trabalho usando um HashMap em memória.
 *   Esta classe é responsável pelo armazenamento dos trabalhos apresentados pelos alunos.
 *
 * POR QUE ESTÁ NA INFRAESTRUTURA?
 *   Persistência é um detalhe técnico, não regra de negócio.
 *   O Domínio define O QUE é um Trabalho.
 *   A Infraestrutura define COMO salvar e buscar um Trabalho.
 *   Se amanhã mudarmos de HashMap para MongoDB, APENAS esta classe muda.
 *   O resto do sistema (Service, Controller, Domain) não é afetado.
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - application/ (o gerenciador usa o Repositório para buscar/salvar trabalhos)
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - Apenas classes do Domínio (domain/).
 *   - NUNCA importa de application/ ou presentation/.
 *   - A seta: Infrastructure → Domain (permitido)
 * ============================================================
 */

public class TrabalhoRepositorio {

    // "Banco de dados" em memória para trabalhos.
    // HashMap<chave, valor> onde a chave é o ID do trabalho.
    private HashMap<String, Trabalho> banco = new HashMap<>();

    /**
     * Salva um trabalho no "banco de dados" (HashMap em memória).
     *
     * O QUE FAZ:
     *   Armazena o trabalho usando seu ID como chave.
     *
     * POR QUE ESTÁ NESTA CAMADA E NÃO EM OUTRA?
     *   Salvar dados é responsabilidade da Infraestrutura.
     *   O HOW (como salvar) fica aqui. O WHAT (o que é um Trabalho) fica no Domínio.
     *
     * @param trabalho o trabalho a ser salvo
     */
    public void salvar(Trabalho trabalho) {
        banco.put(trabalho.getId(), trabalho);
    }

    /**
     * Busca um trabalho pelo seu ID.
     *
     * O QUE FAZ:
     *   Procura no HashMap um trabalho com o ID fornecido.
     *   Retorna null se não encontrar.
     *
     * POR QUE ESTÁ NESTA CAMADA E NÃO EM OUTRA?
     *   Buscar dados de um armazenamento é responsabilidade da Infraestrutura.
     *
     * @param id o identificador do trabalho desejado
     * @return o Trabalho encontrado ou null se não existir
     */
    public Trabalho buscarPorId(String id) {
        return banco.get(id);
    }

    /**
     * Lista todos os trabalhos cadastrados.
     *
     * O QUE FAZ:
     *   Retorna uma lista com todos os trabalhos armazenados.
     *
     * POR QUE ESTÁ NESTA CAMADA E NÃO EM OUTRA?
     *   Listar dados de um armazenamento é responsabilidade da Infraestrutura.
     *
     * @return lista de todos os trabalhos
     */
    public List<Trabalho> listarTodos() {
        return new ArrayList<>(banco.values());
    }
}
