package com.example.proyectorestaurante.Activity;

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

import com.example.proyectorestaurante.ConexionDB;
import com.example.proyectorestaurante.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ModificarPersonal extends AppCompatActivity {

    EditText txtnombre, txtapellido, txtdni, txtdireccion, txttelefono;
    ImageView editarImage;
    Button actualizarButton, agregarUsuario, eliminarUsuarioBtn;
    Spinner spinnerCargo;
    int id_personal;
    boolean usuarioCreado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_personal);

        // Elementos
        txtnombre = findViewById(R.id.editTextNombre);
        txtapellido = findViewById(R.id.editTextApellido);
        txtdni = findViewById(R.id.editTextDNI);
        txtdireccion = findViewById(R.id.editTextDireccion);
        txttelefono = findViewById(R.id.editTextTelefono);

        spinnerCargo = findViewById(R.id.spinnerCargo);
        spinnerCargo.setEnabled(false);

        actualizarButton = findViewById(R.id.actualizarButton);

        agregarUsuario = findViewById(R.id.agregarUsuario);
        eliminarUsuarioBtn = findViewById(R.id.eliminarUsuario);

        editarImage = findViewById(R.id.editar_personal);

        // Obtener el valor de id_personal de la intención
        Intent intent = getIntent();
        id_personal = intent.getIntExtra("id_personal", 0);

        clicEditar();
        if (id_personal != 0) {
            // Utiliza el valor de id_personal como desees en tu actividad
            Toast.makeText(getApplicationContext(), "Personal cargado correctamente", Toast.LENGTH_SHORT).show();
            Connection connection = ConexionDB.obtenerConexion();
            obtenerPersonal(connection, id_personal);

            // Spinner
            List<String> cargo;
            cargo = obtenerCargos(connection);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cargo);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCargo.setAdapter(adapter);

            clicActualizar(connection);

            // Comprueba si existe un usuario para este id_personal
            usuarioCreado = existeUsuario(connection, id_personal);

            // Deshabilita el botón de "Eliminar Usuario" si no hay un usuario creado
            eliminarUsuarioBtn.setVisibility(usuarioCreado ? View.VISIBLE : View.GONE);

            // Deshabilita el botón de "Agregar Usuario" si ya existe un usuario para este personal
            agregarUsuario.setVisibility(usuarioCreado ? View.GONE : View.VISIBLE);


        } else {
            Toast.makeText(getApplicationContext(), "id nulo", Toast.LENGTH_SHORT).show();
        }

        agregarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ModificarPersonal.this, AgregarUsuario.class);
                intent1.putExtra("id_personal", id_personal);
                startActivity(intent1);
            }
        });


        clicEliminarUsuario();
    }

    public void obtenerPersonal(Connection connection, int id_personal) {
        String query = "SELECT nombre,apellido,dni,direccion,telefono,cargo.nombre_cargo FROM personal " +
                "INNER JOIN cargo ON personal.id_cargo = cargo.id_cargo WHERE id_personal = " + id_personal;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String dni = resultSet.getString("dni");
                String direccion = resultSet.getString("direccion");
                String telefono = resultSet.getString("telefono");
                String cargo = resultSet.getString("nombre_cargo");

                txtnombre.setText(nombre);
                txtapellido.setText(apellido);
                txtdni.setText(dni);
                txtdireccion.setText(direccion);
                txttelefono.setText(telefono);
                spinnerCargo.setSelection(id_personal);
            }
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clicActualizar(Connection connection) {
        actualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtnombre.getText().toString();
                String apellido = txtapellido.getText().toString();
                String dni = txtdni.getText().toString();
                String direccion = txtdireccion.getText().toString();
                String telefono = txttelefono.getText().toString();
                String cargo = (String) spinnerCargo.getSelectedItem();

                try {
                    // Obtener id_cargo
                    String cargoquery = "SELECT id_cargo FROM cargo WHERE nombre_cargo LIKE '%" + cargo + "%'";
                    Statement st = connection.createStatement();
                    ResultSet resultSet = st.executeQuery(cargoquery);
                    String idcargo = null;
                    if (resultSet.next()) {
                        idcargo = resultSet.getString("id_cargo");
                    }

                    // Actualizar personal
                    String query = "UPDATE personal SET nombre = '" + nombre + "', apellido = '" + apellido +
                            "', dni = '" + dni + "', direccion = '" + direccion + "', telefono = '" + telefono +
                            "', id_cargo = '" + idcargo + "' WHERE id_personal = " + id_personal;

                    int rowsAffected = st.executeUpdate(query);
                    if (rowsAffected > 0) {
                        Toast.makeText(getApplicationContext(), "Personal actualizado exitosamente", Toast.LENGTH_SHORT).show();
                        // Te redirige al crud personal
                        Intent intent = new Intent(ModificarPersonal.this, Crud_Personal.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "No se pudo actualizar el personal", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clicEditar() {
        editarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si el usuario ya está creado, no muestra los botones "Agregar Usuario" y "Eliminar Usuario"


                txtnombre.setEnabled(true);
                txtnombre.setTextColor(Color.BLACK);

                txtapellido.setEnabled(true);
                txtapellido.setTextColor(Color.BLACK);

                txtdni.setEnabled(true);
                txtdni.setTextColor(Color.BLACK);

                txtdireccion.setEnabled(true);
                txtdireccion.setTextColor(Color.BLACK);

                txttelefono.setEnabled(true);
                txttelefono.setTextColor(Color.BLACK);

                spinnerCargo.setEnabled(true);

                // Muestra el botón "Actualizar" siempre que se presione "Editar Personal"
                actualizarButton.setVisibility(View.VISIBLE);
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

    public boolean existeUsuario(Connection connection, int id_personal) {
        try {
            String query = "SELECT COUNT(*) as count FROM usuario WHERE id_personal = " + id_personal;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }

            statement.close();
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void clicEliminarUsuario() {
        eliminarUsuarioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para eliminar el usuario con id_personal
                eliminarUsuario(id_personal);
            }
        });
    }

    public void eliminarUsuario(int id_personal) {
        Connection connection = ConexionDB.obtenerConexion();

        try {
            String query = "DELETE FROM usuario WHERE id_personal = " + id_personal;
            Statement statement = connection.createStatement();

            int rowsAffected = statement.executeUpdate(query);
            statement.close();

            if (rowsAffected > 0) {
                Toast.makeText(getApplicationContext(), "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show();
                // Actualiza la lista de usuarios si es necesario
                // Aquí puedes refrescar la lista de usuarios, eliminar el usuario de la lista, etc.
            } else {
                Toast.makeText(getApplicationContext(), "No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
