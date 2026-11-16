# Arch Stock

Aplicação didática de controle de estoque criada para demonstrar ideias arquiteturais identificadas no BIRT. É um serviço Quarkus/Java 17 simples, com API REST documentada, Swagger UI e armazenamento H2 por Hibernate ORM.

## Arquiteturas e padrões demonstrados

| Modelo/padrão | Aplicação no Arch Stock | Relação com o BIRT |
|---|---|---|
| Arquitetura em camadas | Adaptadores REST, serviço de aplicação, domínio e persistência ficam separados. | O BIRT organiza responsabilidades entre componentes como `model`, `data`, `engine` e `viewer`. |
| Monólito modular | Um deploy Quarkus com fronteiras internas claras, sem infraestrutura distribuída. | O BIRT separa capacidades em módulos Maven/Tycho, por exemplo `core`, `data`, `engine` e `viewer`. |
| Portas e adaptadores (hexagonal) | `ServicoProduto` depende da porta `RepositorioProduto`; REST e Hibernate são adaptadores. | A separação dos componentes do BIRT permite integrar diferentes fontes de dados e consumidores. |
| Extensões / plugins (inspiração) | Implementações CDI de `PoliticaMovimentacaoEstoque` são selecionadas pelo tipo da movimentação. | O BIRT usa extension points Eclipse, por exemplo conectores ODA e emitters registrados via `plugin.xml`. Aqui CDI substitui OSGi/Eclipse; não há carregamento dinâmico de plugins. |
| Cliente-servidor / REST | Recursos HTTP JSON oferecem as operações; a Swagger UI permite explorar e testar a API. | O BIRT também separa a ferramenta/engine do viewer web que disponibiliza relatórios. |
| Repository | A interface `RepositorioProduto` isola o domínio do armazenamento concreto. | É uma adaptação didática da separação entre dados, conectores e motor no BIRT, não uma afirmação de que o BIRT usa o mesmo padrão. |

### Referências observadas no BIRT

- `birt-master/pom.xml` agrega módulos como `core`, `data`, `engine`, `model` e `viewer`.
- `birt-master/data/org.eclipse.birt.report.data.oda.jdbc/plugin.xml` declara um ponto de extensão e registra um datasource ODA.
- `birt-master/engine/org.eclipse.birt.report.engine.emitter.config.pdf/plugin.xml` registra um emitter PDF em um ponto de extensão do engine.

## Arquitetura do armazenamento

O caso de uso depende somente da interface `RepositorioProduto`, no pacote `application/port`. A implementação atual, `RepositorioProdutoHibernate`, fica em `adapter/out/persistence` e traduz o modelo de domínio para a entidade JPA `EntidadeProduto`. O H2 usa um arquivo local em `arch-stock/data/archstock`, preservando os dados entre reinicializações.

Para trocar Hibernate por outra tecnologia, implemente `RepositorioProduto` em um novo adaptador de saída e selecione apenas uma implementação CDI dessa interface. O serviço de aplicação e a API REST continuam independentes da tecnologia de armazenamento. A dependência do Hibernate, o mapeamento de entidade e as configurações da conexão podem então ser removidos ou substituídos.

## Executar

Requisitos: JDK 17 ou 21 e Maven 3.9+.

Na pasta `arch-stock`:

```bash
export JAVA_HOME="$(/usr/libexec/java_home -v 21)"
export PATH="$JAVA_HOME/bin:$PATH"
mvn -version
mvn quarkus:dev
```

Confirme que `mvn -version` mostra Java 21 antes de iniciar. O `maven.compiler.release`
define a versão do código compilado, mas não escolhe a JVM que executa o Quarkus.
O Java 27 não é compatível com a versão do Byte Buddy usada pelo Quarkus 3.20.2.

- Swagger UI: <http://localhost:8080/q/swagger-ui/>
- Especificação OpenAPI: <http://localhost:8080/q/openapi>
- Listagem de produtos: <http://localhost:8080/api/produtos>

Dois produtos de demonstração são cadastrados automaticamente quando o banco está vazio. O perfil de testes usa um H2 em memória separado; o perfil normal usa arquivo local. Para reiniciar os dados de demonstração, pare a aplicação e remova somente o arquivo de banco `data/archstock.mv.db`.

Executar os testes:

```bash
mvn test
```

## Executar com Docker Compose

Com Docker e o plugin Docker Compose instalados, execute na pasta `arch-stock`:

```bash
docker compose up --build
```

O Compose compila a aplicação em uma imagem de múltiplas etapas e inicia o serviço na porta `8080`. A Swagger UI fica em <http://localhost:8080/q/swagger-ui/>. Os dados H2 são gravados no volume nomeado `dados-estoque` e continuam disponíveis depois que o container for recriado.

Para parar a aplicação sem apagar os dados:

```bash
docker compose down
```

Para apagar também o banco persistido no volume e começar novamente com os produtos de demonstração:

```bash
docker compose down --volumes
```

## Endpoints

Todos os endpoints recebem e retornam JSON. Os exemplos podem ser executados no Swagger UI ou com `curl`.

| Método | Endpoint | Descrição | Respostas principais |
|---|---|---|---|
| `GET` | `/api/produtos` | Lista produtos em ordem alfabética. | `200` |
| `GET` | `/api/produtos/{id}` | Busca um produto pelo identificador. | `200`, `404` |
| `POST` | `/api/produtos` | Cadastra produto com saldo inicial e nível de reposição. | `201`, `400`, `409` |
| `POST` | `/api/produtos/{id}/movimentacoes` | Registra entrada ou saída de estoque. | `200`, `400`, `404` |

### Cadastrar produto

```bash
curl -X POST http://localhost:8080/api/produtos \
  -H 'Content-Type: application/json' \
  -d '{"sku":"MOUSE-001","nome":"Mouse","quantidade":12,"nivelReposicao":3}'
```

Retorna HTTP `201`, o produto criado e o cabeçalho `Location` com o endereço para consulta.

### Buscar e listar produtos

```bash
curl http://localhost:8080/api/produtos
curl http://localhost:8080/api/produtos/demo-1
```

### Movimentar estoque

Use o identificador retornado no cadastro ou um dos identificadores de demonstração:

```bash
curl -X POST http://localhost:8080/api/produtos/demo-1/movimentacoes \
  -H 'Content-Type: application/json' \
  -d '{"tipo":"ENTRADA","quantidade":5}'
```

Para retirar itens, use `"tipo":"SAIDA"`. A quantidade deve ser maior que zero e a saída não pode exceder o saldo. Erros usam o formato `{"mensagem":"..."}`.

## Pontos de interesse

- Os nomes de classes, campos JSON e operações de negócio estão em português; nomes estruturais de arquitetura, como `adapter`, `domain`, `application` e `port`, permanecem para evidenciar os padrões.
- Os métodos REST e seus códigos de resposta estão descritos em português no Swagger UI.
- O domínio não depende de JPA. A entidade `EntidadeProduto` e o mapeamento ficam isolados no adaptador Hibernate.
- Políticas separadas implementam as entradas e saídas de estoque. Outra política CDI pode ser adicionada pelo contrato `PoliticaMovimentacaoEstoque`.
- Não há frontend dedicado: o Swagger UI permite demonstrar as operações sem adicionar uma aplicação separada.
