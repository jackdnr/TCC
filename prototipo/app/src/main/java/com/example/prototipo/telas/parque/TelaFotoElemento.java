package com.example.prototipo.telas.parque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prototipo.R;
import com.example.prototipo.classes.objetos.Elemento;

import com.example.prototipo.classes.models.ModelParque;
import com.example.prototipo.classes.objetos.Parque;
import com.example.prototipo.telas.principais.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class TelaFotoElemento extends AppCompatActivity {
    Bitmap img;
    AlertDialog.Builder adb;
    static ModelParque mp;
    static Elemento me;
    String userId;
    SharedPreferences preferences;
    ImageView iv;
    StorageReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_foto_elemento);
        reference = FirebaseStorage.getInstance().getReference().child(me.getCaminhoFoto());
        carregaFoto();
        adb = new AlertDialog.Builder(this);
        preferences = getSharedPreferences("Pref", MODE_PRIVATE);
        userId = preferences.getString("userid", "");
        getSupportActionBar().hide();
        iv = findViewById(R.id.fotoElemento);
    }

    public void deleta(View view) {
        adb.setTitle("Excluir");
        adb.setMessage("Deseja excluir este item?");
        adb.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(TelaFotoElemento.this, "\uD83D\uDE0E", Toast.LENGTH_SHORT).show();
            }
        });
        adb.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImagem(me);
                for (Elemento e : mp.getElementos()) {
                    if (e.getNome() == me.getNome()) {
                        mp.getElementos().remove(e);
                        break;
                    }
                }
                atualizaParque();
                telaParque();
            }
        });
        adb.show();
    }

    public void deleteImagem(Elemento me) {
        StorageReference storageRef = FirebaseStorage.getInstance()
                .getReference()
                .child(me.getCaminhoFoto());
        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(TelaFotoElemento.this, "Item removido com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TelaFotoElemento.this, "Falha ao remover Item!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void telaParque() {
        Intent i = new Intent(TelaFotoElemento.this, TelaParque.class);
        startActivity(i);
        TelaParque.mp = mp;
    }

    public void atualizaParque() {
        Parque p = new Parque(mp.getElementos(), mp.getNome(), mp.getEstado(), mp.getCidade(), mp.getBairro(), userId, mp.getDescricao(), 5);
        p.salvar();
    }

    public void addInfo(View view) {
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Editar Informações");
        final EditText input = new EditText(this);
        input.setText(me.getInfos());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        adb.setView(input);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                me.setInfos(input.getText().toString());
                for (Elemento e : mp.getElementos()) {
                    if(e.getCaminhoFoto().equals(me.getCaminhoFoto())){
                        e.setInfos(me.getInfos());
                        break;
                    }
                }
                atualizaParque();
                telaParque();
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

    public void exibeInfo(View view) {
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Informações");
        adb.setMessage(me.getInfos());
        adb.show();
    }
    public void carregaFoto(){
        final File localFile;
        try {
            localFile = File.createTempFile("foto","jpg");
            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    img = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    iv.setImageBitmap(img);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TelaFotoElemento.this,"Imagem não pôde ser carregada",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
    public void wiki(View view){
        Intent i = new Intent(TelaFotoElemento.this, TelaWiki.class);
        startActivity(i);
        TelaWiki.url = "https://pt.wikipedia.org/wiki/"+me.getNome();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(TelaFotoElemento.this, TelaListaElementos.class);
        startActivity(i);
        super.onBackPressed();
    }
}