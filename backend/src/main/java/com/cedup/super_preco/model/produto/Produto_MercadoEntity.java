package com.cedup.super_preco.model.produto;

import com.cedup.super_preco.model.mercado.MercadoEntity;

public class Produto_MercadoEntity {
    public int id_produto_mercado;
    public MercadoEntity id_mercado;
    public ProdutoEntity id_produto;
    public String nome;
    public double preco;
    public String volume;
    public String link;
    public String link_img;


    public Produto_MercadoEntity(int id_produto_mercado, MercadoEntity id_mercado, ProdutoEntity id_produto, String nome, double preco, String volume, String link, String link_img) {
        this.id_produto_mercado = id_produto_mercado;
        this.id_mercado = id_mercado;
        this.id_produto = id_produto;
        this.nome = nome;
        this.preco = preco;
        this.volume = volume;
        this.link = link;
        this.link_img = link_img;
    }

    public Produto_MercadoEntity(int id_produto_mercado, MercadoEntity id_mercado, String nome, String volume) {
        this.id_produto_mercado = id_produto_mercado;
        this.id_mercado = id_mercado;
        this.nome = nome;
        this.volume = volume;
    }

    public Produto_MercadoEntity() {
    }

}
