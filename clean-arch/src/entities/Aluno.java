// Arquivo: src/entities/Aluno.java
package entities;

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
 * 🎓 ENTIDADE: ALUNO
 * 
 * Representa um aluno no sistema de avaliação de trabalhos.
 * 
 * REGRAS DE NEGÓCIO ENCAPSULADAS:
 * - O nome do aluno não pode ser vazio ou nulo
 * - Todo aluno tem um identificador único
 * 
 * POR QUE ESTA É UMA ENTITY?
 * - Contém regras de negócio fundamentais (validação de nome)
 * - Representa um conceito de negócio central
 * - Não depende de nenhuma tecnologia ou framework
 * - É imutável e validada na construção
 */
public class Aluno {
    private final String id;
    private final String nome;

    /**
     * Construtor que valida as regras de negócio.
     * 
     * @param id Identificador único do aluno
     * @param nome Nome do aluno (não pode ser vazio)
     * @throws IllegalArgumentException se o nome for inválido
     */
    public Aluno(String id, String nome) {
        // REGRA DE NEGÓCIO: Nome não pode ser vazio ou nulo
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "❌ REGRA DE NEGÓCIO VIOLADA: Nome do aluno não pode ser vazio!"
            );
        }
        
        this.id = id;
        this.nome = nome;
    }

    /**
     * Retorna o ID do aluno.
     * 
     * @return ID único do aluno
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome do aluno.
     * 
     * @return Nome validado do aluno
     */
    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}
