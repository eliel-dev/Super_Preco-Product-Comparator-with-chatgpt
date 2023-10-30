package com.cedup.super_preco.model;

import java.util.List;

public class ProdutoDTO {
    private int id;
    private String nome;
    private List<Integer> idProdutos; // nova lista de IDs de produtos

    public ProdutoDTO(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public ProdutoDTO() {
    }

    @Override
    public String toString() {
        return "Grupo ID: " + id + ", Nome: " + nome;
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

    public List<Integer> getIdProdutos() {
        return idProdutos;
    }

    public void setIdProdutos(List<Integer> idProdutos) {
        this.idProdutos = idProdutos;
    }
}
