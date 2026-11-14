package com.archstock.application.exception;

/**
 * Indica que não foi encontrado produto com o identificador solicitado.
 */
public class ProdutoNaoEncontradoException extends RuntimeException {
    /**
     * Cria a exceção para um identificador inexistente.
     *
     * @param id identificador não encontrado
     */
    public ProdutoNaoEncontradoException(String id) {
        super("O produto '" + id + "' não foi encontrado");
    }
}
