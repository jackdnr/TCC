package com.example.prototipo.telas.parque;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototipo.R;
import com.example.prototipo.classes.objetos.Elemento;
import com.example.prototipo.classes.models.ModelParque;
import com.example.prototipo.classes.objetos.Parque;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TelaCadastroElemento extends AppCompatActivity {
    static ModelParque mp;
    TextInputEditText nome;
    RadioButton vegetal;
    RadioButton animal;
    RadioButton facilidades;
    RadioButton outroElementoNatural;
    RadioButton outro;
    ArrayList<Elemento> list;
    CircleImageView foto;
    StorageReference storageRef;
    Bitmap imagem;
    String caminhofoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro_elemento);
        acessoCamera();
        getSupportActionBar().hide();
        try {
            list = mp.getElementos();
        } catch (Exception e) {
            list = new ArrayList<Elemento>();
        }
        storageRef = FirebaseStorage.getInstance().getReference();
        nome = findViewById(R.id.nomeE);
        vegetal = findViewById(R.id.radioVegetal);
        animal = findViewById(R.id.radioAnimal);
        facilidades = findViewById(R.id.radioFacilidade);
        outroElementoNatural = findViewById(R.id.radioOutroElementoNatural);
        outro = findViewById(R.id.radioOutro);
        foto = findViewById(R.id.foto);
        trataRadioButtons();
    }

    public void foto(View view) {
        Toast.makeText(TelaCadastroElemento.this, "Tirar foto em modo RETRATO", Toast.LENGTH_LONG).show();
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 1);
    }

    public void cadastra(View view) {
        if (imagem != null && nome.getText() != null && (vegetal.isChecked() ||
                animal.isChecked() || facilidades.isChecked() ||
                outroElementoNatural.isChecked() || outro.isChecked())) {
            atualizaCaminhoFoto();
            String tipo = "";
            if (vegetal.isChecked()) {
                tipo = "Vegetal";
            } else if (animal.isChecked()) {
                tipo = "Animal";
            } else if (facilidades.isChecked()) {
                tipo = "Facilidade";
            } else if (outroElementoNatural.isChecked()) {
                tipo = "Outro elemento natural";
            } else {
                tipo = "Outro";
            }

            Elemento e = new Elemento(nome.getText().toString(), tipo, caminhofoto);
            list.add(e);
            SharedPreferences preferences = getSharedPreferences("Pref", MODE_PRIVATE);
            String userId = preferences.getString("userid", "");
            Parque p = new Parque(list, mp.getNome(), mp.getEstado(), mp.getCidade(), mp.getBairro(), userId, mp.getDescricao(), 5);
            p.salvar();
            telaParque();
        } else {
            Toast.makeText(TelaCadastroElemento.this, "Você deixou algum campo em branco,\nou esqueceu de tirar foto", Toast.LENGTH_LONG).show();
        }
    }

    public void telaParque() {
        Intent i = new Intent(TelaCadastroElemento.this, TelaParque.class);
        startActivity(i);
        TelaParque.mp = mp;
    }

    public void acessoCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA
            }, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imagem = rotateBitmap((Bitmap) extras.get("data"), 90);

            foto.setImageBitmap(imagem);
        }
    }

    public void atualizaCaminhoFoto() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte bb[] = bytes.toByteArray();
        uploadToFirebase(bb);
    }

    private void uploadToFirebase(byte[] bb) {
        int i = 0;
        String nomeFoto = nome.getText().toString();
        //while(verificaNomeFoto(nomeFoto)){
        //    i++;
        //}
        nomeFoto += i;
        caminhofoto = "myimages/" + nomeFoto +".jpg";
        StorageReference sr = storageRef.child(caminhofoto);
        sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(TelaCadastroElemento.this, "Upload feito com sucesso!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TelaCadastroElemento.this, "Algo deu errado!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public boolean verificaNomeFoto(String name){
        for(Elemento e : list){
            if(e.getNome().equals(name)){
                return true;
            }
        }
        return false;
    }
    public void trataRadioButtons(){
        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal.setChecked(true);
                vegetal.setChecked(false);
                facilidades.setChecked(false);
                outroElementoNatural.setChecked(false);
                outro.setChecked(false);
            }
        });
        vegetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal.setChecked(false);
                vegetal.setChecked(true);
                facilidades.setChecked(false);
                outroElementoNatural.setChecked(false);
                outro.setChecked(false);
            }
        });
        facilidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal.setChecked(false);
                vegetal.setChecked(false);
                facilidades.setChecked(true);
                outroElementoNatural.setChecked(false);
                outro.setChecked(false);
            }
        });
        outroElementoNatural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal.setChecked(false);
                vegetal.setChecked(false);
                facilidades.setChecked(false);
                outroElementoNatural.setChecked(true);
                outro.setChecked(false);
            }
        });
        outro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animal.setChecked(false);
                vegetal.setChecked(false);
                facilidades.setChecked(false);
                outroElementoNatural.setChecked(false);
                outro.setChecked(true);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(TelaCadastroElemento.this, TelaParque.class);
        startActivity(i);
        super.onBackPressed();
    }
}