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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        // Passa cada produto da lista combinada para o método addProduto() do seu DAO
        for (Produto_MercadoEntity produtoEntity : allProdutos) {
            produtoMercadoDAO.addProduto(produtoEntity);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Produto_MercadoDTO>> getProdutos() throws SQLException {

        return ResponseEntity.ok().body(produtoConverter.toDTO(produtoMercadoDAO.getAll()));

    }

//    @GetMapping("/autocomplete/")
//    public List<Produto_MercadoDTO> autocomplete(@RequestParam String searchTerm) throws SQLException {
//        return produtoMercadoDAO.autocomplete(searchTerm);
//    }

    @GetMapping("/produtos/")
    public ResponseEntity<List<Produto_MercadoDTO>> getUniqueProdutos() throws SQLException {

        return ResponseEntity.ok().body(produtoConverter.toDTO(produtoMercadoDAO.getUniqueProdutos()));

    }

    @GetMapping("/grupo/{id_grupo}")
    public ResponseEntity<List<Produto_MercadoDTO>> getProdutosPorGrupo(@PathVariable ProdutoEntity id_grupo) throws SQLException {

        return ResponseEntity.ok().body(produtoConverter.toDTO(produtoMercadoDAO.getProdutosPorGrupo(id_grupo)));
    }

    @PostMapping("/gpt/")
    public ResponseEntity<String> sentGPT() throws SQLException {
        int loteSize = 25;

        // Obtenha o total de produtos do banco de dados
        int totalProdutos = produtoMercadoDAO.getTotalProdutos();
        String response = null;

        for (int i = 0; i < totalProdutos; i += loteSize) {
            // Obtenha o lote de produtos do banco de dados usando LIMIT e OFFSET
            List<Produto_MercadoEntity> loteProdutos = produtoMercadoDAO.getByMercado(loteSize, i);
            System.out.println("loteProdutos" + loteProdutos);

            // Cria o prompt para a API da OpenAI com o lote de produtos
            String promptListaProduto = criarPromptListaProduto(loteProdutos);

            // Envia o prompt para a API da OpenAI e recebe a resposta
            response = chatGPT.getOpenAIResponse(promptListaProduto);

            // Desativa envio para API GPT
            //response = "{\"choices\": []}";

            // Processa a resposta da API
            criaGruposProdutosDaAPI(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public String criarPromptListaProduto(List<Produto_MercadoEntity> produtos) {
        StringBuilder promptListaProduto = new StringBuilder();
        for (Produto_MercadoEntity produto : produtos) {
            promptListaProduto.append("\\n").append("id_produto: ").append(produto.id_produto_mercado).append(", nome: ").append(produto.nome);
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
                System.out.println("novoProduto" + novoProduto);
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
                System.out.println("ID do Produto_Mercado: " + idProduto_nercado.asInt());
                produtoMercadoDAO.updateIdProduto(idProduto_nercado.asInt(), novoProduto.getId());
            }
        }
    }
}
