package com.example.prototipo.classes.models;

import com.example.prototipo.classes.objetos.Elemento;

import java.util.ArrayList;

public class ModelParque {
    String nome, estado, cidade, bairro, descricao;
    int cont;
    ArrayList<Elemento> elementos = new ArrayList<Elemento>();

    public ModelParque() {
    }

    public ModelParque(String nome, String estado, String cidade, String bairro, String descricao) {
        this.nome = nome;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.descricao = descricao;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCont() {
        return cont;
    }

    public ArrayList<Elemento> getElementos() {
        return elementos;
    }


}
