package com.example.prototipo.telas.parque;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototipo.R;
import com.example.prototipo.classes.models.ModelParque;
import com.example.prototipo.classes.objetos.Parque;
import com.example.prototipo.classes.utilitario.UtilitarioBanco;
import com.example.prototipo.telas.principais.TelaInicial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TelaParque extends AppCompatActivity {
    static ModelParque mp;
    TextView nomeParque;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_parque);
        getSupportActionBar().hide();
        adb = new AlertDialog.Builder(this);
        nomeParque = (TextView) findViewById(R.id.nomeParque);
        nomeParque.setText(mp.getNome());
    }

    public void local(View view) {
        adb.setTitle("Eu fico em:");
        adb.setMessage("Estado: " + mp.getEstado() + "\n" + "Cidade: " + mp.getCidade() + "\n" + "Bairro: " + mp.getBairro());
        adb.show();
    }

    public void descricao(View view) {
        adb.setTitle("Descrição:");
        adb.setMessage(mp.getDescricao());
        adb.show();
    }

    public void elementos(View view) {
        Intent i = new Intent(TelaParque.this, TelaListaElementos.class);
        startActivity(i);
        TelaListaElementos.mp = mp;
    }

    public void colaborar(View view) {
        Intent i = new Intent(TelaParque.this, TelaCadastroElemento.class);
        startActivity(i);
        TelaCadastroElemento.mp = mp;
    }

    public void deletaParque(View view) {
        SharedPreferences preferences = getSharedPreferences("Pref", MODE_PRIVATE);
        String userId = preferences.getString("userid", "");
        if(verificaUserid(userId)){
            Toast.makeText(TelaParque.this, "Você já solicitou a remoção deste parque!",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            if (mp.getCont() < 10) {

                Parque p = new Parque(mp.getElementos(), mp.getNome(), mp.getEstado(),
                        mp.getCidade(), mp.getBairro(), userId,
                        mp.getDescricao(), mp.getCont());
                p.incrementaCont();
                p.setUserId(userId);
                p.salvar();
                Toast.makeText(TelaParque.this, "Solicitação de remoção realizada com sucesso!",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent(TelaParque.this, TelaInicial.class);
                startActivity(i);
            } else {
                DatabaseReference reference = FirebaseDatabase.getInstance().
                        getReference().child("parques/" + mp.getNome());
                Toast.makeText(TelaParque.this, "Solicitação de remoção atendida com sucesso!",
                        Toast.LENGTH_SHORT).show();
                reference.removeValue();
                Intent i = new Intent(TelaParque.this, TelaInicial.class);
                startActivity(i);
            }
        }
    }
    public boolean verificaUserid(String userId){
        ArrayList<Parque> parques = UtilitarioBanco.download();
        for(Parque p : parques){
            if(p.getNome().equals(mp.getNome()) && p.getUserId().equals(userId)){
                return true;
            }
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(TelaParque.this, TelaListaParques.class);
        startActivity(i);
        super.onBackPressed();
    }
}