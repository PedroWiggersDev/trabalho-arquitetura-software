package com.archstock.adapter.in.rest;

import com.archstock.application.ServicoProduto;
import com.archstock.domain.Produto;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

/**
 * Adaptador REST de entrada para operações de produto e estoque.
 */
@Path("/api/produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos", description = "Cadastro e consulta de produtos do estoque.")
public class ProdutoResource {
    private final ServicoProduto servicoProdutos;

    /**
     * Recebe o serviço de aplicação usado pelo adaptador REST.
     *
     * @param servicoProdutos casos de uso de produtos
     */
    @Inject
    public ProdutoResource(ServicoProduto servicoProdutos) {
        this.servicoProdutos = servicoProdutos;
    }

    /**
     * Lista os produtos em ordem alfabética.
     *
     * @return lista de produtos
     */
    @GET
    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos cadastrados, ordenados pelo nome.")
    @APIResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso.")
    public List<Produto> listar() {
        return servicoProdutos.listarProdutos();
    }

    /**
     * Consulta um produto pelo identificador.
     *
     * @param id identificador do produto
     * @return produto encontrado
     */
    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar produto", description = "Busca um produto pelo identificador retornado no cadastro.")
    @APIResponse(responseCode = "200", description = "Produto encontrado.")
    @APIResponse(responseCode = "404", description = "Produto não encontrado.",
            content = @Content(schema = @Schema(implementation = ErroApi.class)))
    public Produto buscar(
            @Parameter(description = "Identificador único do produto.", example = "demo-1")
            @PathParam("id") String id) {
        return servicoProdutos.obterProduto(id);
    }

    /**
     * Cadastra produto e retorna sua URI no cabeçalho Location.
     *
     * @param requisicao dados do produto
     * @param informacoesUri informacoes da requisicao HTTP
     * @return resposta HTTP 201 com o produto cadastrado
     */
    @POST
    @Operation(summary = "Cadastrar produto", description = "Cria produto com saldo inicial e nível de reposição.")
    @APIResponse(responseCode = "201", description = "Produto cadastrado; resposta inclui o cabeçalho Location.")
    @APIResponse(responseCode = "400", description = "Dados inválidos ou valores negativos.",
            content = @Content(schema = @Schema(implementation = ErroApi.class)))
    @APIResponse(responseCode = "409", description = "Já existe produto com o SKU informado.",
            content = @Content(schema = @Schema(implementation = ErroApi.class)))
    public Response cadastrar(RequisicaoCriacaoProduto requisicao, @Context UriInfo informacoesUri) {
        if (requisicao == null) {
            throw new IllegalArgumentException("O corpo da requisição de cadastro é obrigatório");
        }
        Produto produto = servicoProdutos.cadastrarProduto(
                requisicao.sku(), requisicao.nome(), requisicao.quantidade(), requisicao.nivelReposicao());
        URI localizacao = informacoesUri.getAbsolutePathBuilder().path(produto.id()).build();
        return Response.created(localizacao).entity(produto).build();
    }

    /**
     * Aplica uma entrada ou saída ao saldo do produto.
     *
     * @param id identificador do produto
     * @param requisicao tipo e quantidade da movimentação
     * @return produto com saldo atualizado
     */
    @POST
    @Path("/{id}/movimentacoes")
    @Operation(summary = "Movimentar estoque",
            description = "Registra uma ENTRADA ou SAÍDA e retorna o produto com saldo atualizado.")
    @APIResponse(responseCode = "200", description = "Movimentação aplicada.")
    @APIResponse(responseCode = "400", description = "Tipo ou quantidade inválidos, ou saldo insuficiente.",
            content = @Content(schema = @Schema(implementation = ErroApi.class)))
    @APIResponse(responseCode = "404", description = "Produto não encontrado.",
            content = @Content(schema = @Schema(implementation = ErroApi.class)))
    public Produto movimentar(
            @Parameter(description = "Identificador do produto.", example = "demo-1")
            @PathParam("id") String id,
            RequisicaoMovimentacaoEstoque requisicao) {
        if (requisicao == null) {
            throw new IllegalArgumentException("O corpo da requisição de movimentação é obrigatório");
        }
        return servicoProdutos.movimentarEstoque(id, requisicao.tipo(), requisicao.quantidade());
    }
}
