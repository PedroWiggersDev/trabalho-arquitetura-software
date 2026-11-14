package com.archstock.application.exception;

/**
 * Indica que já existe um produto cadastrado com o SKU informado.
 */
public class ProdutoJaCadastradoException extends RuntimeException {
    /**
     * Cria a exceção para um SKU duplicado.
     *
     * @param sku código já cadastrado
     */
    public ProdutoJaCadastradoException(String sku) {
        super("Já existe um produto com o SKU '" + sku + "'");
    }
}
