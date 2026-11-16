package com.archstock.api.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

/**
 * Base para mapeadores de exceções HTTP que devolvem sempre o mesmo formato
 * de erro padronizado pela API.
 *
 * @param <T> tipo da exceção mapeada
 */
public abstract class BaseExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {
    private final int statusCode;

    protected BaseExceptionMapper(Response.Status status) {
        this.statusCode = status.getStatusCode();
    }

    @Override
    public Response toResponse(T exception) {
        return Response.status(statusCode)
                .entity(new ErroApi(exception.getMessage()))
                .build();
    }
}
