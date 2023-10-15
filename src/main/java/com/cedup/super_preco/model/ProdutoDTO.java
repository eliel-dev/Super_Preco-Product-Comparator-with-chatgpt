package com.cedup.super_preco.model;


public class ProdutoDTO {
    public int id_produto;
    public int id_mercado;
    public int id_grupo;
    public String nome;
    public double preco;
    public String link;
    public String link_img;

    public ProdutoDTO(int id_produto, int id_mercado, int id_grupo, String nome, double preco, String link, String link_img) {
        this.id_produto = id_produto;
        this.id_mercado = id_mercado;
        this.id_grupo = id_grupo;
        this.nome = nome;
        this.preco = preco;
        this.link = link;
        this.link_img = link_img;
    }

    public ProdutoDTO(int id_produto, int id_mercado, String nome) {
        this.id_produto = id_produto;
        this.id_mercado = id_mercado;
        this.nome = nome;
    }

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public int getId_mercado() {
        return id_mercado;
    }

    public void setId_mercado(int id_mercado) {
        this.id_mercado = id_mercado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink_img() {
        return link_img;
    }

    public void setLink_img(String link_img) {
        this.link_img = link_img;
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }
}
