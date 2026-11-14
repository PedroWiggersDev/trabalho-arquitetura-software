package com.archstock.domain;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Representa um produto no domínio, sem depender de REST ou persistência.
 *
 * @param id identificador único
 * @param sku código único de identificação
 * @param nome nome legível
 * @param quantidade saldo disponível
 * @param nivelReposicao quantidade de referência para reposição
 */
@Schema(name = "Produto", description = "Produto cadastrado e seu saldo atual.")
public record Produto(
        @Schema(description = "Identificador único do produto.", example = "demo-1")
        String id,
        @Schema(description = "SKU único do produto.", example = "CAD-001")
        String sku,
        @Schema(description = "Nome legivel do produto.", example = "Caderno")
        String nome,
        @Schema(description = "Quantidade disponível em estoque.", example = "20")
        int quantidade,
        @Schema(description = "Quantidade de referência para reposição.", example = "5")
        int nivelReposicao) {
    /**
     * Valida as invariantes básicas do produto.
     */
    public Produto {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("O identificador do produto é obrigatório");
        }
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("O SKU é obrigatório");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório");
        }
        if (quantidade < 0 || nivelReposicao < 0) {
            throw new IllegalArgumentException("Quantidade e nível de reposição não podem ser negativos");
        }
    }

    /**
     * Cria uma cópia com uma nova quantidade.
     *
     * @param novaQuantidade novo saldo
     * @return produto atualizado
     */
    public Produto comQuantidade(int novaQuantidade) {
        return new Produto(id, sku, nome, novaQuantidade, nivelReposicao);
    }
}
