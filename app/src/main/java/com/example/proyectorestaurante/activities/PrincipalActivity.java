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
import com.example.proyectorestaurante.utils.SessionManager;

public class PrincipalActivity extends AppCompatActivity {
    private SessionManager sessionManager;

    ViewFlipper v_fliper;
    TextView nombre;
    TextView roltxt;


    private nombreprincipal nombreprincipal = new nombreprincipal();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getApplicationContext());

        setContentView(R.layout.activity_principal);
        roltxt = findViewById(R.id.rolTextView);

        int images[] = {R.drawable.carrusel1, R.drawable.carrusel2, R.drawable.img_principal};

        v_fliper = findViewById(R.id.carrusel);

        for(int image: images){
            flipperImages(image);
        }

        nombre = findViewById(R.id.nombreP);

        String nombreUsuario = nombreprincipal.obtenerNombreUsuario(getApplicationContext());

        if(nombreUsuario != null){
            nombre.setText("Bienvenido "+nombreUsuario);
            String rol=sessionManager.obtenerRol();
            roltxt.setText(rol);
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
        Intent intent = new Intent(PrincipalActivity.this, Geolocalizador.class);
        startActivity(intent);
    }
    public void irOtroActivity(View view) {
        Intent intent = new Intent(PrincipalActivity.this, PerfilActivity.class);
        startActivity(intent);
    }

}