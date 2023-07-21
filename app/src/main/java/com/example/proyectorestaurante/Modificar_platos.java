package com.example.proyectorestaurante;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;


import com.example.proyectorestaurante.Activity.AgregarPlatos;
import com.example.proyectorestaurante.Activity.Crud_Personal;
import com.example.proyectorestaurante.Activity.Crud_Platos;
import com.example.proyectorestaurante.Activity.ModificarPersonal;
import com.example.proyectorestaurante.recycler.Platos;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Modificar_platos extends AppCompatActivity {

    ImageUploader imageUploader;
    private StorageReference storageReference;
    EditText txtnombre,txtdescripcion,txtprecio;
    ImageView editarImage,imagenActual;
    Button actualizarButton,selectImagenButton;
    Spinner spinnerCategoria;
    public String imagen_ruta="predet.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_platos);

        txtnombre = findViewById(R.id.editTextNombre);
        txtdescripcion = findViewById(R.id.editTextDescripcion);
        txtprecio = findViewById(R.id.editTextPrecio);

        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerCategoria.setEnabled(false);


        actualizarButton = findViewById(R.id.actualizarButton);
        selectImagenButton = findViewById(R.id.btn_select_image);

        imagenActual = findViewById(R.id.imagenActual);
        editarImage = findViewById(R.id.editar_plato);

        // Obtén la referencia a Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();

        //Instancia de la clase para subir imagenes
        imageUploader = new ImageUploader(Modificar_platos.this, storageReference);

        selectImagenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUploader.seleccionarImagen();
            }
        });

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

                selectImagenButton.setEnabled(true);
            }
        });
    }

    public void obtenerPlato(Connection connection,int id_plato){

        String query= "SELECT plato.nombre, descripcion, precio, imagen, categoria.nombre AS nombre_categoria  FROM plato INNER JOIN categoria on plato.id_categoria = categoria.id_categoria WHERE id_plato = "+id_plato;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String descripcion = resultSet.getString("descripcion");
                String precio_plato = resultSet.getString("precio");
                String categoria = resultSet.getString("nombre_categoria");
                imagen_ruta = resultSet.getString("imagen");

                txtnombre.setText(nombre);
                txtdescripcion.setText(descripcion);
                txtprecio.setText(precio_plato);
                spinnerCategoria.setSelection(id_plato);

                //Setear la Imagen
                StorageReference imagenReference = storageReference.child("imagenes/"+imagen_ruta);
                imagenReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get()
                                .load(uri.toString())
                                .into(imagenActual);
                    }
                });
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
                    String image_name=imageUploader.getImageName();
                    String query = "UPDATE plato SET nombre = '" + nombre + "', descripcion = '" + descripcion + "', precio = '" + precio + "',id_categoria = '"+idcategoria+"',imagen = '"+image_name+"' where id_plato= " + id_plato;



                    int rowsAffected = st.executeUpdate(query);
                    if (rowsAffected > 0) {
                        Toast.makeText(getApplicationContext(), "Plato actualizado exitosamente", Toast.LENGTH_SHORT).show();
                        gestionarImagen();

                    } else {
                        Toast.makeText(getApplicationContext(), "No se pudo actualizar el plato", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pasa los resultados de la actividad a la instancia de ImageUploader
        imageUploader.onActivityResult(requestCode, resultCode, data);
    }

    public void gestionarImagen(){
        //Subir imagen
        imageUploader.subirImagen(new Runnable() {
            @Override
            public void run() {
                //Te redirige al crud plato cuando acabe de subir la imagen
                Intent intent = new Intent(Modificar_platos.this, Crud_Platos.class);
                startActivity(intent);
            }
        });
        //Eliminar imagen
        imageUploader.eliminarImagen(imagen_ruta);
    }

}