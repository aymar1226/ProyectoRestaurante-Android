package com.example.proyectorestaurante.recycler;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.proyectorestaurante.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends ArrayAdapter<Players> {
    private Context mContext;
    private int mResource;
    private List<Players> mPersonList;
    boolean isSelected;
    private ArrayList<Integer> selectedItemList = new ArrayList<>();
    private LinearLayout linearLayout;
    private ImageView eliminarImage, agregarImage;


    public Adapter(Context context, int resource, List<Players> personList,ImageView image) {
        super(context, resource, personList);
        mContext = context;
        mResource = resource;
        mPersonList = personList;
        agregarImage=image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);
        }
        //boton

        // Configura el contenido de la vista para mostrar los datos de la persona actual
        TextView nameTextView = view.findViewById(R.id.nombre_personal);
        TextView apellidoTextView = view.findViewById(R.id.nombre_personal);
        TextView cargoTextView = view.findViewById(R.id.cargo_personal);

        Players player = mPersonList.get(position);
        nameTextView.setText(player.getNombre()+" "+player.getApellido());
        cargoTextView.setText(player.getCargo());

        //eliminarImage=view.findViewById(R.id.eliminar_usuario);

        isSelected = selectedItemList.contains(position);
        // Establecer el color de fondo original del ítem
        if(isSelected){
            int background = ContextCompat.getColor(getContext(), R.color.btnVi);
            view.setBackgroundColor(background);
        }else {
            int background = ContextCompat.getColor(getContext(), R.color.colorNormal);
            view.setBackgroundColor(background);
        }



        // Establecer el setOnLongClickListener
        View finalView1 = view;
        view.setOnLongClickListener(new View.OnLongClickListener() {
            boolean isSelected = selectedItemList.contains(position);
            @Override
            public boolean onLongClick(View v) {
                // Verificar si el ítem ya está seleccionado
                if (isSelected) {
                    // Deseleccionar el ítem y restaurar el color de fondo original
                    selectedItemList.remove(Integer.valueOf(position));
                    agregarImage.setVisibility(View.VISIBLE);

                } else {
                    // Seleccionar el ítem y cambiar el color de fondo
                    selectedItemList.add(position);
                    agregarImage.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
                return false;
            }
        });



        //Click corto
        View finalView = view;
        view.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = selectedItemList.contains(position);
            @Override
            public void onClick(View v) {
                // Acciones específicas al hacer clic en el elemento
                finalView.setPressed(false);
                selectedItemList.remove(Integer.valueOf(position));
                agregarImage.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
                Toast.makeText(mContext, "Has hecho clic en: " +position, Toast.LENGTH_SHORT).show();
            }
        });




        return view;
    }

    public interface ImageViewProvider {
        ImageView getImageView();
    }




}

