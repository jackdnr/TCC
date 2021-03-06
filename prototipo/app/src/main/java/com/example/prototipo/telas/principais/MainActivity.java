package com.example.prototipo.telas.principais;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.prototipo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    AlertDialog.Builder ad;
    SignInButton botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        ad = new AlertDialog.Builder(this);
        botao = (SignInButton) findViewById(R.id.botao);
        botao.setOnClickListener(view -> {
            signIn();
        });
        requisita();
    }

    private void requisita() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                SharedPreferences.Editor editor = getApplicationContext()
                        .getSharedPreferences("Pref", MODE_PRIVATE)
                        .edit();
                editor.putString("username", account.getDisplayName());
                editor.putString("userid", account.getIdToken());
                editor.putString("useremail", account.getEmail());
                //editor.putString(("foto"),account.getPhotoUrl().toString());
                editor.apply();
            } catch (ApiException e) {
                ad.setTitle("Falha de Login!");
                ad.setMessage("Algo inesperado aconteceu!");
                ad.show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(MainActivity.this, TelaInicial.class);
                            startActivity(i);
                        } else {
                            ad.setTitle("Falha de Autentica????o!");
                            ad.setMessage("Usu??rio ou Senha Incorreta");
                            ad.show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(MainActivity.this, TelaInicial.class);
            startActivity(i);

        }
    }
    public void sobre(View view){
        ad.setTitle("Sobre o CAC");
        ad.setMessage("O Ci??ncia Ambiental Cidad?? ?? um aplicativo voltado ?? parques ambientais, "+
                "adotando temas como Ci??ncia Cidad?? e Crowdsourcing. Trata-se de um app aberto "+
                "?? comunidade, onde qualquer pessoa pode contribuir.\nProduzido como Trabalho "+
                "de Conclus??o do curso de Sistemas de Informa????o na Universidade Federal de Santa "+
                "Catarina, por Jackson Dener Wrublak.");
        ad.show();
    }
}