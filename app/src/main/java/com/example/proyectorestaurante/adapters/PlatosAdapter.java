package com.example.proyectorestaurante.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.proyectorestaurante.utils.ConexionDB;
import com.example.proyectorestaurante.utils.ImageUploader;
import com.example.proyectorestaurante.activities.ModificarPlatos;
import com.example.proyectorestaurante.R;
import com.example.proyectorestaurante.utils.SessionManager;
import com.example.proyectorestaurante.models.Platos;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlatosAdapter extends ArrayAdapter<Platos> {

    private Context mContext;
    private int mResource;
    private List<Platos> mPlatoList;
    private Activity mActivity;
    ImageUploader imageUploader;
    private ImageView eliminarImage, agregarImage;

    int itemselected;
    boolean isSelected;
    private ArrayList<Integer> selectedItemList = new ArrayList<>();
    private SessionManager sessionManager;



    public PlatosAdapter(Context context, int resource, List<Platos> personList, ImageView agregar, ImageView eliminar, Activity activity) {
        super(context, resource, personList);
        mContext = context;
        mResource = resource;
        mPlatoList = personList;
        agregarImage=agregar;
        eliminarImage=eliminar;
        mActivity=activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(mResource, parent, false);
        }


        // Configura el contenido de la vista para mostrar los datos de los platos
        TextView nameTextView = view.findViewById(R.id.nombre_plato);
        TextView precioTextView = view.findViewById(R.id.cargo_plato);
        TextView categoriaTextView = view.findViewById(R.id.categoria_plato);
        ImageView platoImage = view.findViewById(R.id.imagen_plato);

        Platos plato = mPlatoList.get(position);

        //Imagen
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imagenes/"+plato.getImagen_ruta());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri.toString())
                        .into(platoImage);
            }
        });

        nameTextView.setText(plato.getNombre());
        precioTextView.setText("S/"+ String.valueOf(plato.getPrecio()));
        categoriaTextView.setText(plato.getCategoria());

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

        if(rol.equals("Administrador")) {
            // Clic largo
            view.setOnLongClickListener(new View.OnLongClickListener() {
                boolean isSelected = selectedItemList.contains(position);

                @Override
                public boolean onLongClick(View v) {
                    itemselected = plato.getId();

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
                int clicPlato = plato.getId();
                if(isSelected){
                    finalView.setPressed(false);
                    selectedItemList.remove(Integer.valueOf(position));
                    agregarImage.setVisibility(View.VISIBLE);
                    eliminarImage.setVisibility(View.GONE);
                    notifyDataSetChanged();
                } else {
                    finalView.setPressed(true);
                    if(rol.equals("Administrador")) {
                        Intent intent = new Intent(mContext, ModificarPlatos.class);
                        intent.putExtra("id_plato", clicPlato);
                        mContext.startActivity(intent);
                    }
                }

                //Toast.makeText(mContext, "Clic en: " + itemselected, Toast.LENGTH_SHORT).show();

            }
        });

        if(rol.equals("Administrador")) {
            //Eliminar personal
            eliminarImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mPlatoList.remove(position);
                    notifyDataSetChanged();
                    eliminarPlatoDB(itemselected);
                    agregarImage.setVisibility(View.VISIBLE);
                    eliminarImage.setVisibility(View.GONE);

                }
            });
        }

        return view;
    }

    public void eliminarPlatoDB(int itemselected) {

        // Obtén la referencia a Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        //Instancia de la clase para subir imagenes
        imageUploader = new ImageUploader(mActivity, storageReference);

        // Consulta para eliminar el dato
        String query = "DELETE FROM plato WHERE id_plato= " + itemselected;

        //Consulta para obtener el nombre de la imagen a eliminar
        String imagequery = "SELECT imagen FROM plato WHERE id_plato= "+itemselected;

        Connection connection = ConexionDB.obtenerConexion();

        try {
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(imagequery);
            String nombre_imagen=null;
            if (resultSet.next()) {
                nombre_imagen = resultSet.getString("imagen");
            }

            // Ejecutar la consulta
            int filasAfectadas = st.executeUpdate(query);

            if (filasAfectadas > 0) {
                imageUploader.eliminarImagen(nombre_imagen);
                Toast.makeText(mContext, "Plato eliminado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "El plato no sepudo eliminar", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
