package com.cedup.super_preco.controller.produto;

import com.cedup.super_preco.model.produto.ProdutoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdutoConverter {
    public List<ProdutoDTO> toDTO (List<ProdutoEntity> entities){

        return entities //
                .stream() //
                .map(entity -> new ProdutoDTO(entity.id_produto, entity.nome)) //
                .collect(Collectors.toList());
    }

    public ProdutoDTO toDTO(ProdutoEntity entity){
        return new ProdutoDTO(entity.id_produto, entity.nome);
    }

    public ProdutoEntity toEntity(ProdutoDTO dto){
        return new ProdutoEntity(dto.id, dto.nome);
    }
}
