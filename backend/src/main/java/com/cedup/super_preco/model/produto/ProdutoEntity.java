package com.cedup.super_preco.model.produto;

public class ProdutoEntity {
    public String id_produto;
    public String nome;

    public ProdutoEntity(String id_produto, String nome) {
        this.id_produto = id_produto;
        this.nome = nome;
    }

    public ProdutoEntity() {
    }
}
