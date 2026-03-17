// Arquivo: src/domain/model/Trabalho.java
package domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * ============================================================
 * CAMADA: DOMAIN - MODEL (Núcleo Hexagonal)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Representa um trabalho apresentado por um aluno.
 *   Agrega as notas recebidas e calcula a média.
 *   REGRA DE NEGÓCIO: média = soma das notas ÷ quantidade
 *
 * POR QUE ESTÁ NO DOMAIN?
 *   - "Trabalho" é um conceito central do negócio
 *   - A fórmula de cálculo da média é uma REGRA DE NEGÓCIO
 *   - A agregação de notas é um comportamento do domínio
 *
 * QUEM PODE CHAMAR ESTA CLASSE?
 *   - Application Layer (GerenciadorAvaliacoes)
 *   - Adapters (Repositórios, Controllers para exibição)
 *
 * QUEM ESTA CLASSE PODE CHAMAR?
 *   - Classe Nota (outra entidade do domínio)
 *   - Classes utilitárias do Java (List, Collections)
 *   NÃO chama nada de Infrastructure ou Adapters
 *
 * HEXAGONAL ARCHITECTURE:
 *   Esta classe está no NÚCLEO DO HEXÁGONO.
 *   É um AGGREGATE ROOT (raiz de agregação):
 *   - Gerencia um conjunto de Notas
 *   - Expõe comportamento de negócio (calcularMedia)
 *   - Mantém invariantes (notas sempre válidas)
 *   
 * DIFERENÇA DA ARQUITETURA EM CAMADAS:
 *   Funcionalidade idêntica à versão em camadas, mas com
 *   GARANTIA ARQUITETURAL de que esta classe nunca dependerá
 *   de detalhes técnicos externos. Na Hexagonal, o Domain
 *   é o centro imutável do sistema, protegido por Ports.
 * ============================================================
 */

/**
 * Representa um trabalho acadêmico que pode receber avaliações.
 * 
 * COMPORTAMENTOS:
 * - Adicionar nota de avaliação
 * - Calcular média das notas recebidas
 * - Listar todas as notas
 */
public class Trabalho {
    
    private final String id;
    private final String titulo;
    private final String autorId;
    private final List<Nota> notas;
    
    /**
     * Construtor da entidade Trabalho.
     * 
     * @param id Identificador único do trabalho
     * @param titulo Título do trabalho
     * @param autorId ID do aluno autor
     * 
     * POR QUE INICIALIZAMOS A LISTA AQUI?
     * A lista de notas é parte do ESTADO INTERNO do agregado Trabalho.
     * Inicializá-la vazia garante que a entidade sempre está em um
     * estado válido, mesmo sem avaliações. Isso é uma prática de
     * Domain-Driven Design (DDD), muito usada em Arquitetura Hexagonal.
     */
    public Trabalho(String id, String titulo, String autorId) {
        this.id = id;
        this.titulo = titulo;
        this.autorId = autorId;
        this.notas = new ArrayList<>();
    }
    
    /**
     * Adiciona uma avaliação ao trabalho.
     * 
     * @param nota Nota a ser adicionada
     * 
     * POR QUE ESTE MÉTODO ESTÁ AQUI?
     * "Adicionar nota a um trabalho" é uma OPERAÇÃO DE NEGÓCIO.
     * O Domain é o lugar certo para operações que modificam
     * entidades de negócio, pois aqui podemos garantir que
     * todas as regras sejam respeitadas.
     * 
     * CONTEXTO HEXAGONAL:
     * Este método será chamado pela Application Layer
     * (GerenciadorAvaliacoes), que por sua vez é invocado por
     * um Driving Adapter (Controller). A sequência é:
     * Adapter → Port → Application → Domain Entity
     */
    public void adicionarNota(Nota nota) {
        this.notas.add(nota);
    }
    
    /**
     * Calcula a média aritmética das notas recebidas.
     * 
     * @return Média das notas, ou 0.0 se não houver avaliações
     * 
     * POR QUE ESTE MÉTODO ESTÁ AQUI?
     * O cálculo da média é uma REGRA DE NEGÓCIO fundamental:
     * "A nota final de um trabalho é a média das avaliações recebidas."
     * 
     * Esta regra NÃO pode estar em:
     * - Controller (camada de apresentação - seria duplicação se tivéssemos múltiplos controllers)
     * - Repositório (camada de persistência - não é responsabilidade do DB calcular isso)
     * - Application (seria possível, mas quebraria a coesão do Domain)
     * 
     * Deve estar AQUI porque:
     * - É comportamento intrínseco de um Trabalho
     * - Precisa acessar o estado interno (lista de notas)
     * - É uma regra que vale independente de tecnologia usada
     * 
     * CONTEXTO HEXAGONAL:
     * Este é um exemplo perfeito de lógica de domínio puro.
     * Não importa se usamos REST API, GraphQL, ou Console:
     * o cálculo da média é o mesmo. Não importa se salvamos em
     * PostgreSQL, MongoDB, ou memória: o cálculo é o mesmo.
     * Por isso está no núcleo do hexágono, isolado de Adapters.
     */
    public double calcularMedia() {
        if (notas.isEmpty()) {
            return 0.0;
        }
        
        double soma = 0.0;
        for (Nota nota : notas) {
            soma += nota.getValor();
        }
        
        return soma / notas.size();
    }
    
    /**
     * Retorna o ID único do trabalho.
     * 
     * @return ID do trabalho
     */
    public String getId() {
        return id;
    }
    
    /**
     * Retorna o título do trabalho.
     * 
     * @return Título do trabalho
     */
    public String getTitulo() {
        return titulo;
    }
    
    /**
     * Retorna o ID do autor do trabalho.
     * 
     * @return ID do aluno autor
     */
    public String getAutorId() {
        return autorId;
    }
    
    /**
     * Retorna uma cópia imutável da lista de notas.
     * 
     * @return Lista somente-leitura das notas recebidas
     * 
     * POR QUE RETORNAMOS UMA CÓPIA IMUTÁVEL?
     * Isso protege o ENCAPSULAMENTO do agregado. Se retornássemos
     * a lista original, código externo poderia modificá-la diretamente,
     * burlando regras de negócio. Com Collections.unmodifiableList(),
     * garantimos que só podemos adicionar notas via adicionarNota(),
     * onde podemos validar regras.
     * 
     * Este é um padrão de DEFENSIVE PROGRAMMING muito usado em DDD
     * e Arquitetura Hexagonal.
     */
    public List<Nota> getNotas() {
        return Collections.unmodifiableList(notas);
    }
    
    /**
     * Representação textual do trabalho para debug/logging.
     * 
     * @return String com informações do trabalho e suas notas
     */
    @Override
    public String toString() {
        return String.format(
            "Trabalho{id='%s', titulo='%s', autor='%s', notas=%d, média=%.2f}",
            id, titulo, autorId, notas.size(), calcularMedia()
        );
    }
}
