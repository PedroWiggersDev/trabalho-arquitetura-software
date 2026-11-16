package com.archstock.api.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/**
 * Traduz argumentos inválidos para uma resposta HTTP 400.
 */
@Provider
public class ArgumentoInvalidoExceptionMapper extends BaseExceptionMapper<IllegalArgumentException> {
    public ArgumentoInvalidoExceptionMapper() {
        super(Response.Status.BAD_REQUEST);
    }
}
