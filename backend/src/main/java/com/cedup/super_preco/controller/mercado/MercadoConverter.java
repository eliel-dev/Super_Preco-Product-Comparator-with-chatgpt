package com.cedup.super_preco.controller.mercado;

import com.cedup.super_preco.model.mercado.MercadoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MercadoConverter {
    public List<MercadoDTO> toDTO (List<MercadoEntity> entities){

        return entities //
                .stream() //
                .map(entity -> new MercadoDTO(entity.id_mercado, entity.nome)) //
                .collect(Collectors.toList());
    }

    public MercadoDTO toDTO(MercadoEntity entity){
        return new MercadoDTO(entity.id_mercado, entity.nome);
    }

    public MercadoEntity toEntity(MercadoDTO dto){
        return new MercadoEntity(dto.id, dto.nome);
    }
}
