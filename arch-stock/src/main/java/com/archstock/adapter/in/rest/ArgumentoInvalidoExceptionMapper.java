package com.archstock.adapter.in.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Traduz argumentos inválidos para uma resposta HTTP 400.
 */
@Provider
public class ArgumentoInvalidoExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    /**
     * Gera uma resposta JSON com o motivo da validação.
     *
     * @param excecao argumento que nao atende as regras do dominio
     * @return resposta HTTP 400
     */
    @Override
    public Response toResponse(IllegalArgumentException excecao) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErroApi(excecao.getMessage())).build();
    }
}
