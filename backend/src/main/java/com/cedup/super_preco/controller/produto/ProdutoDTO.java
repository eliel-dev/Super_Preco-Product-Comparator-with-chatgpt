package com.cedup.super_preco.controller.produto;

import java.util.List;

public class ProdutoDTO {
    public String id;
    public String nome;
    public List<Integer> idProdutos; // nova lista de IDs de produtos

    public ProdutoDTO(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public ProdutoDTO() {
    }

    @Override
    public String toString() {
        return "Grupo ID: " + id + ", Nome: " + nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Integer> getIdProdutos() {
        return idProdutos;
    }

    public void setIdProdutos(List<Integer> idProdutos) {
        this.idProdutos = idProdutos;
    }
}
