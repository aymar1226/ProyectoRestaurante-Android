package com.example.proyectorestaurante.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectorestaurante.models.Foro;
import com.example.proyectorestaurante.R;

import java.util.List;

public class ForoAdapter extends RecyclerView.Adapter<ForoAdapter.ForoViewHolder> {

    private List<Foro> listaForos;
    private OnItemClickListener onItemClickListener;


    public ForoAdapter(List<Foro> listaForos) {
        this.listaForos = listaForos;
    }

    @NonNull
    @Override
    public ForoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foro, parent, false);
        return new ForoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForoViewHolder holder, int position) {
        Foro foro = listaForos.get(position);
        holder.tvNombreForo.setText(foro.getNombre_foro());
        holder.tvDescripcionForo.setText(foro.getDescripcion());
        holder.tvFechaForo.setText("Fecha de creación: " + foro.getFecha_creacion());

        // Configurar el clic en cada item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(foro); // Llamada a la interfaz de clic
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaForos.size();
    }

    public static class ForoViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreForo, tvDescripcionForo, tvFechaForo;

        public ForoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreForo = itemView.findViewById(R.id.tvNombreForo);
            tvDescripcionForo = itemView.findViewById(R.id.tvDescripcionForo);
            tvFechaForo = itemView.findViewById(R.id.tvFechaForo);
        }
    }

    // Interfaz para el evento de clic en cada item
    public interface OnItemClickListener {
        void onItemClick(Foro foro);
    }
}
