package com.cedup.super_preco.controller;

import com.cedup.super_preco.ChatGPT;
import com.cedup.super_preco.ScrapeCooper;
import com.cedup.super_preco.ScrapeKoch;
import com.cedup.super_preco.model.ProdutoDTO;
import com.cedup.super_preco.model.Produto_MercadoDTO;
import com.cedup.super_preco.model.dao.ProdutoDAO;
import com.cedup.super_preco.model.dao.Produto_MercadoDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/produto")
public class Produto_MercadoController {
    @Autowired
    Produto_MercadoDAO produtoMercadoDAO;
    @Autowired
    ScrapeCooper scrapeCooper;
    @Autowired
    ScrapeKoch scrapeKoch;
    @Autowired
    ChatGPT chatGPT;

    @GetMapping ("/scrape")
    public List<Produto_MercadoDTO> getScraping() throws SQLException {
        // Chama o método de web scraping em cada serviço e obtenha as listas de produtos
        List<Produto_MercadoDTO> produtosCooper = scrapeCooper.scrapeProducts();
        List<Produto_MercadoDTO> produtosKoch = scrapeKoch.scrapeProducts();

        // Combina as duas listas em uma
        List<Produto_MercadoDTO> allProdutos = new ArrayList<>();
        allProdutos.addAll(produtosCooper);
        allProdutos.addAll(produtosKoch);

        // Passa a lista combinada de produtos para o método postProdutos() do seu DAO
        produtoMercadoDAO.postProdutos(allProdutos);

        return allProdutos;
    }

    @GetMapping("/testOpenAI")
    public String testOpenAI() throws SQLException {
        // limite de token = https://platform.openai.com/docs/models/gpt-3-5

        // Obter todos os produtos
        List<Produto_MercadoDTO> todosProdutos = produtoMercadoDAO.getByMercado();

        // Dividir a lista de produtos em duas listas para cada mercado
        List<Produto_MercadoDTO> produtosCooper = todosProdutos.stream()
                .filter(produto -> produto.id_mercado == 1)
                .toList();
        List<Produto_MercadoDTO> produtosKoch = todosProdutos.stream()
                .filter(produto -> produto.id_mercado == 2)
                .toList();

        // Crie o promptListaProduto para a API da OpenAI
        StringBuilder promptListaProduto = new StringBuilder();
        promptListaProduto.append("\\n\\nCooper:");
        for (Produto_MercadoDTO produto : produtosCooper) {
            promptListaProduto.append("\\n").append("id_produto: ").append(produto.id_produto_mercado).append(", nome: ").append(produto.nome);
        }

        promptListaProduto.append("\\n\\nSuperkoch:");
        for (Produto_MercadoDTO produto : produtosKoch) {
            promptListaProduto.append("\\n").append("id_produto: ").append(produto.id_produto_mercado).append(", nome: ").append(produto.nome);
        }

        // Remova a última vírgula e espaço
        promptListaProduto.setLength(promptListaProduto.length() - 2);

        // Obter a resposta da API da OpenAI
        String response = chatGPT.getOpenAIResponse(promptListaProduto.toString());

        // Lista para armazenar os novos produtos
        List<ProdutoDTO> novosProdutos = new ArrayList<>();

        try {
            // Criar um ObjectMapper
            ObjectMapper mapper = new ObjectMapper();

            // Converter a string JSON em um JsonNode
            JsonNode rootNode = mapper.readTree(response);

            // Obter o nó "choices"
            JsonNode choicesNode = rootNode.path("choices");

            // Converter o conteúdo da resposta em um array de JsonNodes
            JsonNode contentNode = null;
            if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                // Obter o primeiro elemento do array "choices"
                JsonNode firstChoiceNode = choicesNode.get(0);

                // Obter o nó "message"
                JsonNode messageNode = firstChoiceNode.path("message");

                // Obter o nó "content"
                contentNode = messageNode.path("content");

            }

            if (contentNode != null) {
                // Converter o conteúdo da resposta em um array de JsonNodes
                JsonNode grupos = new ObjectMapper().readTree(contentNode.asText());

                // Para cada grupo identificado pela API da OpenAI
                for (JsonNode grupo : grupos) {
                    // Criar um novo Produto
                    ProdutoDTO novoProduto = new ProdutoDTO();

                    // Verificar se id_grupo não é nulo antes de acessá-lo
                    JsonNode idGrupoNode = grupo.get("id_grupo");
                    if (idGrupoNode != null) {
                        novoProduto.setId(Integer.parseInt(idGrupoNode.asText()));
                    }

                    // Obter a lista de id_produto para este grupo
                    JsonNode idProdutos = grupo.get("id_produto");

                    // Verificar se idProdutos não é nulo e não está vazio antes de acessá-lo
                    if (idProdutos != null && !idProdutos.isEmpty()) {
                        // Obter o ID do primeiro produto na lista
                        int idPrimeiroProduto = idProdutos.get(0).asInt();

                        // Obter o Produto_MercadoDTO correspondente usando o método getProduto()
                        Produto_MercadoDTO produto = produtoMercadoDAO.getProduto(idPrimeiroProduto);

                        // Verificar se produto não é nulo antes de acessar seu campo nome
                        if (produto != null) {
                            novoProduto.setNome(produto.nome);
                        }
                    }

                    // Inserir o novo Produto na tabela produto
                    ProdutoDAO produtoDAO = new ProdutoDAO();
                    produtoDAO.insertGrupo(novoProduto);

                    // Adicionar o novo Produto à lista
                    novosProdutos.add(novoProduto);

                    System.out.println(novoProduto);

                    // Verificar se idProdutos não é nulo antes de iterar sobre ele
                    if (idProdutos != null) {
                        for (JsonNode idProduto : idProdutos) {
                            System.out.println("ID do Produto: " + idProduto.asInt());

                            // Atualizar o id_produto na tabela produto_mercado
                            produtoMercadoDAO.updateIdProduto(idProduto.asInt(), novoProduto.getId());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    List<Produto_MercadoDTO> produtoMercadoDTOS = new ArrayList<>();
    @GetMapping
    public List<Produto_MercadoDTO> getProdutos() throws SQLException {

        produtoMercadoDTOS = produtoMercadoDAO.getAll();

        return produtoMercadoDTOS;
    }

    @GetMapping("/grupo/{id_grupo}")
    public List<Produto_MercadoDTO> getProdutosPorGrupo(@PathVariable int id_grupo) throws SQLException {
        List<Produto_MercadoDTO> grupo2 = produtoMercadoDAO.getProdutosPorGrupo(id_grupo);
        return grupo2;
    }

    @GetMapping("/{id}")
    public Produto_MercadoDTO getProdutp(@PathVariable("id") int id) throws SQLException {

        Produto_MercadoDTO produto2 = produtoMercadoDAO.getProduto(id);

        return produto2;
    }

    @PostMapping
    public Produto_MercadoDTO postProduto(@RequestBody Produto_MercadoDTO dto) throws SQLException {

        produtoMercadoDAO.postProduto(dto);

        return dto;
    }

    @DeleteMapping("/{id}")
    public Produto_MercadoDTO deleteProduto(@PathVariable("id") int id) throws SQLException {
        Produto_MercadoDTO grupoDTO = new Produto_MercadoDAO().getProduto(id);

        produtoMercadoDAO.deleteProduto(id);

        return grupoDTO;
    }

    @PutMapping("{id}")
    public Produto_MercadoDTO putProduto(@PathVariable ("id") int id, @RequestBody Produto_MercadoDTO dto) throws SQLException {

        dto.setId_produto_mercado(id);

        produtoMercadoDAO.putProduto(dto);

        return dto;
    }
}
