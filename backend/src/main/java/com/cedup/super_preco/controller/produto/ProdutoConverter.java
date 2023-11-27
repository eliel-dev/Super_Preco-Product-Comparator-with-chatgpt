package com.cedup.super_preco.controller.produto;

import com.cedup.super_preco.model.produto.ProdutoEntity;
import com.cedup.super_preco.model.produto.Produto_MercadoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdutoConverter {

    public ProdutoEntity toEntity(ProdutoDTO dto) {
        return new ProdutoEntity(dto.id, dto.nome);
    }

    public List<Produto_MercadoDTO> toDTO(List<Produto_MercadoEntity> entities) {

        return entities //
                .stream() //
                .map(entity -> new Produto_MercadoDTO(entity.id_produto_mercado, entity.id_mercado.id_mercado, entity.id_produto.id_produto, entity.nome, entity.preco, entity.volume, entity.link, entity.link_img)) //
                .collect(Collectors.toList());
    }

    public Produto_MercadoDTO toDTO(Produto_MercadoEntity entity) {
        return new Produto_MercadoDTO(entity.id_produto_mercado, entity.id_mercado.id_mercado, entity.id_produto.id_produto, entity.nome, entity.preco,entity.volume, entity.link, entity.link_img);
    }


}
