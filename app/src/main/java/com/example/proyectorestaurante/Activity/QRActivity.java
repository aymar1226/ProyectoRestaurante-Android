package com.example.proyectorestaurante.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.proyectorestaurante.GeneradorPDF;
import com.example.proyectorestaurante.R;

public class QRActivity extends AppCompatActivity {
    Button pdfButton;
    GeneradorPDF generadorPDF;
    ProgressBar progressBar;
    ImageView qrImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);
        pdfButton = findViewById(R.id.generatePdf);
        progressBar = findViewById(R.id.progressBar);
        qrImage = findViewById(R.id.qrCartilla);

        generadorPDF = new GeneradorPDF(QRActivity.this,progressBar,qrImage);

        pdfButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            generadorPDF.generarPdf();
        });
    }

}