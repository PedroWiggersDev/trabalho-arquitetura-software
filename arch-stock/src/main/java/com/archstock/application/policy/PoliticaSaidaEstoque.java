package com.archstock.application.policy;

import com.archstock.domain.Produto;
import com.archstock.domain.TipoMovimentacao;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Política que remove unidades sem permitir saldo negativo.
 */
@ApplicationScoped
public class PoliticaSaidaEstoque implements PoliticaMovimentacaoEstoque {
    /**
     * Identifica a estratégia como saída de estoque.
     *
     * @return tipo saida
     */
    @Override
    public TipoMovimentacao tipo() {
        return TipoMovimentacao.SAIDA;
    }

    /**
     * Subtrai unidades quando há saldo suficiente.
     *
     * @param produto produto movimentado
     * @param quantidade unidades retiradas
     * @return produto com saldo reduzido
     */
    @Override
    public Produto aplicar(Produto produto, int quantidade) {
        if (quantidade > produto.quantidade()) {
            throw new IllegalArgumentException("Não há estoque suficiente para esta movimentação");
        }
        return produto.comQuantidade(produto.quantidade() - quantidade);
    }
}
