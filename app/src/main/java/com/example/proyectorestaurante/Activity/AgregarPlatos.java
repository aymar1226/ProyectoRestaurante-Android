package com.example.proyectorestaurante.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectorestaurante.ConexionDB;
import com.example.proyectorestaurante.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AgregarPlatos extends AppCompatActivity {

    EditText nombre,descripcion,precio_plato;
    Button btninsert;
    Spinner spinnerPlato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_platos);
        nombre = findViewById(R.id.txtNombre);
        descripcion = findViewById(R.id.txtDescripcion);
        precio_plato = findViewById(R.id.precio);
        btninsert =findViewById(R.id.btnAdd);
        spinnerPlato = findViewById(R.id.spinner_categoria);

        Connection connection = ConexionDB.obtenerConexion();

        //Obtener Categorias
        List<String> categoria;
        categoria = obtenerCategorias(connection);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoria);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlato.setAdapter(adapter);

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_nombre = nombre.getText().toString();
                String txt_descripcion = descripcion.getText().toString();
                double precio= Double.parseDouble(precio_plato.getText().toString());
                String txt_categoria = (String) spinnerPlato.getSelectedItem();
                Connection connection = ConexionDB.obtenerConexion();
                String imagen_name= "predet.jpg";
                try {
                    String categoriaquery="SELECT id_categoria FROM categoria where nombre LIKE '%"+txt_categoria+"%'";
                    Statement st = connection.createStatement();
                    ResultSet resultSet = st.executeQuery(categoriaquery);
                    String idcategoria="";
                    if (resultSet.next()) {
                        idcategoria = resultSet.getString("id_categoria");
                    }

                    if (connection != null) {
                        String sqlinsert="INSERT INTO plato (nombre, descripcion,precio,id_categoria)" +
                                "VALUES ('"+txt_nombre+"', '"+txt_descripcion+"','"+precio+"','"+idcategoria+"')";
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

    public List<String> obtenerCategorias(Connection connection) {

        List<String> listaCategorias = new ArrayList<>();

        try {
            String query = "SELECT nombre FROM categoria";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String categoria = resultSet.getString("nombre");
                listaCategorias.add(categoria);
            }
            statement.close();
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCategorias;

    }


}