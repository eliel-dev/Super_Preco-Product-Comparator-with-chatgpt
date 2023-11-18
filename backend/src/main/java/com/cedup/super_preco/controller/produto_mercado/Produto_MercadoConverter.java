package com.cedup.super_preco.controller.produto_mercado;

import com.cedup.super_preco.model.mercado.MercadoEntity;
import com.cedup.super_preco.model.produto.ProdutoEntity;
import com.cedup.super_preco.model.produto_mercado.Produto_MercadoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Produto_MercadoConverter {

    public List<Produto_MercadoDTO> toDTO (List<Produto_MercadoEntity> entities){

        return entities //
                .stream() //
                .map(entity -> new Produto_MercadoDTO(entity.id_produto_mercado, entity.id_mercado.id_mercado, entity.id_produto.id_produto, entity.nome, entity.preco, entity.link, entity.link_img)) //
                .collect(Collectors.toList());
    }

    public Produto_MercadoDTO toDTO(Produto_MercadoEntity entity){
        return new Produto_MercadoDTO(entity.id_produto_mercado, entity.id_mercado.id_mercado, entity.id_produto.id_produto, entity.nome, entity.preco, entity.link, entity.link_img);
    }

    public Produto_MercadoEntity toEntity(Produto_MercadoDTO dto){
        return new Produto_MercadoEntity(dto.id_produto_mercado, new MercadoEntity(dto.id_mercado), new ProdutoEntity(dto.id_produto), dto.nome, dto.preco, dto.link, dto.link_img);
    }
}
