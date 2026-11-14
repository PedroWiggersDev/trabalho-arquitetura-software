package com.archstock.adapter.in.rest;

import com.archstock.domain.TipoMovimentacao;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Dados enviados para registrar uma movimentação de estoque.
 *
 * @param tipo tipo ENTRADA ou SAIDA
 * @param quantidade quantidade positiva movimentada
 */
@Schema(name = "RequisicaoMovimentacaoEstoque",
        description = "Tipo ENTRADA ou SAÍDA e quantidade positiva a movimentar.")
public record RequisicaoMovimentacaoEstoque(
        @Schema(description = "Direção da movimentação.", example = "ENTRADA", required = true)
        TipoMovimentacao tipo,
        @Schema(description = "Quantidade movimentada (maior que zero).", example = "5", minimum = "1")
        int quantidade) {
}
