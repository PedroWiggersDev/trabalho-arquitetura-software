package com.archstock.adapter.out.persistence;

import com.archstock.domain.Produto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Representação JPA do produto, isolada no adaptador Hibernate.
 */
@Entity
@Table(name = "produtos")
public class EntidadeProduto {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private int quantidade;

    @Column(name = "nivel_reposicao", nullable = false)
    private int nivelReposicao;

    /**
     * Construtor exigido pelo JPA.
     */
    protected EntidadeProduto() {
    }

    private EntidadeProduto(Produto produto) {
        id = produto.id();
        sku = produto.sku();
        nome = produto.nome();
        quantidade = produto.quantidade();
        nivelReposicao = produto.nivelReposicao();
    }

    /**
     * Converte o modelo de domínio em uma entidade persistível.
     *
     * @param produto modelo de dominio
     * @return entidade JPA correspondente
     */
    public static EntidadeProduto deDominio(Produto produto) {
        return new EntidadeProduto(produto);
    }

    /**
     * Converte a entidade JPA para o modelo de domínio.
     *
     * @return produto independente de persistência
     */
    public Produto paraDominio() {
        return new Produto(id, sku, nome, quantidade, nivelReposicao);
    }
}
