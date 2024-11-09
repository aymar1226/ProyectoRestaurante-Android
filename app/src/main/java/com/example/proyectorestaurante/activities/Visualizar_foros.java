package com.example.proyectorestaurante.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.proyectorestaurante.R;
import com.example.proyectorestaurante.adapters.ForoAdapter;
import com.example.proyectorestaurante.dao.ForoDao;
import com.example.proyectorestaurante.models.Foro;

import java.util.ArrayList;
import java.util.List;

public class Visualizar_foros extends AppCompatActivity {

    private RecyclerView recyclerViewForos;
    private ForoAdapter foroAdapter;
    private List<Foro> listaForos;
    private ForoDao foroDao = new ForoDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_foros);

        // Inicializar RecyclerView
        recyclerViewForos = findViewById(R.id.recyclerViewForos);
        recyclerViewForos.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la lista de foros
        listaForos = obtenerForos(); // Método para obtener la lista de foros

        // Inicializar el adaptador
        foroAdapter = new ForoAdapter(listaForos, foro -> {
            // Crear un intent para abrir la actividad de detalles del foro
            Intent intent = new Intent(getApplicationContext(), Comentarios_Foro.class);
            startActivity(intent);
        });

        recyclerViewForos.setAdapter(foroAdapter);
    }

    // Método para cargar datos de ejemplo (en un caso real se cargarían desde una base de datos o API)
    private List<Foro> obtenerForos() {
        List<Foro> foros = new ArrayList<>();
        foros=foroDao.obtenerForos();
        // Agrega más datos según sea necesario
        return foros;
    }
}