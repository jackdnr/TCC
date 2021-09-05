package com.example.prototipo.classes.objetos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Parque {
    ArrayList<Elemento> elementos = new ArrayList<Elemento>();
    String nome;
    String estado;
    String cidade;
    String bairro;
    String userId;
    String descricao;
    int cont = 1;

    public Parque(String nome, String estado, String cidade, String bairro, String userId, String descricao) {
        this.nome = nome;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.userId = userId;
        this.descricao = descricao;
    }

    public Parque() {
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void incrementaCont() {
        this.cont++;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList getElementos() {
        return elementos;
    }

    public String getNome() {
        return nome;
    }

    public String getEstado() {
        return estado;
    }

    public String getCidade() {
        return cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCont() {
        return cont;
    }

    public Parque(ArrayList<Elemento> elementos, String nome, String estado, String cidade, String bairro, String userId, String descricao, int cont) {
        this.elementos = elementos;
        this.nome = nome;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.userId = userId;
        this.descricao = descricao;
        this.cont = cont;
    }

    public void salvar() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("parques").child(nome).setValue(this);
    }
}
