package com.example.proyectorestaurante.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectorestaurante.ConexionDB;
import com.example.proyectorestaurante.R;
import com.example.proyectorestaurante.recycler.PersonalAdapter;
import com.example.proyectorestaurante.recycler.Platos;
import com.example.proyectorestaurante.recycler.PlatosAdapter;
import com.example.proyectorestaurante.recycler.Players;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Crud_Platos extends AppCompatActivity {

    PlatosAdapter platosAdapter;

    TextView nombre,categoria,precio,descripcion,text_platos;
    ImageView boton_buscar,imagen_plato,boton_agregar,boton_eliminar;

    EditText txt_buscar;

    final int[] idPlatoSeleccionado = new int[1];
    boolean buscarOn=false;

    List<Platos> platosList = new ArrayList<>();
    ListView listView;



    public void IraAgregar_platos(View view) {
        Intent intent = new Intent(Crud_Platos.this, AgregarPlatos.class);
        startActivity(intent);
    }
    public void irAInicio(View view) {
        Intent intent = new Intent(Crud_Platos.this, PrincipalActivity.class);
        startActivity(intent);
    }
    public void irOtroActivity(View view) {
        Intent intent = new Intent(Crud_Platos.this, PerfilActivity.class);
        startActivity(intent);
    }
    public void irAPlatos(View view) {
        Intent intent = new Intent(Crud_Platos.this, Crud_Platos.class);
        startActivity(intent);
    }
    public void irAPersonal(View view) {
        Intent intent = new Intent(Crud_Platos.this, Crud_Personal.class);
        startActivity(intent);
    }
    public void irAQR(View view) {
        Intent intent = new Intent(Crud_Platos.this, QRActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_platos);
        txt_buscar= findViewById(R.id.txt_buscar);
        listView=findViewById(R.id.listView);

        boton_buscar=findViewById(R.id.boton_buscar);
        boton_agregar=findViewById(R.id.agregar_plato);
        boton_eliminar=findViewById(R.id.eliminar_plato);


        Connection connection = ConexionDB.obtenerConexion();
        obtenerYMostrarTarjetas(connection);
        Buscar(connection);
    }

    private void obtenerYMostrarTarjetas(Connection connection) {
        String texto = txt_buscar.getText().toString();
        String query;
        // Consulta SQL para seleccionar todos los registros de la tabla de usuarios
        if(buscarOn){
            query = "SELECT id_plato, plato.nombre, precio, categoria.nombre AS nombre_categoria,imagen  FROM plato INNER JOIN categoria on plato.id_categoria = categoria.id_categoria WHERE plato.nombre LIKE '%" + texto + "%'";
        }
        else {
            query = "SELECT id_plato, plato.nombre, precio, categoria.nombre AS nombre_categoria,imagen  FROM plato INNER JOIN categoria on plato.id_categoria = categoria.id_categoria";
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int idPlato = resultSet.getInt("id_plato");
                String nombrePlato = resultSet.getString("nombre");
                double precio = resultSet.getDouble("precio");
                String categoria = resultSet.getString("nombre_categoria");
                String imagen = resultSet.getString("imagen");

                Platos platos = new Platos(idPlato, nombrePlato, precio, imagen, categoria);
                platosList.add(platos);


            }
            statement.close();
            resultSet.close();

            platosAdapter =new PlatosAdapter(this, R.layout.content_platos, platosList,boton_agregar,boton_eliminar);
            listView.setAdapter(platosAdapter);


        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public void Buscar(Connection connection) {
        boton_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = txt_buscar.getText().toString();
                if (!texto.isEmpty()) {
                    buscarOn = true;
                } else {
                    buscarOn = false;
                }
                platosList.clear();
                obtenerYMostrarTarjetas(connection);
            }
        });
    }



    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_platos);
        CardView cardView = findViewById(R.id.card_view);
        contenedorTarjetas = findViewById(R.id.contenedor_tarjetas);
        text_platos = findViewById(R.id.text_platos);
        agregar_plato = findViewById(R.id.agregar_plato);
        eliminar_plato = findViewById(R.id.eliminar_plato);
        boton_buscar = findViewById(R.id.boton_buscar);
        txt_buscar = findViewById(R.id.txt_buscar);
        imagen_plato = findViewById(R.id.imagen_plato);


        nombre = findViewById(R.id.txt_nombre);
        categoria = findViewById(R.id.txt_categoria);
        precio = findViewById(R.id.txt_descripcion);
        descripcion = findViewById(R.id.txt_precio);

        Connection connection = ConexionDB.obtenerConexion();
        // Obtener los datos de la base de datos y mostrar las tarjetas
        obtenerYMostrarTarjetas(connection);

        eliminar_plato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = ConexionDB.obtenerConexion();
                int id =idPlatoSeleccionado[0];
                try {
                    if (connection != null) {
                        String sqldelete="DELETE platos WHERE id_plato = '"+id+"'";
                        Statement st = connection.createStatement();
                        int rowsAffected = st.executeUpdate(sqldelete);
                        contenedorTarjetas.removeView(tarjetaSeleccionada);
                        text_platos.setVisibility(View.VISIBLE);
                        agregar_plato.setVisibility(View.VISIBLE);
                        eliminar_plato.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Fila afectada" + rowsAffected, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Buscar card
        boton_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = txt_buscar.getText().toString();
                Connection connection= ConexionDB.obtenerConexion();
                if(!texto.isEmpty()){
                    contenedorTarjetas.removeAllViews();
                    buscarOn=true;
                }
                else {
                    contenedorTarjetas.removeAllViews();
                    buscarOn=false;
                }
                obtenerYMostrarTarjetas(connection);
            }
        });
    }


    private void obtenerYMostrarTarjetas(Connection connection) {
        CardView ultimoCard = null;
        String texto = txt_buscar.getText().toString();
        // Consulta SQL para seleccionar todos los registros de la tabla de usuarios
        String query;
        if(buscarOn){
            query = "SELECT id_plato, nombre ,descripcion,precio,imagen FROM plato WHERE nombre_plato LIKE '%" + texto + "%'";
        }
        else {
            query = "SELECT id_plato, nombre_plato, descripcion, precio, imagen_plato FROM plato";        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int idPlatos = resultSet.getInt("id_plato");
                String nombrePlato = resultSet.getString("nombre_plato");
                String descripcion = resultSet.getString("descripcion");
                String precio = resultSet.getString("precio");
                String imagen = resultSet.getString("imagen_plato");

                // Crear el espacio entre las tarjetas
                Space espacioTarjeta = new Space(this);
                espacioTarjeta.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        getResources().getDimensionPixelSize(R.dimen.card_spacing)
                ));

                // Agregar el espacio entre las tarjetas al contenedor principal
                contenedorTarjetas.addView(espacioTarjeta);


                // Crear la tarjeta
                CardView cardView = new CardView(this);
                cardView.setLayoutParams(new CardView.LayoutParams(
                        CardView.LayoutParams.MATCH_PARENT,
                        CardView.LayoutParams.WRAP_CONTENT
                ));
                cardView.setRadius(getResources().getDimensionPixelSize(R.dimen.card_radius));
                cardView.setCardElevation(getResources().getDimensionPixelSize(R.dimen.card_elevation));
                cardView.setCardBackgroundColor(Color.WHITE);
                ultimoCard = cardView;


                // Crear el contenedor de contenido de la tarjeta
                LinearLayout contenidoTarjeta = new LinearLayout(this);
                contenidoTarjeta.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                contenidoTarjeta.setOrientation(LinearLayout.HORIZONTAL);
                contenidoTarjeta.setPadding(
                        getResources().getDimensionPixelSize(R.dimen.card_padding),
                        getResources().getDimensionPixelSize(R.dimen.card_padding),
                        getResources().getDimensionPixelSize(R.dimen.card_padding),
                        getResources().getDimensionPixelSize(R.dimen.card_padding)
                );

                // Eliminar la extensión de la imagen (si está presente)
                int extensionIndex = imagen.lastIndexOf(".");
                if (extensionIndex != -1) {
                    imagen = imagen.substring(0, extensionIndex);
                }

                // Crear la imagen de perfil
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        getResources().getDimensionPixelSize(R.dimen.image_width),
                        getResources().getDimensionPixelSize(R.dimen.image_height)
                ));

                // Obtener el resource ID de la imagen utilizando el nombre almacenado en "imagen_plato"
                int resourceId = getResources().getIdentifier(imagen, "drawable", getPackageName());

                // Verificar si se encontró el resource ID
                if (resourceId != 0) {
                    // Establecer la imagen del ImageView utilizando el resource ID
                    imageView.setImageResource(resourceId);
                    imageView.setContentDescription("Profile Image");
                } else {
                    // Si no se encontró el resource ID, se puede establecer una imagen de fallback o mostrar un mensaje de error
                    // Por ejemplo:
                    imageView.setImageResource(R.drawable.predet);
                    imageView.setContentDescription("Default Image");
                }


                // Crear el contenedor del texto
                LinearLayout textoContainer = new LinearLayout(this);
                textoContainer.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textoContainer.setOrientation(LinearLayout.VERTICAL);
                textoContainer.setPadding(16, 0, 0, 0);

                // Crear el nombre del plato
                TextView textViewNombrePlato = new TextView(this);
                textViewNombrePlato.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textViewNombrePlato.setId(View.generateViewId());
                textViewNombrePlato.setText(nombrePlato);
                textViewNombrePlato.setTextSize(18);
                textViewNombrePlato.setTextColor(Color.parseColor("#212121"));
                textViewNombrePlato.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));


                // Crear la descripcion
                TextView textViewDesripcion = new TextView(this);
                textViewDesripcion.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textViewDesripcion.setId(View.generateViewId());
                textViewDesripcion.setText(descripcion);
                textViewDesripcion.setTextSize(14);
                textViewDesripcion.setTextColor(Color.parseColor("#757575"));
                textViewDesripcion.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));

                // Crear el precio
                TextView textViewPrecio = new TextView(this);
                textViewPrecio.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textViewPrecio.setId(View.generateViewId());
                textViewPrecio.setText(precio);
                textViewPrecio.setTextSize(14);
                textViewPrecio.setTextColor(Color.parseColor("#757575"));
                textViewPrecio.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));

                // Agregar los elementos al contenedor de contenido de la tarjeta
                contenidoTarjeta.addView(imageView);
                textoContainer.addView(textViewNombrePlato);
                textoContainer.addView(textViewDesripcion);
                textoContainer.addView(textViewPrecio);
                contenidoTarjeta.addView(textoContainer);

                // Agregar el contenedor de contenido de la tarjeta a la tarjeta
                cardView.addView(contenidoTarjeta);

                // Agregar la tarjeta al contenedor principal
                contenedorTarjetas.addView(cardView);


                final TextView text_platos = findViewById(R.id.text_platos);
                final ImageView agregar_plato = findViewById(R.id.agregar_plato);
                final ImageView eliminar_plato = findViewById(R.id.eliminar_plato);
                final boolean[] isLongClickActive = {false};


                cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        tarjetaSeleccionada = cardView;
                        idPlatoSeleccionado[0]=idPlatos;
                        Toast.makeText(getApplicationContext(), "ID del Plato: " + idPlatoSeleccionado[0], Toast.LENGTH_SHORT).show();


                        isLongClickActive[0] = true;
                        cardView.setSelected(!cardView.isSelected()); // Cambia el estado seleccionado

                        if (cardView.isSelected()) {
                            cardView.setBackgroundColor(getResources().getColor(R.color.btnVi));
                            text_platos.setVisibility(View.INVISIBLE);
                            agregar_plato.setVisibility(View.INVISIBLE);
                            eliminar_plato.setVisibility(View.VISIBLE);

                        } else {
                            if (text_platos != null) {
                                text_platos.setVisibility(View.VISIBLE);
                                agregar_plato.setVisibility(View.VISIBLE);
                                eliminar_plato.setVisibility(View.INVISIBLE);
                            }
                            cardView.setBackgroundColor(getResources().getColor(R.color.colorNormal));
                        }

                        // Devuelve 'true' para indicar que se ha consumido el evento
                        return false;
                    }
                });


                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isLongClickActive[0]) {
                            if (cardView.isSelected()) {
                                cardView.setSelected(false); // Deselecciona la tarjeta
                                cardView.setBackgroundColor(getResources().getColor(R.color.colorNormal));
                                text_platos.setVisibility(View.VISIBLE);
                                agregar_plato.setVisibility(View.VISIBLE);
                                eliminar_plato.setVisibility(View.INVISIBLE);
                            } else {
                                // Cambia el color de la tarjeta por 100 milisegundos
                                cardView.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        cardView.setBackgroundColor(getResources().getColor(R.color.colorNormal));
                                    }
                                }, 100);
                            }
                        }
                        isLongClickActive[0] = false;
                    }
                });

            }

            if (ultimoCard != null) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ultimoCard.getLayoutParams();
                layoutParams.setMargins(
                        layoutParams.leftMargin,
                        layoutParams.topMargin,
                        layoutParams.rightMargin,
                        getResources().getDimensionPixelSize(R.dimen.card_margin_bottom)
                );
                ultimoCard.setLayoutParams(layoutParams);
            }

            statement.close();
            resultSet.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/
}