package com.example.prototipo.classes.objetos;

public class Elemento {
    String nome;
    String tipo;
    String caminhoFoto;
    String infos;

    public Elemento(String nome, String tipo, String caminhoFoto) {
        this.nome = nome;
        this.tipo = tipo;
        this.caminhoFoto = caminhoFoto;
    }

    public Elemento() {
    }

    public String getInfos() {
        return infos;
    }

    public Elemento(String nome, String tipo, String caminhoFoto, String infos) {
        this.nome = nome;
        this.tipo = tipo;
        this.caminhoFoto = caminhoFoto;
        this.infos = infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
