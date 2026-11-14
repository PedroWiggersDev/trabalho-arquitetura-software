package com.archstock.application.port;

import com.archstock.domain.Produto;
import com.archstock.domain.TipoMovimentacao;

/**
 * Contrato extensível que aplica uma regra de movimentação de estoque.
 */
public interface PoliticaMovimentacaoEstoque {
    /**
     * Informa o tipo atendido por esta política.
     *
     * @return tipo da movimentacao
     */
    TipoMovimentacao tipo();

    /**
     * Calcula o produto resultante da movimentação.
     *
     * @param produto produto cujo saldo sera alterado
     * @param quantidade unidades movimentadas
     * @return produto com o saldo atualizado
     */
    Produto aplicar(Produto produto, int quantidade);
}
