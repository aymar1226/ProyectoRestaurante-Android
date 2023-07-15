package com.example.proyectorestaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectorestaurante.Activity.Crud_Personal;
import com.example.proyectorestaurante.Activity.Crud_Platos;
import com.example.proyectorestaurante.Activity.ModificarPersonal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Modificar_platos extends AppCompatActivity {

    EditText txtnombre,txtdescripcion,txtprecio;
    ImageView editarImage;
    Button actualizarButton;
    Spinner spinnerCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_platos);

        txtnombre = findViewById(R.id.editTextNombre);
        txtdescripcion = findViewById(R.id.editTextDescripcion);
        txtprecio = findViewById(R.id.editTextPrecio);

        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerCategoria.setEnabled(false);


        actualizarButton=findViewById(R.id.actualizarButton);
        editarImage = findViewById(R.id.editar_plato);

        clicEditar();
        Connection connection=ConexionDB.obtenerConexion();

        Intent intent = getIntent();
        int id_plato = intent.getIntExtra("id_plato", 0);
        if (id_plato!=0) {
            // Utiliza el valor de id_personal como desees en tu actividad
            Toast.makeText(getApplicationContext(), "Plato cargado correctamente", Toast.LENGTH_SHORT).show();
            obtenerPlato(connection,id_plato);

            //Spinner
            List<String> categoria;
            categoria = obtenerCategorias(connection);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoria);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategoria.setAdapter(adapter);

            clicActualizar(connection,id_plato);

        }else{
            Toast.makeText(getApplicationContext(), "id nulo", Toast.LENGTH_SHORT).show();
        }
    }

    public void clicEditar(){
        editarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarButton.setVisibility(View.VISIBLE);

                txtnombre.setEnabled(true);
                txtnombre.setTextColor(Color.BLACK);

                txtdescripcion.setEnabled(true);
                txtdescripcion.setTextColor(Color.BLACK);

                txtprecio.setEnabled(true);
                txtprecio.setTextColor(Color.BLACK);

                spinnerCategoria.setEnabled(true);
            }
        });
    }

    public void obtenerPlato(Connection connection,int id_plato){

        String query= "SELECT plato.nombre, descripcion, precio, categoria.nombre AS nombre_categoria FROM plato INNER JOIN categoria on plato.id_categoria = categoria.id_categoria WHERE id_plato = "+id_plato;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String descripcion = resultSet.getString("descripcion");
                String precio_plato = resultSet.getString("precio");
                String categoria = resultSet.getString("nombre_categoria");

                txtnombre.setText(nombre);
                txtdescripcion.setText(descripcion);
                txtprecio.setText(precio_plato);
                spinnerCategoria.setSelection(id_plato);
            }
            statement.close();
            resultSet.close();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

    public void clicActualizar(Connection connection,int id_plato){
        actualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtnombre.getText().toString();
                String descripcion = txtdescripcion.getText().toString();
                double precio= Double.parseDouble(txtprecio.getText().toString());
                String categoria = (String) spinnerCategoria.getSelectedItem();

                try {
                    //Obtener id_categoria
                    String categoriaquery="SELECT id_categoria FROM categoria where nombre LIKE '%"+categoria+"%'";
                    Statement st = connection.createStatement();
                    ResultSet resultSet = st.executeQuery(categoriaquery);
                    String idcategoria=null;
                    if (resultSet.next()) {
                        idcategoria = resultSet.getString("id_categoria");
                    }

                    //Actualizar plato
                    String query = "UPDATE plato SET nombre = '" + nombre + "', descripcion = '" + descripcion + "', precio = '" + precio + "',id_categoria = '"+idcategoria+"' where id_plato= " + id_plato;

                    int rowsAffected = st.executeUpdate(query);
                    if (rowsAffected > 0) {
                        Toast.makeText(getApplicationContext(), "Plato actualizado exitosamente", Toast.LENGTH_SHORT).show();
                        //Te redirige al crud plato
                        Intent intent = new Intent(Modificar_platos.this, Crud_Platos.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "No se pudo actualizar el plato", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}