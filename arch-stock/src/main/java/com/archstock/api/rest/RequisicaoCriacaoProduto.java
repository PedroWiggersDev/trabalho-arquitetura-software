package com.archstock.api.rest;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Dados enviados para cadastrar um produto pela API.
 *
 * @param sku código único do produto
 * @param nome nome legível
 * @param quantidade saldo inicial
 * @param nivelReposicao quantidade de referência para reposição
 */
@Schema(name = "RequisicaoCriacaoProduto", description = "Dados necessários para cadastrar um produto.")
public record RequisicaoCriacaoProduto(
        @Schema(description = "SKU único do produto.", example = "MOUSE-001", required = true)
        String sku,
        @Schema(description = "Nome legivel do produto.", example = "Mouse", required = true)
        String nome,
        @Schema(description = "Saldo inicial (zero ou maior).", example = "12", minimum = "0")
        int quantidade,
        @Schema(description = "Nível usado como referência de reposição.", example = "3", minimum = "0")
        int nivelReposicao) {
}
