package com.archstock.application;

import com.archstock.application.port.PoliticaMovimentacaoEstoque;
import com.archstock.domain.Produto;
import com.archstock.domain.TipoMovimentacao;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Política que adiciona unidades ao saldo atual.
 */
@ApplicationScoped
public class PoliticaEntradaEstoque implements PoliticaMovimentacaoEstoque {
    /**
     * Identifica a estratégia como entrada de estoque.
     *
     * @return tipo entrada
     */
    @Override
    public TipoMovimentacao tipo() {
        return TipoMovimentacao.ENTRADA;
    }

    /**
     * Soma unidades ao saldo sem ultrapassar o limite inteiro.
     *
     * @param produto produto recebido
     * @param quantidade unidades recebidas
     * @return produto com saldo acrescido
     */
    @Override
    public Produto aplicar(Produto produto, int quantidade) {
        if (quantidade > Integer.MAX_VALUE - produto.quantidade()) {
            throw new IllegalArgumentException("O saldo resultante excede a quantidade suportada");
        }
        return produto.comQuantidade(Math.addExact(produto.quantidade(), quantidade));
    }
}
