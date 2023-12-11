package com.cedup.super_preco.controller.produto;

import com.cedup.super_preco.model.produto.ProdutoDAO;
import com.cedup.super_preco.model.produto.ProdutoEntity;
import com.cedup.super_preco.model.produto.Produto_MercadoDAO;
import com.cedup.super_preco.model.produto.Produto_MercadoEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/produto/")
@CrossOrigin(origins = "*")
public class ProdutoController {
    @Autowired
    Produto_MercadoDAO produtoMercadoDAO;
    @Autowired
    ProdutoDAO produtoDAO;
    @Autowired
    CooperScrapper cooperScrapper;
    @Autowired
    KochScrapper kochScrapper;
    @Autowired
    ChatGPT chatGPT;
    @Autowired
    ProdutoConverter produtoConverter;


    @PostMapping("/scraping/")
    public ResponseEntity<List<Produto_MercadoEntity>> getScraping() throws SQLException {
        // Chama o método de web scraping em cada serviço e obtenha as listas de produtos
        List<Produto_MercadoEntity> produtosCooper = cooperScrapper.scrapeProducts();
        List<Produto_MercadoEntity> produtosKoch = kochScrapper.scrapeProducts();

        // Combina as duas listas em uma
        List<Produto_MercadoEntity> allProdutos = new ArrayList<>();
        allProdutos.addAll(produtosCooper);
        allProdutos.addAll(produtosKoch);

        // Passa cada produto da lista combinada para o método addProduto()
        for (Produto_MercadoEntity produtoEntity : allProdutos) {
            produtoMercadoDAO.addProduto(produtoEntity);
        }
        System.out.println();
        System.out.println("Scraping de produtos finalizado.");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Produto_MercadoDTO>> getProdutos() throws SQLException {

        return ResponseEntity.ok().body(produtoConverter.toDTO(produtoMercadoDAO.getAll()));

    }

    @GetMapping("/autocomplete/")
    public ResponseEntity<List<Produto_MercadoDTO>> autocomplete(@RequestParam String searchTerm) throws SQLException {
        return ResponseEntity.ok().body(produtoConverter.toDTO(produtoMercadoDAO.autocomplete(searchTerm)));
    }

    @GetMapping("/produtos/")
    public ResponseEntity<List<Produto_MercadoDTO>> getUniqueProdutos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) throws SQLException {

        List<Produto_MercadoDTO> produtos = produtoConverter.toDTO(produtoMercadoDAO.getUniqueProdutos(page, size));
        return ResponseEntity.ok().body(produtos);
    }

    @GetMapping("/grupo/{id_grupo}")
    public ResponseEntity<List<Produto_MercadoDTO>> getProdutosPorGrupo(@PathVariable ProdutoEntity id_grupo) throws SQLException {

        return ResponseEntity.ok().body(produtoConverter.toDTO(produtoMercadoDAO.getProdutosPorGrupo(id_grupo)));
    }

    @PostMapping("/gpt/")
    public ResponseEntity<String> sentGPT() throws SQLException {
        int loteSize = 40;

        // Obtenha o total de produtos do banco de dados
        int totalProdutos = produtoMercadoDAO.getTotalProdutos();
        String response = null;

        int y = 0;
        for (int i = 0; i < totalProdutos; i += loteSize) {
            y ++;
            System.out.println();
            System.out.println("Enviando o " + y + "º lote de produtos.");
            // Obtenha o lote de produtos do banco de dados usando LIMIT e OFFSET
            List<Produto_MercadoEntity> loteProdutos = produtoMercadoDAO.getByMercado(loteSize, i);

            // Cria o prompt para a API da OpenAI com o lote de produtos
            String promptListaProduto = criarPromptListaProduto(loteProdutos);

            // Envia o prompt para a API da OpenAI e recebe a resposta
            response = chatGPT.getOpenAIResponse(promptListaProduto);

            // Processa a resposta da API
            criaGruposProdutosDaAPI(response);
        }

        System.out.println("Relacionamentos de produtos finalizado.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public String criarPromptListaProduto(List<Produto_MercadoEntity> produtos) {
        StringBuilder promptListaProduto = new StringBuilder();
        for (Produto_MercadoEntity produto : produtos) {
            String nomeProduto = produto.nome.toLowerCase(); // remove o volume do nome do produto

            promptListaProduto.append("\\n")
                    .append("id_produto: ")
                    .append(produto.id_produto_mercado)
                    .append(", nome: ")
                    .append(nomeProduto.trim()); // adicionado trim() para remover espaços em branco que podem ter sido deixados após a remoção do volume
        }
        return promptListaProduto.toString();
    }

    public void criaGruposProdutosDaAPI(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            JsonNode firstChoiceNode = rootNode.path("choices").get(0);
            String conteudoResposta = firstChoiceNode.path("message").get("content").asText();
            conteudoResposta = conteudoResposta.substring(conteudoResposta.indexOf('\n') + 1); // remove first line
            conteudoResposta = conteudoResposta.substring(0, conteudoResposta.lastIndexOf('\n')); // remove last line
            JsonNode gruposDeProdutoMercado = mapper.readTree(conteudoResposta);

            for (JsonNode grupo : gruposDeProdutoMercado) {
                ProdutoDTO novoProduto = criaGrupoProdutosDoMercado(grupo);
                ProdutoConverter produtoConverter = new ProdutoConverter();
                ProdutoEntity entity = produtoConverter.toEntity(novoProduto);
                ProdutoDAO produtoDAO = new ProdutoDAO();
                produtoDAO.addGrupo(entity);
                atualizaIdProdutoDoGrupo(grupo, novoProduto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ProdutoDTO criaGrupoProdutosDoMercado(JsonNode grupo) throws SQLException {
        ProdutoDTO novoGrupoProdutoMercado = new ProdutoDTO();
        String idGrupoUUID = UUID.randomUUID().toString();
        novoGrupoProdutoMercado.setId(idGrupoUUID);
        JsonNode idProdutos = grupo.get("id_produto");
        if (idProdutos != null && idProdutos.isArray() && !idProdutos.isEmpty()) {
            int idPrimeiroProduto = idProdutos.get(0).asInt();
            Produto_MercadoDTO produto = produtoConverter.toDTO(produtoMercadoDAO.getProduto(idPrimeiroProduto));
            if (produto != null && produto.nome != null) {
                novoGrupoProdutoMercado.setNome(produto.nome);
            }
        }
        return novoGrupoProdutoMercado;
    }

    private void atualizaIdProdutoDoGrupo(JsonNode grupo, ProdutoDTO novoProduto) throws SQLException {
        JsonNode idsProdutoMercado = grupo.get("id_produto");
        if (idsProdutoMercado != null && idsProdutoMercado.isArray()) {
            for (JsonNode idProduto_nercado : idsProdutoMercado) {
                produtoMercadoDAO.updateIdProduto(idProduto_nercado.asInt(), novoProduto.getId());
            }
        }
    }

}
