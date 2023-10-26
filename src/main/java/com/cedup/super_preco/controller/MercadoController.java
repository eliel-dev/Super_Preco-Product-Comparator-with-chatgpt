package com.cedup.super_preco.controller;

import com.cedup.super_preco.model.MercadoDTO;
import com.cedup.super_preco.model.dao.MercadoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mercado")
public class MercadoController {
    @Autowired
    MercadoDAO mercadoDAO;

    List<MercadoDTO> mercadoDTOS = new ArrayList<>();
    @GetMapping
    public List<MercadoDTO> getMercados() throws SQLException {

        mercadoDTOS = mercadoDAO.getAll();

        return mercadoDTOS;
    }

    @GetMapping("/{id}")
    public MercadoDTO getGrupo(@PathVariable("id") int id) throws SQLException {

        MercadoDTO grupo2 = mercadoDAO.getMercado(id);

        return grupo2;
    }

    @PostMapping
    public MercadoDTO postGrupo(@RequestBody MercadoDTO dto) throws SQLException {

        mercadoDAO.insertMercado(dto);

        return dto;
    }

    @DeleteMapping("/{id}")
    public MercadoDTO deletGrupo(@PathVariable("id") int id) throws SQLException {
        MercadoDTO grupoDTO = new MercadoDAO().getMercado(id);

        mercadoDAO.deleteMercado(id);

        return grupoDTO;
    }

    @PutMapping("{id}")
    public MercadoDTO putGrupo(@PathVariable ("id") int id, @RequestBody MercadoDTO dto) throws SQLException {

        dto.setId(id);

        mercadoDAO.updateMercado(dto);

        return dto;
    }
}
