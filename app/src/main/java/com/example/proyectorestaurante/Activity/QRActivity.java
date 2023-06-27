package com.example.proyectorestaurante.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectorestaurante.FirebaseManager;
import com.example.proyectorestaurante.GeneradorPDF;
import com.example.proyectorestaurante.R;

import java.io.File;

public class QRActivity extends AppCompatActivity {

    ImageView qr;

    private FirebaseManager firebaseManager;
    private static final int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);
        qr = findViewById(R.id.qr);
        firebaseManager = new FirebaseManager(this);

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarCartillaPDF();

                if (ContextCompat.checkSelfPermission(QRActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    generatePdfAndUpload();
                } else {
                    ActivityCompat.requestPermissions(QRActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CODE);
                }

            }

        });
    }
    private void generarCartillaPDF() {
        // Verificar si se tienen los permisos necesarios para escribir en el almacenamiento externo
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permisos si no se tienen
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            // Generar el PDF
            GeneradorPDF.generarCartillaPDF(getApplicationContext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, generar el PDF
                GeneradorPDF.generarCartillaPDF(getApplicationContext());
            } else {
                // Permiso denegado, mostrar un mensaje o realizar alguna acción adicional
                Toast.makeText(this, "Permiso denegado para escribir en el almacenamiento externo", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void generatePdfAndUpload() {
        String fileName = "cartilla.pdf";
        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        // Verificar si el archivo se generó correctamente
        if (pdfFile != null && pdfFile.exists()) {
            // Subir el PDF a Firebase Storage
            firebaseManager.uploadPdfToFirebase(pdfFile);
            Toast.makeText(this, "Permiso denegado para escribir en el almacenamiento externo", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al generar el PDF", Toast.LENGTH_SHORT).show();
        }
    }
}