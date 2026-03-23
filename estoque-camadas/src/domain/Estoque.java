package domain;

import java.util.List;

/*
 * ============================================================
 * CAMADA: DOMAIN
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Contém regra de negócio do estoque.
 *   Neste exemplo, calcula o preço médio dos produtos cadastrados.
 */
public class Estoque {

    public double calcularPrecoMedio(List<Produto> produtos) {
        if (produtos == null || produtos.isEmpty()) {
            return 0.0;
        }

        double soma = 0.0;
        for (Produto produto : produtos) {
            soma += produto.getPreco();
        }

        return soma / produtos.size();
    }
}