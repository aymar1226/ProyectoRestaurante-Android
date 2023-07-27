package com.example.proyectorestaurante.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectorestaurante.utils.ConexionDB;
import com.example.proyectorestaurante.R;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AgregarUsuario extends AppCompatActivity {

    Spinner spinnerrol;
    TextInputLayout txtCorreoLayout, txtContraseñaLayout;
    EditText txtCorreo, txtContraseña;
    Button btnAgregarUser, btnCancelarUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);

        txtCorreoLayout = findViewById(R.id.txtCorreo);
        txtContraseñaLayout = findViewById(R.id.txtcontraseña);
        txtCorreo = txtCorreoLayout.getEditText();
        txtContraseña = txtContraseñaLayout.getEditText();
        btnAgregarUser = findViewById(R.id.btnAgregarUser);
        btnCancelarUser = findViewById(R.id.btnCancelarUser);

        Intent intent = getIntent();
        int id_personal = intent.getIntExtra("id_personal", 0);
        spinnerrol = findViewById(R.id.spinner_usuario);
        spinnerrol.setPrompt("rol");


        Connection connection= ConexionDB.obtenerConexion();

        //listar roles
        List<String> rol;
        rol = obtenerRoles(connection);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,rol);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerrol.setAdapter(adapter);

        btnAgregarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = txtCorreo.getText().toString().trim();
                String contraseña = txtContraseña.getText().toString().trim();
                String rolSeleccionado = spinnerrol.getSelectedItem().toString();

                // Obtener el id_rol correspondiente al rol seleccionado
                int id_rol = obtenerIdRol(connection, rolSeleccionado);

                // Convertir la contraseña a VARBINARY si es necesario (como se muestra en el código anterior).
                // Recuerda que en una aplicación real, deberías almacenar la contraseña de manera segura, utilizando
                // un algoritmo de hash y almacenar el resultado en el campo VARBINARY de la base de datos.
                byte[] contraseñaVarBinary = contraseña.getBytes();

                // Realizar la inserción en la tabla usuario
                if (agregarUsuario(connection, correo, contraseñaVarBinary, id_rol, id_personal)) {
                    Toast.makeText(getApplicationContext(), "Usuario agregado exitosamente", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AgregarUsuario.this, Crud_Personal.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "No se pudo agregar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si se presiona el botón "Cancelar", simplemente redirige a Crud_Personal sin realizar ninguna acción
                Intent intent = new Intent(AgregarUsuario.this, Crud_Personal.class);
                startActivity(intent);
            }
        });

    }

    public boolean agregarUsuario(Connection connection, String correo, byte[] contraseña, int id_rol, int id_personal) {
        try {
            String query = "INSERT INTO usuario (correo, contraseña, id_rol, id_personal) VALUES (?,ENCRYPTBYPASSPHRASE('L4f4ry3t3nCrypt4d0', ?), ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, correo);
            preparedStatement.setBytes(2, contraseña);
            preparedStatement.setInt(3, id_rol);
            preparedStatement.setInt(4, id_personal);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int obtenerIdRol(Connection connection, String nombreRol) {
        int id_rol = 0;

        try {
            String query = "SELECT id_rol FROM rol WHERE nombre_rol = '" + nombreRol + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                id_rol = resultSet.getInt("id_rol");
            }

            statement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id_rol;
    }
    public List<String> obtenerRoles(Connection connection) {

        List<String> listarol = new ArrayList<>();

        try {
            String query = "SELECT nombre_rol FROM rol";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String rol = resultSet.getString("nombre_rol");
                listarol.add(rol);
            }
            statement.close();
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarol;

    }
}

