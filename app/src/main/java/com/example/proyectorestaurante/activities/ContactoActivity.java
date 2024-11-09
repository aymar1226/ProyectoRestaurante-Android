package com.example.proyectorestaurante.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectorestaurante.R;
import com.example.proyectorestaurante.dao.ContactoDAO;
import com.example.proyectorestaurante.dao.ContactoDAOImpl;
import com.example.proyectorestaurante.models.Contacto;
import com.example.proyectorestaurante.utils.ConexionDB;

import java.sql.Connection;
import java.util.List;

public class ContactoActivity extends AppCompatActivity {

    private GridLayout contactGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        contactGrid = findViewById(R.id.contactGrid);

        // Conectar a la base de datos y obtener contactos
        Connection connection = ConexionDB.obtenerConexion();
        if (connection != null) {
            ContactoDAO contactoDAO = new ContactoDAOImpl(connection);
            List<Contacto> contactos = contactoDAO.getAllContactos();

            // Mostrar los contactos en el GridLayout
            mostrarContactosEnGrid(contactos);
        } else {
            Toast.makeText(this, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarContactosEnGrid(List<Contacto> contactos) {
        contactGrid.removeAllViews(); // Limpiar cualquier vista existente

        for (Contacto contacto : contactos) {
            // Crear un contenedor para cada contacto
            LinearLayout contactContainer = new LinearLayout(this);
            contactContainer.setOrientation(LinearLayout.VERTICAL);
            contactContainer.setGravity(Gravity.CENTER);
            contactContainer.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            // Crear una imagen de ejemplo (puedes cambiarla según el tipo de contacto)
            ImageView contactIcon = new ImageView(this);
            contactIcon.setImageResource(R.drawable.user); // Usa un icono de usuario o el icono que prefieras
            contactIcon.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            contactContainer.addView(contactIcon);

            // Crear un TextView para mostrar el nombre del contacto
            TextView contactName = new TextView(this);
            contactName.setText(contacto.getnNombre());
            contactName.setTextColor(getResources().getColor(R.color.black));
            contactName.setTextSize(14);
            contactContainer.addView(contactName);

            // Crear un TextView para mostrar el número de teléfono del contacto
            TextView contactNumber = new TextView(this);
            contactNumber.setText(contacto.getnNumero());
            contactNumber.setTextColor(getResources().getColor(R.color.black));
            contactNumber.setTextSize(12);
            contactContainer.addView(contactNumber);

            // Agregar el contenedor del contacto al GridLayout
            contactGrid.addView(contactContainer);
        }
    }
}
