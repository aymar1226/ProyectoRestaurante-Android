package com.example.proyectorestaurante.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectorestaurante.utils.ConexionDB;
import com.example.proyectorestaurante.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AgregarPersonal extends AppCompatActivity {

    private EditText nombre, apellido, cargo, direccion, telefono, dni;
    private Button btninsert;
    private Spinner spinner_personal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_personal);
        nombre = findViewById(R.id.txtNombre);
        apellido = findViewById(R.id.txtApellido);
        spinner_personal = findViewById(R.id.spinner_personal);
        direccion = findViewById(R.id.txtDireccion);
        telefono = findViewById(R.id.txtTelefono);
        dni = findViewById(R.id.txtdni);
        btninsert = findViewById(R.id.btnAdd);

        spinner_personal.setPrompt("cargo");

        Connection connection = ConexionDB.obtenerConexion();

        //Cargar cargos en spinner
        List<String> cargo;
        cargo = obtenerCargos(connection);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cargo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_personal.setAdapter(adapter);

        //Crear personal
        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_nombre = nombre.getText().toString();
                String txt_apellido = apellido.getText().toString();
                String txt_direccion = direccion.getText().toString();
                String txt_cargo = (String) spinner_personal.getSelectedItem();
                String txt_telefono = telefono.getText().toString();
                String txt_dni = dni.getText().toString();
                Connection connection = ConexionDB.obtenerConexion();
                String imagen_name = "predet.jpg";
                try {
                    //Obtener el cargo del sepinner
                    String cargoquery="SELECT id_cargo FROM cargo where nombre_cargo LIKE '%"+txt_cargo+"%'";
                    Statement st = connection.createStatement();
                    ResultSet resultSet = st.executeQuery(cargoquery);
                    String idcargo="";
                    if (resultSet.next()) {
                         idcargo = resultSet.getString("id_cargo");
                    }
                    //Crear un nuevo personal
                    if (connection != null) {
                        String sqlinsert = "INSERT INTO personal (nombre, apellido, id_cargo, direccion, telefono,dni)" +
                                "VALUES ('" + txt_nombre + "', '" + txt_apellido + "','" + idcargo + "','" + txt_direccion + "','" + txt_telefono + "','" + txt_dni + "')";
                        int rowsAffected = st.executeUpdate(sqlinsert);
                        if (rowsAffected > 0) {
                            Toast.makeText(getApplicationContext(), "Personal agregado exitosamente", Toast.LENGTH_SHORT).show();
                            //Redirige al crud
                            Intent intent = new Intent(AgregarPersonal.this, Crud_Personal.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "No se pudo agregar el personal", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public List<String> obtenerCargos(Connection connection) {

        List<String> listaCargos = new ArrayList<>();

        try {
            String query = "SELECT nombre_cargo FROM cargo";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String cargo = resultSet.getString("nombre_cargo");
                listaCargos.add(cargo);
            }
            statement.close();
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
            return listaCargos;

    }

}