package com.example.proyectorestaurante.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.example.proyectorestaurante.R;
import com.example.proyectorestaurante.dao.RelacionDAO;
import com.example.proyectorestaurante.dao.RelacionDAOImpl;
import com.example.proyectorestaurante.models.Relacion;
import com.example.proyectorestaurante.utils.ConexionDB;

import java.util.ArrayList;
import java.util.List;

public class AgregarContactoActivity extends AppCompatActivity {

    private Spinner relationshipSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        relationshipSpinner = findViewById(R.id.relationshipSpinner);
        loadRelationshipSpinner();
    }

    private void loadRelationshipSpinner() {
        RelacionDAOImpl relacionDAO = new RelacionDAOImpl();
        List<Relacion> relaciones = relacionDAO.getAllRelaciones();

        // Creamos una lista de Strings con las descripciones para el Spinner
        List<String> relationshipDescriptions = new ArrayList<>();
        for (Relacion relacion : relaciones) {
            relationshipDescriptions.add(relacion.getnDescripcion());
        }

        // Configuramos el adaptador del Spinner con las descripciones
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, relationshipDescriptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relationshipSpinner.setAdapter(adapter);
    }
}