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

public class AplazarCitasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TIPO_NORMAL = 0;
    private static final int TIPO_SIN_CITAS = 1;
    private List<Citas> citasList;

    public AplazarCitasAdapter(List<Citas> citasList) {
        this.citasList = citasList;
    }

    @Override
    public int getItemViewType(int position) {
        if (citasList.isEmpty()) {
            return TIPO_SIN_CITAS;
        }
        return TIPO_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TIPO_SIN_CITAS) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_citas, parent, false);
            return new SinCitasViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_hora, parent, false);
            return new AplazarCitasViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TIPO_NORMAL) {
            Citas cita = citasList.get(position);
            AplazarCitasViewHolder viewHolder = (AplazarCitasViewHolder) holder;
            viewHolder.textViewNombreDoctor.setText(cita.getMedico().getNombreMedico());
            viewHolder.textViewEspecialidadDoctor.setText(cita.getMedico().getEspecialidad());
            viewHolder.buttonHoraCita.setText(cita.getHoraCita().getHora());

            String imageUrl = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + cita.getMedico().getFoto().getFileName();
            Picasso.get().load(imageUrl).error(R.drawable.image_not_found).into(viewHolder.imageViewDoctor);
        }
    }

    @Override
    public int getItemCount() {
        return citasList.isEmpty() ? 1 : citasList.size();
    }

    public void setCitasList(List<Citas> citasList) {
        this.citasList = citasList;
        notifyDataSetChanged();
    }

    public static class AplazarCitasViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewDoctor;
        TextView textViewNombreDoctor, textViewEspecialidadDoctor;
        Button buttonHoraCita;

        public AplazarCitasViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewDoctor = itemView.findViewById(R.id.imageViewDoctor);
            textViewNombreDoctor = itemView.findViewById(R.id.textViewNombreDoctor);
            textViewEspecialidadDoctor = itemView.findViewById(R.id.textViewEspecialidadDoctor);
            buttonHoraCita = itemView.findViewById(R.id.buttonHoraCita);
        }
    }

    public static class SinCitasViewHolder extends RecyclerView.ViewHolder {
        public SinCitasViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
