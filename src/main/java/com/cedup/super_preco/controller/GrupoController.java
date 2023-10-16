package com.cedup.super_preco.controller;

import com.cedup.super_preco.model.GrupoDTO;
import com.cedup.super_preco.model.ProdutoDTO;
import com.cedup.super_preco.model.dao.GrupoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping ("/grupo")
public class GrupoController {
    @Autowired
    GrupoDAO grupoDAO;

    List<GrupoDTO> grupoDTOS = new ArrayList<>();
    @GetMapping
    public List<GrupoDTO> getGrupos() throws SQLException {

        grupoDTOS = grupoDAO.getGrupos();

        return grupoDTOS;
    }

    @GetMapping("/{id}")
    public GrupoDTO getGrupo(@PathVariable("id") int id) throws SQLException {

        GrupoDTO grupo2 = grupoDAO.getGrupo(id);

        return grupo2;
    }

    @PostMapping
    public GrupoDTO postGrupo(@RequestBody GrupoDTO dto) throws SQLException {

        grupoDAO.postGrupo(dto);

        return dto;
    }

    @DeleteMapping("/{id}")
    public GrupoDTO deletGrupo(@PathVariable("id") int id) throws SQLException {
        GrupoDTO grupoDTO = new GrupoDAO().getGrupo(id);

        grupoDAO.deleteGrupo(id);

        return grupoDTO;
    }

    @PutMapping("{id}")
    public GrupoDTO putGrupo(@PathVariable ("id") int id, @RequestBody GrupoDTO dto) throws SQLException {

        dto.setId(id);

        grupoDAO.putGrupo(dto);

        return dto;
    }
}
