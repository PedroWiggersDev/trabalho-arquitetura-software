package com.archstock.adapter.in.rest;

import com.archstock.application.exception.ProdutoNaoEncontradoException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Traduz produto inexistente para uma resposta HTTP 404.
 */
@Provider
public class ProdutoNaoEncontradoExceptionMapper
        implements ExceptionMapper<ProdutoNaoEncontradoException> {
    /**
     * Gera uma resposta JSON com a mensagem do erro.
     *
     * @param excecao erro de produto inexistente
     * @return resposta HTTP 404
     */
    @Override
    public Response toResponse(ProdutoNaoEncontradoException excecao) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErroApi(excecao.getMessage())).build();
    }
}
