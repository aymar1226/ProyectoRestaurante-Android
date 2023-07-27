package com.example.proyectorestaurante.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.proyectorestaurante.activities.ModificarPersonal;
import com.example.proyectorestaurante.utils.ConexionDB;
import com.example.proyectorestaurante.R;
import com.example.proyectorestaurante.utils.SessionManager;
import com.example.proyectorestaurante.models.Personal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonalAdapter extends ArrayAdapter<Personal> {
    private Context mContext;
    private int mResource;
    private List<Personal> mPersonList;
    boolean isSelected;
    int itemselected;
    private ArrayList<Integer> selectedItemList = new ArrayList<>();
    private ImageView eliminarImage, agregarImage;

    private SessionManager sessionManager;

    public PersonalAdapter(Context context, int resource, List<Personal> personList, ImageView agregar, ImageView eliminar) {
        super(context, resource, personList);
        mContext = context;
        mResource = resource;
        mPersonList = personList;
        agregarImage=agregar;
        eliminarImage=eliminar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);
        }

        // Configura el contenido de la vista para mostrar los datos de la persona actual
        TextView nameTextView = view.findViewById(R.id.nombre_personal);
        TextView cargoTextView = view.findViewById(R.id.cargo_personal);
        Personal player = mPersonList.get(position);
        nameTextView.setText(player.getNombre()+" "+player.getApellido());
        cargoTextView.setText(player.getCargo());





        isSelected = selectedItemList.contains(position);
        // Establecer el color de fondo original del ítem
        if(isSelected){
            int background = ContextCompat.getColor(getContext(), R.color.btnVi);
            view.setBackgroundColor(background);
        }else {
            int background = ContextCompat.getColor(getContext(), R.color.colorNormal);
            view.setBackgroundColor(background);
            notifyDataSetChanged();
        }


        //Crear instancia de session
        sessionManager = new SessionManager(getContext());
        String rol = sessionManager.obtenerRol();

        // Clic largo
        if(rol.equals("Administrador")) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                boolean isSelected = selectedItemList.contains(position);

                @Override
                public boolean onLongClick(View v) {
                    itemselected = player.getId();

                    // Verificar si el ítem ya está seleccionado
                    if (isSelected) {
                        // Deseleccionar el ítem y restaurar el color de fondo original
                        selectedItemList.remove(Integer.valueOf(position));
                        agregarImage.setVisibility(View.VISIBLE);
                        eliminarImage.setVisibility(View.GONE);
                    } else {
                        // Seleccionar el ítem y cambiar el color de fondo
                        selectedItemList.add(position);
                        agregarImage.setVisibility(View.GONE);
                        eliminarImage.setVisibility(View.VISIBLE);
                    }
                    notifyDataSetChanged();
                    return false;
                }
            });
        }else {
            agregarImage.setVisibility(View.GONE);
        }

        //Clic corto
        View finalView = view;
        view.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = selectedItemList.contains(position);
            @Override
            public void onClick(View v) {
                int clicPersonal = player.getId();
                if(isSelected){
                    finalView.setPressed(false);
                    selectedItemList.remove(Integer.valueOf(position));
                    agregarImage.setVisibility(View.VISIBLE);
                    eliminarImage.setVisibility(View.GONE);
                    notifyDataSetChanged();
                }else {
                    finalView.setPressed(true);

                    if(rol.equals("Administrador")) {
                        Intent intent = new Intent(mContext, ModificarPersonal.class);
                        intent.putExtra("id_personal", clicPersonal);
                        mContext.startActivity(intent);
                    }
                }

                //Toast.makeText(mContext, "Clic en: " + itemselected, Toast.LENGTH_SHORT).show();
            }
        });

        //Eliminar personal
        if(rol.equals("Administrador")) {
            eliminarImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPersonList.remove(position);
                    notifyDataSetChanged();
                    eliminarPersonalDB(itemselected);
                    Toast.makeText(mContext, "Personal eliminado correctamente" + itemselected, Toast.LENGTH_SHORT).show();
                    agregarImage.setVisibility(View.VISIBLE);
                    eliminarImage.setVisibility(View.GONE);
                }
            });
        }


        return view;
    }
    public void eliminarPersonalDB(int itemselected) {

        // Consulta para eliminar el dato
        String query = "DELETE FROM personal WHERE id_personal = " + itemselected;

        Connection connection = ConexionDB.obtenerConexion();

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            // Ejecutar la consulta
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("El dato ha sido eliminado correctamente.");
            } else {
                System.out.println("El dato no existe en la tabla.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el dato: " + e.getMessage());
        }
    }



}

