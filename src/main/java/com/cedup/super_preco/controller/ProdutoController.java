package com.cedup.super_preco.controller;

import com.cedup.super_preco.ChatGPT;
import com.cedup.super_preco.ScrapeCooper;
import com.cedup.super_preco.ScrapeKoch;
import com.cedup.super_preco.model.ProdutoDTO;
import com.cedup.super_preco.model.dao.ProdutoDAO;
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
    public String testOpenAI() {
        // limite de token = https://platform.openai.com/docs/models/gpt-3-5

        ProdutoDTO produto1 = new ProdutoDTO(1, 1, "refrigerante sem acucar coca-cola garrafa 1,5l");
        ProdutoDTO produto2 = new ProdutoDTO(6, 2,  "refrigerante coca cola zero pet 1.5l");

        // Crie o prompt para a API da OpenAI
        String prompt =  produto1.nome + produto2.nome;

        // Obter a resposta da API da OpenAI
        String response = chatGPT.getOpenAIResponse(prompt);

        return response;
    }

    List<ProdutoDTO> produtoDTOS = new ArrayList<>();
    @GetMapping
    public List<ProdutoDTO> getProdutos() throws SQLException {

        produtoDTOS = produtoDAO.getProdutos();

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
