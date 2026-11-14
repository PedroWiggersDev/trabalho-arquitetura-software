package com.archstock.adapter.in.rest;

import com.archstock.application.exception.ProdutoJaCadastradoException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Traduz SKU duplicado para uma resposta HTTP 409.
 */
@Provider
public class ProdutoJaCadastradoExceptionMapper
        implements ExceptionMapper<ProdutoJaCadastradoException> {
    /**
     * Gera resposta JSON indicando conflito de cadastro.
     *
     * @param excecao erro de SKU duplicado
     * @return resposta HTTP 409
     */
    @Override
    public Response toResponse(ProdutoJaCadastradoException excecao) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErroApi(excecao.getMessage())).build();
    }
}
