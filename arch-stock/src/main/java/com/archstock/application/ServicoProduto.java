package com.archstock.application;

import com.archstock.application.exception.ProdutoJaCadastradoException;
import com.archstock.application.exception.ProdutoNaoEncontradoException;
import com.archstock.application.port.PoliticaMovimentacaoEstoque;
import com.archstock.application.port.RepositorioProduto;
import com.archstock.domain.Produto;
import com.archstock.domain.TipoMovimentacao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementa os casos de uso de consulta, cadastro e movimentação.
 *
 * Depende somente de portas, mantendo REST e Hibernate fora das regras
 * da aplicação.
 */
@ApplicationScoped
public class ServicoProduto {
    private final RepositorioProduto produtos;
    private final Instance<PoliticaMovimentacaoEstoque> politicasMovimentacao;

    /**
     * Injeta as portas e políticas CDI disponíveis.
     *
     * @param produtos repositorio de produtos
     * @param politicasMovimentacao políticas de movimentação
     */
    @Inject
    public ServicoProduto(RepositorioProduto produtos,
                          Instance<PoliticaMovimentacaoEstoque> politicasMovimentacao) {
        this.produtos = produtos;
        this.politicasMovimentacao = politicasMovimentacao;
    }

    /**
     * Lista todos os produtos cadastrados.
     *
     * @return produtos em ordem alfabética
     */
    public List<Produto> listarProdutos() {
        return produtos.listarTodos();
    }

    /**
     * Busca um produto ou informa que ele não existe.
     *
     * @param id identificador procurado
     * @return produto localizado
     */
    public Produto obterProduto(String id) {
        return produtos.buscarPorId(id).orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    /**
     * Cadastra um produto com identificador gerado pela aplicação.
     *
     * @param sku codigo unico
     * @param nome nome do produto
     * @param quantidade saldo inicial
     * @param nivelReposicao referência de reposição
     * @return produto criado
     */
    @Transactional
    public Produto cadastrarProduto(String sku, String nome, int quantidade, int nivelReposicao) {
        if (produtos.buscarPorSku(sku).isPresent()) {
            throw new ProdutoJaCadastradoException(sku);
        }
        return produtos.salvar(new Produto(
                UUID.randomUUID().toString(), sku, nome, quantidade, nivelReposicao));
    }

    /**
     * Aplica uma entrada ou saída ao saldo e persiste o resultado.
     *
     * @param id identificador do produto
     * @param tipo tipo de movimentação solicitada
     * @param quantidade quantidade movimentada
     * @return produto atualizado
     */
    @Transactional
    public Produto movimentarEstoque(String id, TipoMovimentacao tipo, int quantidade) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de movimentação é obrigatório");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade movimentada deve ser maior que zero");
        }
        Produto produto = obterProduto(id);
        PoliticaMovimentacaoEstoque politica = politicasMovimentacao.stream()
                .filter(candidata -> candidata.tipo() == tipo)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Não há política registrada para o tipo de movimentação " + tipo));
        return produtos.salvar(politica.aplicar(produto, quantidade));
    }
}
