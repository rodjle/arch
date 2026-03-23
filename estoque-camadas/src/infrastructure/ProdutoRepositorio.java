package infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.Produto;

/*
 * ============================================================
 * CAMADA: INFRASTRUCTURE
 * ============================================================
 *
 * RESPONSABILIDADE:
 *   Simula a persistência de produtos com HashMap em memória.
 *
 * PROBLEMA DIDÁTICO:
 *   Na arquitetura em camadas clássica, a Application depende
 *   diretamente desta implementação concreta.
 */
public class ProdutoRepositorio {

    private HashMap<String, Produto> banco = new HashMap<>();

    public void salvar(Produto produto) {
        banco.put(produto.getId(), produto);
    }

    public Produto buscarPorId(String id) {
        return banco.get(id);
    }

    public void excluirPorId(String id) {
        banco.remove(id);
    }

    public List<Produto> listarTodos() {
        return new ArrayList<>(banco.values());
    }
}