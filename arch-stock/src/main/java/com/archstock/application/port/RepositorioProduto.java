package com.archstock.application.port;

import com.archstock.domain.Produto;

import java.util.List;
import java.util.Optional;

/**
 * Porta de saída para buscar e armazenar produtos.
 *
 * Implementações concretas ficam nos adaptadores de persistência e podem ser
 * trocadas sem alterar os casos de uso.
 */
public interface RepositorioProduto {
    /**
     * Lista os produtos em ordem alfabética de nome.
     *
     * @return produtos cadastrados
     */
    List<Produto> listarTodos();

    /**
     * Busca um produto pelo identificador.
     *
     * @param id identificador procurado
     * @return produto encontrado ou vazio
     */
    Optional<Produto> buscarPorId(String id);

    /**
     * Busca um produto pelo SKU.
     *
     * @param sku código de identificação
     * @return produto encontrado ou vazio
     */
    Optional<Produto> buscarPorSku(String sku);

    /**
     * Insere ou atualiza um produto.
     *
     * @param produto produto a persistir
     * @return produto persistido
     */
    Produto salvar(Produto produto);
}
