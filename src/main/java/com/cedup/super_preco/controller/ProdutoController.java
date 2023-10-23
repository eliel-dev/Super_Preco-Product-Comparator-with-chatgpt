package com.cedup.super_preco.controller;

import com.cedup.super_preco.ChatGPT;
import com.cedup.super_preco.ScrapeCooper;
import com.cedup.super_preco.ScrapeKoch;
import com.cedup.super_preco.model.ProdutoDTO;
import com.cedup.super_preco.model.dao.ProdutoDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
    @Autowired
    ProdutoDAO produtoDAO;
    @Autowired
    ScrapeCooper scrapeCooper;
    @Autowired
    ScrapeKoch scrapeKoch;
    @Autowired
    ChatGPT chatGPT;

    @GetMapping ("/scrape")
    public List<ProdutoDTO> getScraping() throws SQLException {
        // Chama o método de web scraping em cada serviço e obtenha as listas de produtos
        List<ProdutoDTO> produtosCooper = scrapeCooper.scrapeProducts();
        List<ProdutoDTO> produtosKoch = scrapeKoch.scrapeProducts();

        // Combina as duas listas em uma
        List<ProdutoDTO> allProdutos = new ArrayList<>();
        allProdutos.addAll(produtosCooper);
        allProdutos.addAll(produtosKoch);

        // Passa a lista combinada de produtos para o método postProdutos() do seu DAO
        produtoDAO.postProdutos(allProdutos);

        return allProdutos;
    }

    @GetMapping("/testOpenAI")
    public String testOpenAI() throws SQLException {
        // limite de token = https://platform.openai.com/docs/models/gpt-3-5

        // Obter todos os produtos
        List<ProdutoDTO> todosProdutos = produtoDAO.getByMercado();

        // Dividir a lista de produtos em duas listas para cada mercado
        List<ProdutoDTO> produtosCooper = todosProdutos.stream()
                .filter(produto -> produto.id_mercado == 1)
                .toList();
        List<ProdutoDTO> produtosKoch = todosProdutos.stream()
                .filter(produto -> produto.id_mercado == 2)
                .toList();

        // Crie o prompt para a API da OpenAI
        StringBuilder prompt = new StringBuilder();
        prompt.append("\\n\\nCooper:");
        for (ProdutoDTO produto : produtosCooper) {
            prompt.append("\\n").append("id_produto: ").append(produto.id_produto).append(", nome: ").append(produto.nome);
        }

        prompt.append("\\n\\nSuperkoch:");
        for (ProdutoDTO produto : produtosKoch) {
            prompt.append("\\n").append("id_produto: ").append(produto.id_produto).append(", nome: ").append(produto.nome);
        }

        // Remova a última vírgula e espaço
        prompt.setLength(prompt.length() - 2);

        // Obter a resposta da API da OpenAI
        String response = chatGPT.getOpenAIResponse(prompt.toString());

        try {
            // Criar um ObjectMapper
            ObjectMapper mapper = new ObjectMapper();

            // Converter a string JSON em um JsonNode
            JsonNode rootNode = mapper.readTree(response);

            // Obter o nó "choices"
            JsonNode choicesNode = rootNode.path("choices");

            // Se o nó "choices" é um array e tem pelo menos um elemento
            if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                // Obter o primeiro elemento do array "choices"
                JsonNode firstChoiceNode = choicesNode.get(0);

                // Obter o nó "message"
                JsonNode messageNode = firstChoiceNode.path("message");

                // Obter o nó "content"
                JsonNode contentNode = messageNode.path("content");

                // Imprimir o conteúdo no console
                System.out.println("Conteúdo da resposta: " + contentNode.asText());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    List<ProdutoDTO> produtoDTOS = new ArrayList<>();
    @GetMapping
    public List<ProdutoDTO> getProdutos() throws SQLException {

        produtoDTOS = produtoDAO.getAll();

        return produtoDTOS;
    }

    @GetMapping("/grupo/{id_grupo}")
    public List<ProdutoDTO> getProdutosPorGrupo(@PathVariable int id_grupo) throws SQLException {
        List<ProdutoDTO> grupo2 = produtoDAO.getProdutosPorGrupo(id_grupo);
        return grupo2;
    }

    @GetMapping("/{id}")
    public ProdutoDTO getProdutp(@PathVariable("id") int id) throws SQLException {

        ProdutoDTO produto2 = produtoDAO.getProduto(id);

        return produto2;
    }

    @PostMapping
    public ProdutoDTO postProduto(@RequestBody ProdutoDTO dto) throws SQLException {

        produtoDAO.postProduto(dto);

        return dto;
    }

    @DeleteMapping("/{id}")
    public ProdutoDTO deleteProduto(@PathVariable("id") int id) throws SQLException {
        ProdutoDTO grupoDTO = new ProdutoDAO().getProduto(id);

        produtoDAO.deleteProduto(id);

        return grupoDTO;
    }

    @PutMapping("{id}")
    public ProdutoDTO putProduto(@PathVariable ("id") int id, @RequestBody ProdutoDTO dto) throws SQLException {

        dto.setId_produto(id);

        produtoDAO.putProduto(dto);

        return dto;
    }
}
