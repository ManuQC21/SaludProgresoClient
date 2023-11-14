package com.upao.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.upao.R;
import com.upao.activity.ui.MisCitas.AplazarCitasActivity;
import com.upao.api.ConfigApi;
import com.upao.entity.service.Citas;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.CitasViewHolder> {

    private List<Citas> listaCitas;

    // Constructor vacío inicializa con lista vacía
    public CitasAdapter() {
        this.listaCitas = new ArrayList<>();
    }

    @NonNull
    @Override
    public CitasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mis_citas, parent, false);
        return new CitasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitasViewHolder holder, int position) {
        Citas cita = listaCitas.get(position);
        holder.bind(cita);
    }

    @Override
    public int getItemCount() {
        return listaCitas.size();
    }

    // Actualiza la lista de citas y notifica al adaptador
    public void updateCitas(List<Citas> nuevasCitas) {
        listaCitas = nuevasCitas;
        notifyDataSetChanged();
    }

    // ViewHolder para la vista de cada ítem en el RecyclerView
    public static class CitasViewHolder extends RecyclerView.ViewHolder {
        private ImageView doctorImageView;
        private TextView doctorNameTextView, doctorSpecialtyTextView, appointmentDateTextView, appointmentTimeTextView;

        private Button btnAplazar, btnEliminar;

        public CitasViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorImageView = itemView.findViewById(R.id.doctorImageView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            doctorSpecialtyTextView = itemView.findViewById(R.id.doctorSpecialtyTextView);
            appointmentDateTextView = itemView.findViewById(R.id.appointmentDateTextView);
            appointmentTimeTextView = itemView.findViewById(R.id.appointmentTimeTextView);
            btnAplazar = itemView.findViewById(R.id.btnAplazar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        void bind(Citas cita) {
            // Aquí se establecen los datos de la cita en las vistas
            doctorNameTextView.setText(cita.getMedico().getNombreMedico());
            doctorSpecialtyTextView.setText(cita.getMedico().getEspecialidad());
            appointmentDateTextView.setText(cita.getFechaCita().getFecha());
            appointmentTimeTextView.setText(cita.getHoraCita().getHora());

            // Carga la imagen del médico usando Picasso
            String imageUrl = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + cita.getMedico().getFoto().getFileName();
            Picasso.get().load(imageUrl).error(R.drawable.image_not_found).into(doctorImageView);

            // Si necesitas manejar clics en elementos de la lista, puedes hacerlo aquí.
            btnAplazar.setOnClickListener(v -> {
                // Aquí inicias la actividad AplazarCitasActivity
                Intent intent = new Intent(itemView.getContext(), AplazarCitasActivity.class);
                intent.putExtra("cita_id", cita.getId());
                itemView.getContext().startActivity(intent);
            });

            btnEliminar.setOnClickListener(v -> {
                // Aquí puedes manejar la eliminación de la cita
                Toast.makeText(itemView.getContext(), "Se hizo prueba, se eliminó", Toast.LENGTH_SHORT).show();
                // Además, aquí podrías agregar la lógica para eliminar la cita de la base de datos o del servicio que estés utilizando.
            });

        }
    }
}
