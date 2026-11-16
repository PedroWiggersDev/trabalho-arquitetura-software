package com.archstock.api.rest;

import com.archstock.application.exception.ProdutoJaCadastradoException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * Traduz SKU duplicado para uma resposta HTTP 409.
 */
@Provider
public class ProdutoJaCadastradoExceptionMapper
        extends BaseExceptionMapper<ProdutoJaCadastradoException> {
    public ProdutoJaCadastradoExceptionMapper() {
        super(Response.Status.CONFLICT);
    }
}
