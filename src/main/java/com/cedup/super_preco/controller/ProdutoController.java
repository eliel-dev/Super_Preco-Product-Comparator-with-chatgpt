package com.cedup.super_preco.controller;

import com.cedup.super_preco.model.MercadoDTO;
import com.cedup.super_preco.model.ProdutoDTO;
import com.cedup.super_preco.model.dao.MercadoDAO;
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

    List<ProdutoDTO> produtoDTOS = new ArrayList<>();
    @GetMapping
    public List<ProdutoDTO> getMercados() throws SQLException {

        produtoDTOS = produtoDAO.getProdutos();

        return produtoDTOS;
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
