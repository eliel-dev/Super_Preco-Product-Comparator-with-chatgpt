package com.cedup.super_preco.controller.mercado;

import com.cedup.super_preco.model.mercado.MercadoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/mercado/")
@CrossOrigin(origins = "*")
public class MercadoController {
    @Autowired
    MercadoDAO mercadoDAO;
    @Autowired
    MercadoConverter mercadoConverter;

    @GetMapping
    public ResponseEntity<List<MercadoDTO>> getMercados() throws SQLException {
        return ResponseEntity.ok(mercadoConverter.toDTO(mercadoDAO.getAll()));
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
