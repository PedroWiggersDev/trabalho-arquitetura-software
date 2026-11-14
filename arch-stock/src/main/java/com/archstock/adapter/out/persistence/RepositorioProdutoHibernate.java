package com.archstock.adapter.out.persistence;

import com.archstock.application.port.RepositorioProduto;
import com.archstock.domain.Produto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import io.quarkus.runtime.StartupEvent;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de saída que implementa a porta de produtos usando Hibernate ORM.
 *
 * Para trocar a tecnologia de armazenamento, substitua esta implementação da
 * porta por outro bean CDI sem alterar os casos de uso nem o contrato REST.
 */
@ApplicationScoped
public class RepositorioProdutoHibernate implements RepositorioProduto {
    @PersistenceContext
    EntityManager gerenciadorEntidades;

    /**
     * Insere produtos demonstrativos somente quando o banco ainda está vazio.
     *
     * @param evento evento de inicializacao do Quarkus
     */
    @Transactional
    void carregarProdutosDemonstracao(@Observes StartupEvent evento) {
        Long total = gerenciadorEntidades.createQuery(
                "select count(p) from EntidadeProduto p", Long.class).getSingleResult();
        if (total == 0) {
            gerenciadorEntidades.persist(EntidadeProduto.deDominio(
                    new Produto("demo-1", "CAD-001", "Caderno", 20, 5)));
            gerenciadorEntidades.persist(EntidadeProduto.deDominio(
                    new Produto("demo-2", "CAN-001", "Caneta azul", 50, 10)));
        }
    }

    /**
     * Lista os produtos persistidos ordenados pelo nome.
     *
     * @return produtos armazenados
     */
    @Override
    @Transactional
    public List<Produto> listarTodos() {
        return gerenciadorEntidades.createQuery(
                        "select p from EntidadeProduto p order by p.nome", EntidadeProduto.class)
                .getResultList().stream()
                .map(EntidadeProduto::paraDominio)
                .toList();
    }

    /**
     * Procura um produto pela chave primária.
     *
     * @param id identificador do produto
     * @return produto encontrado ou vazio
     */
    @Override
    @Transactional
    public Optional<Produto> buscarPorId(String id) {
        return Optional.ofNullable(gerenciadorEntidades.find(EntidadeProduto.class, id))
                .map(EntidadeProduto::paraDominio);
    }

    /**
     * Procura um produto pelo SKU único.
     *
     * @param sku código do produto
     * @return produto encontrado ou vazio
     */
    @Override
    @Transactional
    public Optional<Produto> buscarPorSku(String sku) {
        return gerenciadorEntidades.createQuery(
                        "select p from EntidadeProduto p where p.sku = :sku", EntidadeProduto.class)
                .setParameter("sku", sku)
                .getResultStream()
                .findFirst()
                .map(EntidadeProduto::paraDominio);
    }

    /**
     * Insere ou atualiza um produto.
     *
     * @param produto modelo de dominio a persistir
     * @return produto persistido
     */
    @Override
    @Transactional
    public Produto salvar(Produto produto) {
        EntidadeProduto entidade = EntidadeProduto.deDominio(produto);
        if (gerenciadorEntidades.find(EntidadeProduto.class, produto.id()) == null) {
            gerenciadorEntidades.persist(entidade);
        } else {
            gerenciadorEntidades.merge(entidade);
        }
        return produto;
    }
}
