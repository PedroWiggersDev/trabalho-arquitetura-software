package com.archstock.adapter.in.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

/**
 * Testes de integração para os endpoints REST e o armazenamento H2.
 */
@QuarkusTest
class ProdutoResourceTest {
    /**
     * Verifica cadastro, persistência, entrada e validação de saída de estoque.
     */
    @Test
    void cadastraProdutoEPersisteMovimentacoes() {
        String sku = "TESTE-" + UUID.randomUUID();
        String idProduto = given()
                .contentType("application/json")
                .body("""
                        {"sku":"%s","nome":"Item de demonstração","quantidade":5,"nivelReposicao":2}
                        """.formatted(sku))
                .when()
                .post("/api/produtos")
                .then()
                .statusCode(201)
                .body("quantidade", equalTo(5))
                .extract()
                .path("id");

        given()
                .when()
                .get("/api/produtos/{id}", idProduto)
                .then()
                .statusCode(200)
                .body("sku", equalTo(sku));

        given()
                .contentType("application/json")
                .body("""
                        {"tipo":"ENTRADA","quantidade":3}
                        """)
                .when()
                .post("/api/produtos/{id}/movimentacoes", idProduto)
                .then()
                .statusCode(200)
                .body("quantidade", equalTo(8));

        given()
                .contentType("application/json")
                .body("""
                        {"tipo":"SAIDA","quantidade":9}
                        """)
                .when()
                .post("/api/produtos/{id}/movimentacoes", idProduto)
                .then()
                .statusCode(400)
                .body("mensagem", equalTo("Não há estoque suficiente para esta movimentação"));
    }

    /**
     * Verifica a disponibilidade da especificação OpenAPI e da Swagger UI.
     */
    @Test
    void publicaDocumentacaoDaApi() {
        given()
                .when()
                .get("/q/openapi")
                .then()
                .statusCode(200)
                .body(containsString("/api/produtos"))
                .body(containsString("Cadastrar produto"));

        given()
                .when()
                .get("/q/swagger-ui/")
                .then()
                .statusCode(200);
    }

    /**
     * Verifica as respostas de produto inexistente e SKU duplicado.
     */
    @Test
    void retornaErrosDeProdutoEConflito() {
        given()
                .when()
                .get("/api/produtos/nao-existe")
                .then()
                .statusCode(404)
                .body("mensagem", equalTo("O produto 'nao-existe' não foi encontrado"));

        given()
                .contentType("application/json")
                .body("""
                        {"sku":"CAD-001","nome":"Duplicado","quantidade":1,"nivelReposicao":0}
                        """)
                .when()
                .post("/api/produtos")
                .then()
                .statusCode(409);
    }
}
