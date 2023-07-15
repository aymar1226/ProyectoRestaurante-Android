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
import com.example.proyectorestaurante.recycler.Players;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ModificarPersonal extends AppCompatActivity {

    EditText txtnombre,txtapellido,txtdni,txtdireccion,txttelefono;
    ImageView editarImage;
    Button actualizarButton;
    Spinner spinnerCargo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_personal);

        //Elementos
        txtnombre=findViewById(R.id.editTextNombre);
        txtapellido=findViewById(R.id.editTextApellido);
        txtdni=findViewById(R.id.editTextDNI);
        txtdireccion=findViewById(R.id.editTextDireccion);
        txttelefono=findViewById(R.id.editTextTelefono);

        spinnerCargo=findViewById(R.id.spinnerCargo);
        spinnerCargo.setEnabled(false);

        actualizarButton=findViewById(R.id.actualizarButton);

        editarImage = findViewById(R.id.editar_personal);

        clicEditar();
        Connection connection=ConexionDB.obtenerConexion();

        Intent intent = getIntent();
        int id_personal = intent.getIntExtra("id_personal", 0);
        if (id_personal!=0) {
            // Utiliza el valor de id_personal como desees en tu actividad
            Toast.makeText(getApplicationContext(), "Personal cargado correctamente", Toast.LENGTH_SHORT).show();
            obtenerPersonal(connection,id_personal);

            //Spinner
            List<String> cargo;
            cargo = obtenerCargos(connection);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cargo);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCargo.setAdapter(adapter);

            clicActualizar(connection,id_personal);

        }else{
            Toast.makeText(getApplicationContext(), "id nulo", Toast.LENGTH_SHORT).show();
        }

    }
    public void obtenerPersonal(Connection connection,int id_personal){

        String query= "SELECT nombre,apellido,dni,direccion,telefono,cargo.nombre_cargo FROM personal INNER JOIN cargo on personal.id_cargo = cargo.id_cargo WHERE id_personal = "+id_personal;
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
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clicActualizar(Connection connection,int id_personal){
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
                    //Obtener id_cargo
                    String cargoquery="SELECT id_cargo FROM cargo where nombre_cargo LIKE '%"+cargo+"%'";
                    Statement st = connection.createStatement();
                    ResultSet resultSet = st.executeQuery(cargoquery);
                    String idcargo=null;
                    if (resultSet.next()) {
                        idcargo = resultSet.getString("id_cargo");
                    }

                    //Actualizar personal
                    String query = "UPDATE personal SET nombre = '" + nombre + "', apellido = '" + apellido + "', dni = '" + dni + "', direccion = '" + direccion + "', telefono = '" + telefono + "',id_cargo = '"+idcargo+"' where id_personal= " + id_personal;

                    int rowsAffected = st.executeUpdate(query);
                    if (rowsAffected > 0) {
                        Toast.makeText(getApplicationContext(), "Personal actualizado exitosamente", Toast.LENGTH_SHORT).show();
                        //Te redirige al crud personal
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


    public void clicEditar(){
        editarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarButton.setVisibility(View.VISIBLE);

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