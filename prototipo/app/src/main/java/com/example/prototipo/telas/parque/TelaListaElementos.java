package com.example.prototipo.telas.parque;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import com.example.prototipo.R;
import com.example.prototipo.classes.adapters.AdapterElemento;
import com.example.prototipo.classes.objetos.Elemento;

import com.example.prototipo.classes.models.ModelParque;


import java.util.ArrayList;

public class TelaListaElementos extends AppCompatActivity {
    RecyclerView rv;
    AdapterElemento adapter;
    ArrayList<Elemento> list;
    static ModelParque mp;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_lista_elementos);
        adb = new AlertDialog.Builder(this);
        getSupportActionBar().hide();
        list = new ArrayList<Elemento>();
        rv = (RecyclerView) findViewById(R.id.rve);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        populaLista();
        adapter = new AdapterElemento(this, list, new AdapterElemento.OnItemClickListener() {
            @Override
            public void onItemClick(Elemento me) {
                Intent i = new Intent(TelaListaElementos.this,TelaFotoElemento.class);
                startActivity(i);
                TelaFotoElemento.me = me;
                TelaFotoElemento.mp = mp;
            }
        });
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void populaLista() {
        for (Elemento e : mp.getElementos()) {
            Elemento me = new Elemento(e.getNome(), e.getTipo(), e.getCaminhoFoto(), e.getInfos());
            list.add(me);
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(TelaListaElementos.this, TelaParque.class);
        startActivity(i);
        super.onBackPressed();
    }
}