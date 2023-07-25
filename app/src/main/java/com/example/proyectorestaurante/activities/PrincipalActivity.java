package com.example.proyectorestaurante.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.proyectorestaurante.auth.nombreprincipal;
import com.example.proyectorestaurante.R;

public class PrincipalActivity extends AppCompatActivity {

    ViewFlipper v_fliper;
    TextView nombre;
    int idUsuario;

    private nombreprincipal nombreprincipal = new nombreprincipal();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        int images[] = {R.drawable.carrusel1, R.drawable.carrusel2, R.drawable.img_principal};

        v_fliper = findViewById(R.id.carrusel);

        for(int image: images){
            flipperImages(image);
        }

        nombre = findViewById(R.id.nombreP);
        idUsuario = 1;

        String nombreUsuario = nombreprincipal.obtenerNombreUsuario(idUsuario);
        if(nombreUsuario != null){
            nombre.setText("Bienvenido "+nombreUsuario);
        }else{
            nombre.setText("Nombre no encontrado");
        }


    }

    public void flipperImages(int image){
        ImageView imageView = new ImageView(this);

        imageView.setBackgroundResource(image);

        v_fliper.addView(imageView);
        v_fliper.setFlipInterval(3000);
        v_fliper.setAutoStart(true);
        v_fliper.setOutAnimation(this, android.R.anim.slide_out_right);
        v_fliper.setInAnimation(this, android.R.anim.slide_in_left);

    }

    public void irAInicio(View view) {
        Intent intent = new Intent(PrincipalActivity.this, PrincipalActivity.class);
        startActivity(intent);
    }
    public void irOtroActivity(View view) {
        Intent intent = new Intent(PrincipalActivity.this, PerfilActivity.class);
        startActivity(intent);
    }
    public void irAPlatos(View view) {
        Intent intent = new Intent(PrincipalActivity.this, Crud_Platos.class);
        startActivity(intent);
    }
    public void irAPersonal(View view) {
        Intent intent = new Intent(PrincipalActivity.this, Crud_Personal.class);
        startActivity(intent);
    }
    public void irAQR_principal(View view) {
        Intent intent = new Intent(PrincipalActivity.this, QRActivity.class);
        startActivity(intent);
    }
}