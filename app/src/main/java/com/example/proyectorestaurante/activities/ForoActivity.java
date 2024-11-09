package com.example.proyectorestaurante.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectorestaurante.R;
import com.example.proyectorestaurante.dao.ForoDao;
import com.example.proyectorestaurante.utils.ConexionDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ForoActivity extends AppCompatActivity {

    private Button btnCrearForo;
    private ForoDao foroDao = new ForoDao(); // Declaración correcta de foroDao
    private EditText NombreForo, Descripcion; // Agrega las vistas de EditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro);

        btnCrearForo = findViewById(R.id.btnCrearForo); // Inicialización de btnCrearForo
        NombreForo = findViewById(R.id.NombreForo); // Inicialización de etNombreForo
        Descripcion = findViewById(R.id.Descripcion); // Inicialización de etDescripcion

        // Llama a la función que realiza la operación de creación y devuelve el resultado
        btnCrearForo.setOnClickListener(v -> {
            boolean resultado = crearForoOnClick();
            if (resultado) {
                Toast.makeText(ForoActivity.this, "Foro creado exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ForoActivity.this, "Error al crear foro", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // Función que maneja el clic del botón y devuelve un booleano
    private boolean crearForoOnClick() {
        String nombreForo = NombreForo.getText().toString();
        String descripcion = Descripcion.getText().toString();
        return foroDao.crearForo(nombreForo, descripcion); // Llamada al método de creación en foroDao
    }


}
