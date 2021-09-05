package com.example.prototipo.telas.parque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.prototipo.R;

public class TelaWiki extends AppCompatActivity {
    static String url;
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_wiki);
        getSupportActionBar().hide();
        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(TelaWiki.this, TelaFotoElemento.class);
        startActivity(i);
        super.onBackPressed();
    }
}