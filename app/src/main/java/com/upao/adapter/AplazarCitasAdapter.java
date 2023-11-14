package com.upao.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.upao.R;
import com.upao.api.ConfigApi;
import com.upao.entity.service.Citas;
import java.util.List;

public class AplazarCitasAdapter extends RecyclerView.Adapter<AplazarCitasAdapter.AplazarCitasViewHolder> {

    private List<Citas> citasList;

    public AplazarCitasAdapter(List<Citas> citasList) {
        this.citasList = citasList;
    }

    @NonNull
    @Override
    public AplazarCitasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_hora, parent, false);
        return new AplazarCitasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AplazarCitasViewHolder holder, int position) {
        Citas cita = citasList.get(position);
        holder.textViewNombreDoctor.setText(cita.getMedico().getNombreMedico());
        holder.textViewEspecialidadDoctor.setText(cita.getMedico().getEspecialidad());
        holder.buttonHoraCita.setText(cita.getHoraCita().getHora());

        // Utiliza Picasso para cargar imágenes desde una URL
        // Asegúrate de tener la URL correcta para la imagen del doctor
        String imageUrl = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + cita.getMedico().getFoto().getFileName();
        Picasso.get().load(imageUrl).error(R.drawable.image_not_found).into(holder.imageViewDoctor);
    }

    @Override
    public int getItemCount() {
        return citasList.size();
    }
    public void setCitasList(List<Citas> citasList) {
        this.citasList = citasList;
        notifyDataSetChanged();
    }
    public static class AplazarCitasViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewDoctor;
        TextView textViewNombreDoctor;
        TextView textViewEspecialidadDoctor;
        Button buttonHoraCita;

        public AplazarCitasViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewDoctor = itemView.findViewById(R.id.imageViewDoctor);
            textViewNombreDoctor = itemView.findViewById(R.id.textViewNombreDoctor);
            textViewEspecialidadDoctor = itemView.findViewById(R.id.textViewEspecialidadDoctor);
            buttonHoraCita = itemView.findViewById(R.id.buttonHoraCita);
        }
    }
}
