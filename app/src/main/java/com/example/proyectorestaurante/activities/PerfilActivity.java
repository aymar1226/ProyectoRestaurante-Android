package com.example.proyectorestaurante.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.proyectorestaurante.auth.Usuario;
import com.example.proyectorestaurante.R;
import com.example.proyectorestaurante.utils.ConexionDB;
import com.example.proyectorestaurante.utils.SessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PerfilActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    TextView nombre, apellido, correo, cerrar;

    int idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        nombre = findViewById(R.id.textViewNombre);
        apellido = findViewById(R.id.textViewApellido);
        correo = findViewById(R.id.textViewCorreo);
        cerrar = findViewById(R.id.textViewPresionar);

        cerrar.setOnClickListener(view -> {
            sessionManager.cerrarSesion();
            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        obtenerNombreUsuario();

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

    public void obtenerNombreUsuario() {
        sessionManager = new SessionManager(getApplicationContext());
        String idUsuario = sessionManager.obtenerId();


        try {
            // Establecer la conexión a la base de datos SQL Server
            Connection connection = ConexionDB.obtenerConexion();

            // Ejecutar la consulta SQL para obtener el nombre del usuario según su ID
            String sql = "SELECT personal.nombre, personal.apellido, usuario.correo FROM usuario INNER JOIN personal ON usuario.id_personal = personal.id_personal WHERE usuario.id_usuario = "+idUsuario;

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            // Obtener el nombre del usuario si se encuentra
            if (resultSet.next()) {
                String nombretxt = resultSet.getString("nombre");
                String apellidotxt = resultSet.getString("apellido");
                String correotxt = resultSet.getString("correo");

                nombre.setText(nombretxt);
                apellido.setText(apellidotxt);
                correo.setText(correotxt);
            }

            // Cerrar los recursos
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
