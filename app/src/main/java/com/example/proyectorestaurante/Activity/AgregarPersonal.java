package com.example.proyectorestaurante.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectorestaurante.ConexionDB;
import com.example.proyectorestaurante.R;

import java.sql.Connection;
import java.sql.Statement;

public class AgregarPersonal extends AppCompatActivity {

    EditText nombre,apellido,cargo,direccion,telefono,dni;
    Button btninsert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_personal);
        nombre = findViewById(R.id.txtNombre);
        apellido = findViewById(R.id.txtApellido);
        cargo= findViewById(R.id.txtCargo);
        direccion= findViewById(R.id.txtDireccion);
        telefono = findViewById(R.id.txtTelefono);
        dni=findViewById(R.id.txtdni);


        btninsert =findViewById(R.id.btnAdd);

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_nombre = nombre.getText().toString();
                String txt_apellido = apellido.getText().toString();
                String txt_cargo = cargo.getText().toString();
                String txt_direccion = direccion.getText().toString();
                String txt_telefono = telefono.getText().toString();
                String txt_dni =dni.getText().toString();
                Connection connection = ConexionDB.obtenerConexion();
                String imagen_name= "predet.jpg";
                try {
                    if (connection != null) {
                        String sqlinsert="INSERT INTO personal (nombre, apellido, cargo, direccion, telefono,dni)\n" +
                                "VALUES ('"+txt_nombre+"', '"+txt_apellido+"','"+txt_cargo+"','"+txt_direccion+"','"+txt_telefono+"','"+txt_dni+"')";
                        Statement st = connection.createStatement();
                        int rowsAffected = st.executeUpdate(sqlinsert);
                        if (rowsAffected > 0) {
                            Toast.makeText(getApplicationContext(), "Personal agregado exitosamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "No se pudo agregar el personal", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}