package com.cedup.super_preco.model.mercado;

public class MercadoEntity {
    public int id_mercado;
    public String nome;

    public MercadoEntity(int id_mercado) {
        this.id_mercado = id_mercado;
    }

    public MercadoEntity() {
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



}
