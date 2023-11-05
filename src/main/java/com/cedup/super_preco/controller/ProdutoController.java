package com.cedup.super_preco.controller;

import com.cedup.super_preco.model.ProdutoDTO;
import com.cedup.super_preco.model.dao.ProdutoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping ("/grupo")
public class ProdutoController {
    @Autowired
    ProdutoDAO produtoDAO;

    List<ProdutoDTO> produtoDTOS = new ArrayList<>();
    @GetMapping
    public List<ProdutoDTO> getGrupos() throws SQLException {

        produtoDTOS = produtoDAO.getGrupos();

        return produtoDTOS;
    }

    @GetMapping("/{id}")
    public ProdutoDTO getGrupo(@PathVariable("id") String id) throws SQLException {

        ProdutoDTO grupo2 = produtoDAO.getGrupo(id);

        return grupo2;
    }

    @PostMapping
    public ProdutoDTO postGrupo(@RequestBody ProdutoDTO dto) throws SQLException {

        produtoDAO.insertGrupo(dto);

        return dto;
    }

    @DeleteMapping("/{id}")
    public ProdutoDTO deletGrupo(@PathVariable("id") String id) throws SQLException {
        ProdutoDTO produtoDTO = new ProdutoDAO().getGrupo(id);

        produtoDAO.deleteGrupo(id);

        return produtoDTO;
    }

    @PutMapping("{id}")
    public ProdutoDTO putGrupo(@PathVariable ("id") String id, @RequestBody ProdutoDTO dto) throws SQLException {

        dto.setId(id);

        produtoDAO.putGrupo(dto);

        return dto;
    }
}
