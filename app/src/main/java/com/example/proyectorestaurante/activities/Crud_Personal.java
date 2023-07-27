package com.example.proyectorestaurante.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.example.proyectorestaurante.utils.ConexionDB;
import com.example.proyectorestaurante.R;
import com.example.proyectorestaurante.adapters.PersonalAdapter;
import com.example.proyectorestaurante.models.Personal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Crud_Personal extends AppCompatActivity {
    ListView listView;
    EditText txt_buscar;
    ImageView boton_buscar,boton_agregar,boton_eliminar;

    Button crear_usuario;

    boolean buscarOn=false;
    List<Personal> userList = new ArrayList<>();
    PersonalAdapter personalAdapter;


    public void IraAgregar_personal(View view) {
        Intent intent = new Intent(Crud_Personal.this, AgregarPersonal.class);
        startActivity(intent);
    }
    public void irAInicio(View view) {
        Intent intent = new Intent(Crud_Personal.this, PrincipalActivity.class);
        startActivity(intent);
    }
    public void irOtroActivity(View view) {
        Intent intent = new Intent(Crud_Personal.this, PerfilActivity.class);
        startActivity(intent);
    }
    public void irAPlatos(View view) {
        Intent intent = new Intent(Crud_Personal.this, Crud_Platos.class);
        startActivity(intent);
    }
    public void irAPersonal(View view) {
        Intent intent = new Intent(Crud_Personal.this, Crud_Personal.class);
        startActivity(intent);
    }
    public void irAQR(View view) {
        Intent intent = new Intent(Crud_Personal.this, QRActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_personal);
        //Elementos
        txt_buscar = findViewById(R.id.buscar_personal);
        listView = findViewById(R.id.listView);
        //botones
        boton_buscar = findViewById(R.id.boton_buscar);
        boton_agregar = findViewById(R.id.agregar_usuario);
        boton_eliminar = findViewById(R.id.eliminar_usuario);

        //Buscar card


        Connection connection = ConexionDB.obtenerConexion();
        obtenerYMostrarTarjetas(connection);
        BuscarPersonal(connection);


    }
    private void obtenerYMostrarTarjetas(Connection connection) {
        String texto = txt_buscar.getText().toString();
        String query;
        // Consulta SQL para seleccionar todos los registros de la tabla de usuarios
        if(buscarOn){
            query = "SELECT id_personal, nombre, apellido, cargo.nombre_cargo, dni FROM personal INNER JOIN cargo on personal.id_cargo = cargo.id_cargo WHERE nombre LIKE '%" + texto + "%' OR apellido LIKE '%" + texto + "%'";
        }
        else {
            query = "SELECT id_personal, nombre, apellido, cargo.nombre_cargo, dni FROM personal INNER JOIN cargo on personal.id_cargo = cargo.id_cargo";
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int idUsuario = resultSet.getInt("id_personal");
                String nombreUsuario = resultSet.getString("nombre");
                String apellidoUsuario = resultSet.getString("apellido");
                String cargo = resultSet.getString("nombre_cargo");
                String dni = resultSet.getString("dni");

                Personal personal = new Personal(idUsuario, nombreUsuario, apellidoUsuario, cargo,dni);
                userList.add(personal);


            }
            statement.close();
            resultSet.close();

            personalAdapter =new PersonalAdapter(this, R.layout.content_personal, userList,boton_agregar,boton_eliminar);
            listView.setAdapter(personalAdapter);


        }catch(Exception e){
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    public void BuscarPersonal(Connection connection) {
        boton_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = txt_buscar.getText().toString();
                if(!texto.isEmpty()){
                    buscarOn=true;
                }
                else {
                    buscarOn=false;
                }
                userList.clear();
                obtenerYMostrarTarjetas(connection);
            }
        });
    }
}











