package application;

import java.util.List;

import domain.Estoque;
import domain.Produto;
import infrastructure.ProdutoRepositorio;

/*
 * ============================================================
 * CAMADA: APPLICATION
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Orquestra os casos de uso do sistema de estoque.
 *
 * PROBLEMA DIDÁTICO:
 *   Esta classe depende diretamente de ProdutoRepositorio,
 *   uma classe concreta da infraestrutura.
 */
public class GerenciadorEstoque {

    private ProdutoRepositorio produtoRepositorio;
    private Estoque estoque;

    public GerenciadorEstoque(ProdutoRepositorio produtoRepositorio) {
        this.produtoRepositorio = produtoRepositorio;
        this.estoque = new Estoque();
    }

    public void cadastrarProduto(String id, String nome, double preco) {
        Produto produto = new Produto(id, nome, preco);
        produtoRepositorio.salvar(produto);
    }

    public Produto consultarProduto(String id) {
        return produtoRepositorio.buscarPorId(id);
    }

    public void excluirProduto(String id) {
        Produto produto = produtoRepositorio.buscarPorId(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto com ID '" + id + "' não encontrado.");
        }

        produtoRepositorio.excluirPorId(id);
    }

    public double calcularPrecoMedio() {
        List<Produto> produtos = produtoRepositorio.listarTodos();
        return estoque.calcularPrecoMedio(produtos);
    }
}