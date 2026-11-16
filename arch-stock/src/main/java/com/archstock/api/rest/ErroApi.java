package com.archstock.api.rest;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Formato padrão das respostas de erro da API.
 *
 * @param mensagem descricao do erro
 */
@Schema(name = "ErroApi", description = "Resposta padrão para erros de requisição.")
public record ErroApi(
        @Schema(description = "Descrição do erro.", example = "O produto não foi encontrado")
        String mensagem) {
}
