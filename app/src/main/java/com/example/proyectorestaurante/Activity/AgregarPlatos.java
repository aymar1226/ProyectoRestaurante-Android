package com.example.proyectorestaurante.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectorestaurante.ConexionDB;
import com.example.proyectorestaurante.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AgregarPlatos extends AppCompatActivity {

    EditText nombre,descripcion,precio_plato;
    Button btninsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_platos);
        nombre = findViewById(R.id.txtNombre);
        descripcion = findViewById(R.id.txtDescripcion);
        btninsert =findViewById(R.id.btnAdd);
        precio_plato = findViewById(R.id.precio_plato);


        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_nombre = nombre.getText().toString();
                String txt_descripcion = descripcion.getText().toString();
                double precio= Double.parseDouble(precio_plato.getText().toString());
                Connection connection = ConexionDB.obtenerConexion();
                String imagen_name= "predet.jpg";
                try {
                    if (connection != null) {
                        String sqlinsert="INSERT INTO platos (nombre_plato, descripcion,precio,imagen_plato)\n" +
                                "VALUES ('"+txt_nombre+"', '"+txt_descripcion+"','"+precio+"','"+imagen_name+"')";
                        Statement st = connection.createStatement();
                        int rowsAffected = st.executeUpdate(sqlinsert);
                        if (rowsAffected > 0) {
                            Toast.makeText(getApplicationContext(), "Plato agregado exitosamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "No se pudo agregar el plato", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}