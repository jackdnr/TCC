package com.example.prototipo.telas.parque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SearchView;


import com.example.prototipo.R;
import com.example.prototipo.classes.adapters.AdapterParque;
import com.example.prototipo.classes.models.ModelParque;
import com.example.prototipo.classes.objetos.Elemento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TelaListaParques extends AppCompatActivity {
    RecyclerView rv;
    AdapterParque adapter;
    ArrayList<ModelParque> list;
    ArrayList<ModelParque> listFiltrada;
    EditText cidade;
    EditText elemento;


    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_lista_parques);
        getSupportActionBar().hide();
        reference = FirebaseDatabase.getInstance().getReference().child("parques");
        cidade = (EditText) findViewById(R.id.pesquisaCidade);
        elemento = (EditText) findViewById(R.id.pesquisaElemento);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        baixaListaDoBanco();
        preenche(list);
        defineFiltro();
    }

    private void baixaListaDoBanco() {
        list = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot dn : snapshot.getChildren()) {
                    ModelParque model = dn.getValue(ModelParque.class);
                    if(model.getCont()>4){
                        list.add(model);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
            }
        });
    }
    private void preenche(ArrayList<ModelParque> lista) {
        adapter = new AdapterParque(this, lista, new AdapterParque.OnItemClickListener() {
            @Override
            public void onItemClick(ModelParque mp) {
                telaParque(mp);
            }
        });
        rv.setAdapter(adapter);
    }
    public void telaParque(ModelParque p){
        Intent i = new Intent(TelaListaParques.this, TelaParque.class);
        startActivity(i);
        TelaParque.mp=p;
    }
    public void defineFiltro(){
        cidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isEmpty(elemento)){
                    listFiltrada = new ArrayList<>(list);
                }
                String textoDigitado = s.toString();
                ArrayList filtro = new ArrayList();
                for(ModelParque parque : listFiltrada){
                    if(parque.getCidade().toLowerCase().contains(textoDigitado.toLowerCase())){
                        filtro.add(parque);
                    }
                }
                if(isEmpty(elemento)){
                    listFiltrada = filtro;
                }
                preenche(filtro);
            }
        });
        elemento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isEmpty(cidade)){
                    listFiltrada = new ArrayList<>(list);
                }
                String textoDigitado = s.toString();
                ArrayList filtro = new ArrayList();
                for(ModelParque parque : listFiltrada){
                    for(Elemento elemento : parque.getElementos()) {
                        if (elemento.getNome().toLowerCase().contains(textoDigitado.toLowerCase())) {
                            filtro.add(parque);
                        }
                    }
                }
                if(isEmpty(cidade)){
                    listFiltrada = filtro;
                }
                preenche(filtro);
            }
        });
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}