package com.upao.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.upao.R;
import com.upao.activity.ui.MisCitas.AplazarCitasActivity;
import com.upao.api.ConfigApi;
import com.upao.entity.service.Citas;
import com.upao.viewmodel.CitasViewModel;
import java.util.ArrayList;
import java.util.List;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.CitasViewHolder> {

    private List<Citas> listaCitas;
    private CitasViewModel citasViewModel;

    public CitasAdapter(CitasViewModel citasViewModel) {
        this.listaCitas = new ArrayList<>();
        this.citasViewModel = citasViewModel;
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

    public void updateCitas(List<Citas> nuevasCitas) {
        this.listaCitas = nuevasCitas;
        notifyDataSetChanged();
    }

    public class CitasViewHolder extends RecyclerView.ViewHolder {
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
            doctorNameTextView.setText(cita.getMedico().getNombreMedico());
            doctorSpecialtyTextView.setText(cita.getMedico().getEspecialidad());
            appointmentDateTextView.setText(cita.getFechaCita().getFecha());
            appointmentTimeTextView.setText(cita.getHoraCita().getHora());

            String imageUrl = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + cita.getMedico().getFoto().getFileName();
            Picasso.get().load(imageUrl).error(R.drawable.image_not_found).into(doctorImageView);

            btnAplazar.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), AplazarCitasActivity.class);
                intent.putExtra("citaId", cita.getId());
                itemView.getContext().startActivity(intent);
            });

            btnEliminar.setOnClickListener(v -> mostrarDialogoConfirmacion(cita));

        }
        private void mostrarDialogoConfirmacion(Citas cita) {
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Estás seguro de que deseas eliminar esta cita?")
                    .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            eliminarCita(cita);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        private void eliminarCita(Citas cita) {
            citasViewModel.eliminarCita(cita.getId()).observe((LifecycleOwner) itemView.getContext(), response -> {
                if (response.getRpta() == 1) {
                    // Actualizar lista para reflejar la eliminación
                    listaCitas.remove(cita);
                    notifyDataSetChanged();
                    mostrarMensajeEliminacionExitosa();
                } else {
                    // Mostrar mensaje de error
                }
            });
        }
        private void mostrarMensajeEliminacionExitosa() {
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle("¡Cita eliminada!")
                    .setMessage("La cita ha sido eliminada con éxito.")
                    .setPositiveButton("OK", null)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }
    }
}