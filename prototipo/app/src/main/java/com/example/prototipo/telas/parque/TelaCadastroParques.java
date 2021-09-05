package com.example.prototipo.telas.parque;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prototipo.R;
import com.example.prototipo.classes.objetos.Elemento;
import com.example.prototipo.classes.objetos.Parque;
import com.example.prototipo.classes.utilitario.UtilitarioBanco;
import com.example.prototipo.telas.principais.TelaInicial;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class TelaCadastroParques extends AppCompatActivity {
    Parque p;
    ArrayList<Parque> parques;
    TextInputEditText nomeP;
    TextInputEditText estadoP;
    TextInputEditText cidadeP;
    TextInputEditText bairroP;
    TextInputEditText descricaoP;
    ExtendedFloatingActionButton botaoCadastraP;
    String userId;
    String descricao;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro_parques);
        getSupportActionBar().hide();
        SharedPreferences preferences = getSharedPreferences("Pref", MODE_PRIVATE);
        userId = preferences.getString("userid", "");
        parques = UtilitarioBanco.download();
        nomeP = (TextInputEditText) findViewById(R.id.nomeP);
        estadoP = (TextInputEditText) findViewById(R.id.estadoP);
        cidadeP = (TextInputEditText) findViewById(R.id.cidadeP);
        bairroP = (TextInputEditText) findViewById(R.id.bairroP);
        descricaoP = (TextInputEditText) findViewById(R.id.descricaoP);
        botaoCadastraP = (ExtendedFloatingActionButton) findViewById(R.id.botaoCadastraParque);
        botaoCadastraP.setOnClickListener(view -> {
            cadastra();
        });
    }

    private void cadastra() {
        String nome = nomeP.getText().toString();
        String estado = estadoP.getText().toString();
        String cidade = cidadeP.getText().toString();
        String bairro = bairroP.getText().toString();
        descricao = descricaoP.getText().toString();
        String mensagem = "";
        if (verificaSeExiste(nome, estado, cidade)) {
            if (!p.getUserId().equals(userId)) {
                mensagem = "O cadastro desse parque já foi solicitado!";
                if (p.getCont() == 4) {
                    p.incrementaCont();
                    mensagem = "Parque aprovado!";

                } else if (p.getCont() > 4) {
                    mensagem = "Esse parque já está cadastrado!";
                } else {
                    p.setUserId(userId);
                    p.incrementaCont();
                }
                editaInfos();
                p.salvar();
            } else {
                mensagem = "Você já solicitou o cadastro desse parque!";
            }
        } else {
            Parque p = new Parque(nome, estado, cidade, bairro, userId, descricao);
            p.salvar();
            mensagem = "Solicitação de cadastro feita com sucesso!";
            Intent i = new Intent(TelaCadastroParques.this, TelaInicial.class);
            startActivity(i);
        }
        Toast.makeText(TelaCadastroParques.this, mensagem, Toast.LENGTH_LONG).show();
    }

    public void editaInfos() {
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Solicitação de cadastro já realizada, por favor verifique as informações:");
        final EditText input = new EditText(this);
        input.setText(p.getDescricao());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        adb.setView(input);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                descricao = input.getText().toString();
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        adb.show();
    }

    private boolean verificaSeExiste(String nome, String estado, String cidade) {
        for (Parque parque : parques) {
            if (parque.getNome().toLowerCase().equals(nome.toLowerCase()) &&
                    parque.getEstado().toLowerCase().equals(estado.toLowerCase()) &&
                    parque.getCidade().toLowerCase().equals(cidade.toLowerCase())) {
                p = (Parque) parque;
                return true;
            }
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(TelaCadastroParques.this, TelaInicial.class);
        startActivity(i);
        super.onBackPressed();
    }
}