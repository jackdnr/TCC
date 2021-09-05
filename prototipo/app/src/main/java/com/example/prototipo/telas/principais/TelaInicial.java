package com.example.prototipo.telas.principais;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.prototipo.R;
import com.example.prototipo.telas.parque.TelaCadastroParques;
import com.example.prototipo.telas.parque.TelaFotoElemento;
import com.example.prototipo.telas.parque.TelaListaElementos;
import com.example.prototipo.telas.parque.TelaListaParques;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class TelaInicial extends AppCompatActivity {
    ExtendedFloatingActionButton deslogar;
    ExtendedFloatingActionButton novo;
    ExtendedFloatingActionButton lista;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
        getSupportActionBar().hide();
        deslogar = (ExtendedFloatingActionButton) findViewById(R.id.botaoSair);
        novo = (ExtendedFloatingActionButton) findViewById(R.id.botaoNovoParque);
        lista = (ExtendedFloatingActionButton) findViewById(R.id.botaoListarParques);
        mAuth = FirebaseAuth.getInstance();
        deslogar.setOnClickListener( view->{
            mAuth.signOut();
            Intent i = new Intent(TelaInicial.this, MainActivity.class);
            startActivity(i);
        });
        novo.setOnClickListener(view ->{
            Intent i = new Intent(TelaInicial.this, TelaCadastroParques.class);
            startActivity(i);
        });
        lista.setOnClickListener(view ->{
            Intent i = new Intent(TelaInicial.this, TelaListaParques.class);
            startActivity(i);
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(TelaInicial.this, MainActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}