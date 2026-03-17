// Arquivo: src/entities/Trabalho.java
package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * ============================================================
 * CLEAN ARCHITECTURE - CAMADA: ENTITIES (Círculo Mais Interno)
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Encapsula as regras de negócio mais críticas e fundamentais.
 *   Enterprise Business Rules - regras que existiriam mesmo sem sistema.
 *
 * POSIÇÃO NA CLEAN ARCHITECTURE:
 *   ● ← Você está aqui (centro do círculo)
 *   ○ Use Cases
 *   ○ Interface Adapters
 *   ○ Frameworks & Drivers
 *
 * QUEM PODE DEPENDER DESTA CAMADA?
 *   TODOS! Esta é a camada mais estável.
 *   Use Cases, Adapters, Frameworks podem usar Entities.
 *
 * QUEM ESTA CAMADA PODE DEPENDER?
 *   NINGUÉM! Entities não dependem de nada.
 *   Nem de Use Cases, nem de bibliotecas, nem de frameworks.
 *
 * DIFERENÇA DE HEXAGONAL:
 *   Em Hexagonal, chamávamos de "Domain/Model".
 *   Em Clean, chamamos de "Entities" (regras enterprise).
 *   Conceito similar, mas Clean é mais explícito sobre a independência total.
 * ============================================================
 */

/**
 * 🎓 ENTIDADE: TRABALHO
 * 
 * Representa um trabalho acadêmico apresentado por um aluno.
 * 
 * REGRAS DE NEGÓCIO ENCAPSULADAS:
 * - Todo trabalho tem um título e um autor
 * - Um trabalho pode receber múltiplas avaliações (notas)
 * - A média é calculada somando as notas e dividindo pela quantidade
 * - Trabalho sem avaliações tem média 0.0
 * 
 * POR QUE ESTA É UMA ENTITY?
 * - Contém regras de negócio fundamentais (cálculo de média)
 * - É um agregado (contém uma lista de Notas)
 * - Representa um conceito de negócio central
 * - Não depende de nenhuma tecnologia ou framework
 * 
 * PADRÃO AGGREGATE ROOT (DDD):
 * - Trabalho é a "raiz do agregado"
 * - As Notas pertencem ao Trabalho
 * - Modificações nas Notas passam pelo Trabalho
 */
public class Trabalho {
    private final String id;
    private final String titulo;
    private final Aluno autor;
    private final List<Nota> notas;

    /**
     * Construtor para criar um novo trabalho sem notas.
     * 
     * @param id Identificador único do trabalho
     * @param titulo Título do trabalho
     * @param autor Aluno autor do trabalho
     */
    public Trabalho(String id, String titulo, Aluno autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.notas = new ArrayList<>();
    }

    /**
     * Adiciona uma avaliação (nota) ao trabalho.
     * 
     * REGRA DE NEGÓCIO: Este método permite que o trabalho receba avaliações.
     * 
     * @param nota Nota a ser adicionada
     */
    public void adicionarNota(Nota nota) {
        this.notas.add(nota);
    }

    /**
     * Calcula a média das notas recebidas.
     * 
     * REGRA DE NEGÓCIO FUNDAMENTAL:
     * - Soma todas as notas
     * - Divide pela quantidade de avaliações
     * - Se não houver notas, retorna 0.0
     * 
     * Esta lógica está na Entity porque é uma regra de negócio fundamental
     * que existiria independentemente de como o sistema é implementado.
     * 
     * @return Média aritmética das notas (0.0 se não houver avaliações)
     */
    public double calcularMedia() {
        if (notas.isEmpty()) {
            return 0.0;
        }
        
        // Soma todas as notas
        double soma = 0.0;
        for (Nota nota : notas) {
            soma += nota.getValor();
        }
        
        // REGRA DE NEGÓCIO: Média = Soma ÷ Quantidade
        return soma / notas.size();
    }

    /**
     * Retorna o ID do trabalho.
     * 
     * @return ID único do trabalho
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
     * Retorna o autor do trabalho.
     * 
     * @return Aluno autor do trabalho
     */
    public Aluno getAutor() {
        return autor;
    }

    /**
     * Retorna a lista de notas (imutável para proteger o encapsulamento).
     * 
     * NOTA SOBRE ENCAPSULAMENTO:
     * - Retornamos uma cópia não modificável da lista
     * - Isso evita que código externo modifique diretamente a lista
     * - Para adicionar notas, deve-se usar adicionarNota()
     * 
     * @return Lista imutável de notas
     */
    public List<Nota> getNotas() {
        return Collections.unmodifiableList(notas);
    }

    /**
     * Retorna a quantidade de avaliações recebidas.
     * 
     * @return Número de avaliações
     */
    public int getQuantidadeAvaliacoes() {
        return notas.size();
    }

    @Override
    public String toString() {
        return "Trabalho{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", autor=" + autor.getNome() +
                ", avaliacoes=" + notas.size() +
                ", media=" + String.format("%.2f", calcularMedia()) +
                '}';
    }
}
