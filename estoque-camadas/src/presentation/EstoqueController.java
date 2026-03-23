package presentation;

import application.GerenciadorEstoque;
import domain.Produto;
import infrastructure.ProdutoRepositorio;

/*
 * ============================================================
 * CAMADA: PRESENTATION
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Simula o ponto de entrada do sistema.
 *   Neste exemplo, usa main() para demonstrar os casos de uso.
 */
public class EstoqueController {

    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE ESTOQUE - ARQUITETURA EM CAMADAS ===\n");

        ProdutoRepositorio produtoRepositorio = new ProdutoRepositorio();
        GerenciadorEstoque gerenciador = new GerenciadorEstoque(produtoRepositorio);

        gerenciador.cadastrarProduto("p1", "Teclado Mecânico", 250.00);
        gerenciador.cadastrarProduto("p2", "Mouse Gamer", 180.00);
        gerenciador.cadastrarProduto("p3", "Monitor 24\"", 920.00);

        Produto produto = gerenciador.consultarProduto("p2");
        System.out.println("Produto consultado: " + produto.getNome() + " - R$ " + produto.getPreco());

        double precoMedio = gerenciador.calcularPrecoMedio();
        System.out.println("Preço médio do estoque: R$ " + precoMedio);

        gerenciador.excluirProduto("p1");
        System.out.println("Produto p1 excluído com sucesso.");
    }
}