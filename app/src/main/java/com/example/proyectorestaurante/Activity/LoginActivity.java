package com.example.proyectorestaurante.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.icu.text.ConstrainedFieldPosition;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectorestaurante.Clases.classlogin;
import com.example.proyectorestaurante.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {


    TextInputLayout txusuarioLayout, txpasswordLayout;
    TextInputEditText txusuario,txpassword;
    Button gobtn;

    int numfallidos = 0;
    private classlogin objlogin= new classlogin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txusuarioLayout = findViewById(R.id.usuario);
        txpasswordLayout = findViewById(R.id.contraseña);
        txusuario= (TextInputEditText) txusuarioLayout.getEditText();
        txpassword= (TextInputEditText) txpasswordLayout.getEditText();
        gobtn = (Button) findViewById(R.id.buttoning);
        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = (txusuario.getText().toString()).trim();
                String password = (txpassword.getText().toString()).trim();
                new Validarusuario().execute(usuario, password);
            }
        });


    }

    private class Validarusuario extends AsyncTask<String, Void, Boolean > {
        @Override
        protected Boolean doInBackground(String... params) {
            String usuario = params[0];
            String password = params[1];
            return classlogin.validarCredenciales(usuario, password);
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            if (resultado) {
                Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                startActivity(intent);
                // Realizar la acción correspondiente al inicio de sesión exitoso
            } else {
                Toast.makeText(LoginActivity.this, "Usuario o contraseña inválidos", Toast.LENGTH_LONG).show();
            }
        }
    }

}