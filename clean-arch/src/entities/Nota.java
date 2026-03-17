// Arquivo: src/entities/Nota.java
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
 * 🎓 ENTIDADE: NOTA
 * 
 * Representa uma avaliação dada por um aluno a um trabalho.
 * 
 * REGRAS DE NEGÓCIO ENCAPSULADAS:
 * - A nota deve estar entre 0 e 10 (inclusive)
 * - Toda avaliação tem um avaliador e um valor
 * 
 * POR QUE ESTA É UMA ENTITY?
 * - Contém regras de negócio fundamentais (validação da nota)
 * - Representa um conceito de negócio central
 * - Não depende de nenhuma tecnologia ou framework
 * - É imutável e validada na construção
 * 
 * NOTA SOBRE IMUTABILIDADE:
 * - Entities em Clean Architecture são geralmente imutáveis
 * - Uma vez criadas, não podem ser modificadas
 * - Para mudar, cria-se uma nova instância
 * - Isso facilita testes e reduz bugs
 */
public class Nota {
    private final String alunoAvaliadorId;
    private final double valor;

    // Constantes que definem as regras de negócio
    private static final double NOTA_MINIMA = 0.0;
    private static final double NOTA_MAXIMA = 10.0;

    /**
     * Construtor que valida as regras de negócio.
     * 
     * @param alunoAvaliadorId ID do aluno que está avaliando
     * @param valor Valor da nota (deve estar entre 0 e 10)
     * @throws IllegalArgumentException se a nota for inválida
     */
    public Nota(String alunoAvaliadorId, double valor) {
        // REGRA DE NEGÓCIO: Nota deve estar entre 0 e 10
        if (valor < NOTA_MINIMA || valor > NOTA_MAXIMA) {
            throw new IllegalArgumentException(
                String.format(
                    "❌ REGRA DE NEGÓCIO VIOLADA: Nota deve estar entre %.1f e %.1f, mas foi %.1f",
                    NOTA_MINIMA, NOTA_MAXIMA, valor
                )
            );
        }
        
        this.alunoAvaliadorId = alunoAvaliadorId;
        this.valor = valor;
    }

    /**
     * Retorna o ID do aluno avaliador.
     * 
     * @return ID do aluno que deu a nota
     */
    public String getAlunoAvaliadorId() {
        return alunoAvaliadorId;
    }

    /**
     * Retorna o valor da nota.
     * 
     * @return Valor validado da nota (entre 0 e 10)
     */
    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "avaliadorId='" + alunoAvaliadorId + '\'' +
                ", valor=" + valor +
                '}';
    }
}
