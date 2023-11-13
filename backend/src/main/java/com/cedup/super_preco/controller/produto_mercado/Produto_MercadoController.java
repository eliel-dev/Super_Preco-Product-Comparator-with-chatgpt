package com.cedup.super_preco.controller.produto_mercado;

import com.cedup.super_preco.ChatGPT;
import com.cedup.super_preco.ScrapeCooper;
import com.cedup.super_preco.ScrapeKoch;
import com.cedup.super_preco.controller.produto.ProdutoDTO;
import com.cedup.super_preco.model.produto.ProdutoDAO;
import com.cedup.super_preco.model.produto_mercado.Produto_MercadoDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/produto/")
public class Produto_MercadoController {
    @Autowired
    Produto_MercadoDAO produtoMercadoDAO;
    @Autowired
    ScrapeCooper scrapeCooper;
    @Autowired
    ScrapeKoch scrapeKoch;
    @Autowired
    ChatGPT chatGPT;

    @GetMapping ("/scraping/")
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

    @GetMapping("/gpt/")
    public String testOpenAI() throws SQLException {
        int loteSize = 25;

        // Obtenha o total de produtos do banco de dados
        int totalProdutos = produtoMercadoDAO.getTotalProdutos();
        String response = null;

        for (int i = 0; i < totalProdutos; i += loteSize) {
            // Obtenha o lote de produtos do banco de dados usando LIMIT e OFFSET
            List<Produto_MercadoDTO> loteProdutos = produtoMercadoDAO.getByMercado(loteSize, i);
            System.out.println("loteProdutos" + loteProdutos);

            // Cria o prompt para a API da OpenAI com o lote de produtos
            String promptListaProduto = criarPromptListaProduto(loteProdutos);

            // Envia o prompt para a API da OpenAI e recebe a resposta
            response = chatGPT.getOpenAIResponse(promptListaProduto);

            // Desativa envio para API GPT
            //response = "{\"choices\": []}";

            // Processa a resposta da API
            List<ProdutoDTO> novosProdutos = criaGruposProdutosDaAPI(response);
        }

        return response;
    }

    List<Produto_MercadoDTO> produtoMercadoDTOS = new ArrayList<>();
    @GetMapping
    public List<Produto_MercadoDTO> getProdutos() throws SQLException {

        return produtoMercadoDAO.getAll();

    }

    @GetMapping("/autocomplete/")
    public List<Produto_MercadoDTO> autocomplete(@RequestParam String searchTerm) throws SQLException {
        return produtoMercadoDAO.autocomplete(searchTerm);
    }

    @GetMapping("/grupo/{id_grupo}")
    public List<Produto_MercadoDTO> getProdutosPorGrupo(@PathVariable String id_grupo) throws SQLException {
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

    private String criarPromptListaProduto(List<Produto_MercadoDTO> produtos) {
        StringBuilder promptListaProduto = new StringBuilder();
        for (Produto_MercadoDTO produto : produtos) {
            promptListaProduto.append("\\n").append("id_produto: ").append(produto.id_produto_mercado).append(", nome: ").append(produto.nome);
        }
        return promptListaProduto.toString();
    }

    private List<ProdutoDTO> criaGruposProdutosDaAPI(String response) {
        List<ProdutoDTO> novoGrupoProdutoMercado = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            JsonNode choicesNode = rootNode.path("choices");
            if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                JsonNode firstChoiceNode = choicesNode.get(0);
                JsonNode messageNode = firstChoiceNode.path("message");
                String conteudoResposta = messageNode.get("content").asText();
                conteudoResposta = conteudoResposta.substring(conteudoResposta.indexOf('\n') + 1); // remove first line
                conteudoResposta = conteudoResposta.substring(0, conteudoResposta.lastIndexOf('\n')); // remove last line
                JsonNode gruposDeProdutoMercado = mapper.readTree(conteudoResposta);
                for (JsonNode grupo : gruposDeProdutoMercado) {
                    ProdutoDTO novoProduto = criaGrupoProdutosDoMercado(grupo);
                    ProdutoDAO produtoDAO = new ProdutoDAO();
                    produtoDAO.insertGrupo(novoProduto);
                    novoGrupoProdutoMercado.add(novoProduto);
                    System.out.println("novoProduto" + novoProduto);
                    atualizaIdProdutoDoGrupo(grupo, novoProduto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return novoGrupoProdutoMercado;
    }

    private ProdutoDTO criaGrupoProdutosDoMercado(JsonNode grupo) throws SQLException {
        ProdutoDTO novoGrupoProdutoMercado = new ProdutoDTO();
        String idGrupoUUID = UUID.randomUUID().toString();
        novoGrupoProdutoMercado.setId(idGrupoUUID);
        JsonNode idProdutos = grupo.get("id_produto");
        if (idProdutos != null && idProdutos.isArray() && !idProdutos.isEmpty()) {
            int idPrimeiroProduto = idProdutos.get(0).asInt();
            Produto_MercadoDTO produto = produtoMercadoDAO.getProduto(idPrimeiroProduto);
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
