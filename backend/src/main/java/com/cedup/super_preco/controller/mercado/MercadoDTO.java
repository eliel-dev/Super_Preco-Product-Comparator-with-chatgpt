package com.cedup.super_preco.controller.mercado;


public class MercadoDTO {
    public int id;
    public String nome;

    public MercadoDTO(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
