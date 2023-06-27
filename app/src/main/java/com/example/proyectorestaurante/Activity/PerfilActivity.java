package com.example.proyectorestaurante.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.proyectorestaurante.Clases.Usuario;
import com.example.proyectorestaurante.R;


public class PerfilActivity extends AppCompatActivity {

    TextView nombre, apellido, correo, dni;

    int idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nombre = findViewById(R.id.nombrepe);
        apellido = findViewById(R.id.apellidope);
        correo = findViewById(R.id.correope);
        dni = findViewById(R.id.dni);

        idUsuario =1;

        Usuario usuario = Usuario.obtenerUsuario(idUsuario);
        if (usuario != null) {
            nombre.setText("Nombres: "+usuario.getNombre());
            apellido.setText("Apellidos: "+usuario.getApellido());
            correo.setText("correo: "+usuario.getCorreo());
            dni.setText("DNI: "+usuario.getDni());
        } else {
            nombre.setText("Usuario no encontrado");
            apellido.setText("Usuario no encontrado");
            correo.setText("Usuario no encontrado");
            dni.setText("Usuario no encontrado");
        }



    }
    public void irAInicio(View view) {
        Intent intent = new Intent(PerfilActivity.this, PrincipalActivity.class);
        startActivity(intent);
    }
    public void irOtroActivity(View view) {
        Intent intent = new Intent(PerfilActivity.this, PerfilActivity.class);
        startActivity(intent);
    }
    public void irAPlatos(View view) {
        Intent intent = new Intent(PerfilActivity.this, Crud_Platos.class);
        startActivity(intent);
    }
    public void irAPersonal(View view) {
        Intent intent = new Intent(PerfilActivity.this, Crud_Personal.class);
        startActivity(intent);
    }
    public void irAQR(View view) {
        Intent intent = new Intent(PerfilActivity.this, QRActivity.class);
        startActivity(intent);
    }
}
