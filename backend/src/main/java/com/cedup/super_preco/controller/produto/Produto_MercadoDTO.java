package com.cedup.super_preco.controller.produto;


public class Produto_MercadoDTO {
    public int id_produto_mercado;
    public int id_mercado;
    public String id_produto;
    public String nome;
    public double preco;
    public String link;
    public String link_img;

    public Produto_MercadoDTO(int id_produto_mercado, int id_mercado, String id_produto, String nome, double preco, String link, String link_img) {
        this.id_produto_mercado = id_produto_mercado;
        this.id_mercado = id_mercado;
        this.id_produto = id_produto;
        this.nome = nome;
        this.preco = preco;
        this.link = link;
        this.link_img = link_img;
    }

    public Produto_MercadoDTO(int id_produto_mercado, int id_mercado, String nome) {
        this.id_produto_mercado = id_produto_mercado;
        this.id_mercado = id_mercado;
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "\n id_produto: " + id_produto_mercado + " nome: " + nome;

    }


    public int getId_produto_mercado() {
        return id_produto_mercado;
    }

    public void setId_produto_mercado(int id_produto_mercado) {
        this.id_produto_mercado = id_produto_mercado;
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

    public String getId_produto() {
        return id_produto;
    }

    public void setId_produto(String id_produto) {
        this.id_produto = id_produto;
    }
}
