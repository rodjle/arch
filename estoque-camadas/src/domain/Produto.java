package domain;

/*
 * ============================================================
 * CAMADA: DOMAIN
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Representa um produto do estoque.
 *   Contém os dados essenciais e validações básicas do negócio.
 */
public class Produto {

    private String id;
    private String nome;
    private double preco;

    public Produto(String id, String nome, double preco) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID do produto é obrigatório.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("Preço do produto não pode ser negativo.");
        }

        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }
}