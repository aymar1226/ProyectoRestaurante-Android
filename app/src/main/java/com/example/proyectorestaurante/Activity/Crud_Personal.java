package com.example.proyectorestaurante.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;


import androidx.appcompat.app.AppCompatActivity;


import com.example.proyectorestaurante.ConexionDB;
import com.example.proyectorestaurante.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Crud_Personal extends AppCompatActivity {
    LinearLayout contenedorTarjetas;
    TextView nombre,apellido,text_personal,cargo;
    EditText txt_buscar;
    ImageView boton_buscar;

    final int[] idUsuarioSeleccionado = new int[1];
    CardView tarjetaSeleccionada;
    boolean buscarOn=false;

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
        nombre = findViewById(R.id.txt_nombre);
        apellido = findViewById(R.id.txt_apellido);
        cargo = findViewById(R.id.txt_cargo);
        setContentView(R.layout.activity_crud_personal);
        CardView cardView = findViewById(R.id.card_view);
        text_personal = findViewById(R.id.text_personal);
        final ImageView agregar_personal= findViewById(R.id.agregar_usuario);
        final ImageView eliminar_personal= findViewById(R.id.eliminar_usuario);
        txt_buscar = findViewById(R.id.buscar_personal);
        contenedorTarjetas = findViewById(R.id.contenedor_tarjetas);
        boton_buscar = findViewById(R.id.boton_buscar);

        final boolean[] isLongClickActive = {false};

        //Obtener la conexion a la base de datos
        Connection connection = ConexionDB.obtenerConexion();

        // mostrar las tarjetas
        obtenerYMostrarTarjetas(connection);

        //Clic largo
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongClickActive[0] = true;
                cardView.setSelected(!cardView.isSelected()); // Cambia el estado seleccionado

                if (cardView.isSelected()) {
                    cardView.setBackgroundColor(getResources().getColor(R.color.btnVi));
                    text_personal.setVisibility(View.INVISIBLE);
                    agregar_personal.setVisibility(View.INVISIBLE);
                    eliminar_personal.setVisibility(View.VISIBLE);

                } else {
                    if (text_personal != null) {
                        text_personal.setVisibility(View.VISIBLE);
                        agregar_personal.setVisibility(View.VISIBLE);
                        eliminar_personal.setVisibility(View.INVISIBLE);
                    }
                    cardView.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                }

                // Devuelve 'true' para indicar que se ha consumido el evento
                return false;
            }
        });

        //Clic corto
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLongClickActive[0]) {
                    if (cardView.isSelected()) {
                        cardView.setSelected(false); // Deselecciona la tarjeta
                        cardView.setBackgroundColor(getResources().getColor(R.color.colorNormal));
                        text_personal.setVisibility(View.VISIBLE);
                        agregar_personal.setVisibility(View.VISIBLE);
                        eliminar_personal.setVisibility(View.INVISIBLE);
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

        //Eliminar Card
        eliminar_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = ConexionDB.obtenerConexion();

                int id =idUsuarioSeleccionado[0];
                try {
                    if (connection != null) {
                        String sqldelete="DELETE personal WHERE id_personal = '"+id+"'";
                        Statement st = connection.createStatement();
                        int rowsAffected = st.executeUpdate(sqldelete);
                        contenedorTarjetas.removeView(tarjetaSeleccionada);
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
                Connection connection=ConexionDB.obtenerConexion();
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
        String query;
        // Consulta SQL para seleccionar todos los registros de la tabla de usuarios
        if(buscarOn){
            query = "SELECT id_personal, nombre, apellido, cargo FROM personal WHERE nombre LIKE '%" + texto + "%' OR apellido LIKE '%" + texto + "%'";
        }
        else {
            query = "SELECT id_personal, nombre, apellido, cargo FROM personal";
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int idUsuario = resultSet.getInt("id_personal");
                String nombreUsuario = resultSet.getString("nombre");
                String apellidoUsuario = resultSet.getString("apellido");
                String cargo = resultSet.getString("cargo");

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

                // Crear la imagen de perfil
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        getResources().getDimensionPixelSize(R.dimen.image_width),
                        getResources().getDimensionPixelSize(R.dimen.image_height)
                ));
                imageView.setImageResource(R.drawable.fotovacio);
                imageView.setContentDescription("Profile Image");

                // Crear el contenedor del texto
                LinearLayout textoContainer = new LinearLayout(this);
                textoContainer.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textoContainer.setOrientation(LinearLayout.VERTICAL);
                textoContainer.setPadding(16, 0, 0, 0);

                // Crear el nombre y el apellido
                TextView textViewNombreApellido = new TextView(this);
                textViewNombreApellido.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textViewNombreApellido.setId(View.generateViewId());
                textViewNombreApellido.setText(nombreUsuario + " " + apellidoUsuario);
                textViewNombreApellido.setTextSize(18);
                textViewNombreApellido.setTextColor(Color.parseColor("#212121"));
                textViewNombreApellido.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));

                // Crear el cargo
                TextView textViewCargo = new TextView(this);
                textViewCargo.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textViewCargo.setId(View.generateViewId());
                textViewCargo.setText(cargo);
                textViewCargo.setTextSize(14);
                textViewCargo.setTextColor(Color.parseColor("#757575"));
                textViewCargo.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));

                // Agregar los elementos al contenedor de contenido de la tarjeta
                contenidoTarjeta.addView(imageView);
                textoContainer.addView(textViewNombreApellido);
                textoContainer.addView(textViewCargo);
                contenidoTarjeta.addView(textoContainer);

                // Agregar el contenedor de contenido de la tarjeta a la tarjeta
                cardView.addView(contenidoTarjeta);

                // Agregar la tarjeta al contenedor principal
                contenedorTarjetas.addView(cardView);



                final TextView text_personal = findViewById(R.id.text_personal);
                final ImageView agregar_personal= findViewById(R.id.agregar_usuario);
                final ImageView eliminar_personal= findViewById(R.id.eliminar_usuario);
                final boolean[] isLongClickActive = {false};



                cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        idUsuarioSeleccionado[0] = idUsuario;
                        Toast.makeText(getApplicationContext(), "ID de usuario: " + idUsuarioSeleccionado[0], Toast.LENGTH_SHORT).show();

                        isLongClickActive[0] = true;
                        cardView.setSelected(!cardView.isSelected()); // Cambia el estado seleccionado
                        tarjetaSeleccionada = cardView;

                        if (cardView.isSelected()) {
                            cardView.setBackgroundColor(getResources().getColor(R.color.btnVi));
                            text_personal.setVisibility(View.INVISIBLE);
                            agregar_personal.setVisibility(View.INVISIBLE);
                            eliminar_personal.setVisibility(View.VISIBLE);

                        } else {
                            if (text_personal != null) {
                                text_personal.setVisibility(View.VISIBLE);
                                agregar_personal.setVisibility(View.VISIBLE);
                                eliminar_personal.setVisibility(View.INVISIBLE);
                            }
                            cardView.setBackgroundColor(getResources().getColor(R.color.colorNormal));
                        }


                        // Devuelve 'true' para indicar que se ha consumido el evento
                        return  true;
                    }
                });


                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isLongClickActive[0]) {
                            if (cardView.isSelected()) {
                                cardView.setSelected(false); // Deselecciona la tarjeta
                                cardView.setBackgroundColor(getResources().getColor(R.color.colorNormal));
                                text_personal.setVisibility(View.VISIBLE);
                                agregar_personal.setVisibility(View.VISIBLE);
                                eliminar_personal.setVisibility(View.INVISIBLE);
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
    }
    


}











