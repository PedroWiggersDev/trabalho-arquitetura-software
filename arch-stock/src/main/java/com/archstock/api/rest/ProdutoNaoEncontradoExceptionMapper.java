package com.archstock.api.rest;

import com.archstock.application.exception.ProdutoNaoEncontradoException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * Traduz produto inexistente para uma resposta HTTP 404.
 */
@Provider
public class ProdutoNaoEncontradoExceptionMapper
        extends BaseExceptionMapper<ProdutoNaoEncontradoException> {
    public ProdutoNaoEncontradoExceptionMapper() {
        super(Response.Status.NOT_FOUND);
    }
}
