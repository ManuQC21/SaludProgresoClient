package com.upao.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
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
import com.upao.entity.service.DatosCitaSeleccionada;
import com.upao.entity.service.Agenda_Medica;
import java.util.List;

public class AgregarCitasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TIPO_NORMAL = 0;
    private static final int TIPO_SIN_CITAS = 1;
    private List<Agenda_Medica> disponibilidadList;

    private Context context;
    private String horaSeleccionada;
    private Agenda_Medica seleccionActual;


    public AgregarCitasAdapter(Context context, List<Agenda_Medica> disponibilidadList) {
        this.disponibilidadList = disponibilidadList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (disponibilidadList.isEmpty()) {
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
            return new AgregarCitasAdapter.SinCitasViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_hora, parent, false);
            return new AgregarCitasAdapter.AgregarCitasViewHolder(itemView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TIPO_NORMAL) {
            Agenda_Medica disponibilidadMedico = disponibilidadList.get(position);
            AgregarCitasAdapter.AgregarCitasViewHolder viewHolder = (AgregarCitasAdapter.AgregarCitasViewHolder) holder;
            viewHolder.textViewNombreDoctor.setText(disponibilidadMedico.getMedico().getNombreMedico());
            viewHolder.textViewEspecialidadDoctor.setText(disponibilidadMedico.getMedico().getEspecialidad());
            viewHolder.buttonHoraCita.setText(disponibilidadMedico.getHoraCita().getHora());

            String imageUrl = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + disponibilidadMedico.getMedico().getFoto().getFileName();
            Picasso.get().load(imageUrl).error(R.drawable.image_not_found).into(viewHolder.imageViewDoctor);
            viewHolder.buttonHoraCita.setOnClickListener(v -> {
                mostrarDialogoConfirmacion(disponibilidadMedico, viewHolder.buttonHoraCita);
            });
        }
    }
    @Override
    public int getItemCount() {
        return disponibilidadList.isEmpty() ? 1 : disponibilidadList.size();
    }

    public void setDisponibilidadList(List<Agenda_Medica> disponibilidadList) {
        this.disponibilidadList = disponibilidadList;
        notifyDataSetChanged();
    }

    public static class AgregarCitasViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewDoctor;
        TextView textViewNombreDoctor, textViewEspecialidadDoctor;
        Button buttonHoraCita;

        public AgregarCitasViewHolder(@NonNull View itemView) {
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

    private void mostrarDialogoConfirmacion(Agenda_Medica disponibilidadMedico, Button buttonHoraCita) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmar selección de hora");
        builder.setMessage("Usted seleccionó la hora: " + disponibilidadMedico.getHoraCita().getHora());
        horaSeleccionada = disponibilidadMedico.getHoraCita().getHora();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("se selecciono",horaSeleccionada);
                seleccionActual = disponibilidadMedico;
                buttonHoraCita.setBackgroundColor(Color.GRAY);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                horaSeleccionada = null;
                buttonHoraCita.setBackgroundColor(Color.WHITE);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public String getHoraSeleccionada() {
        return horaSeleccionada;
    }
    public Agenda_Medica getSeleccionActual() {
        return seleccionActual;
    }

    public DatosCitaSeleccionada obtenerDatosCitaSeleccionada(int position) {
        Agenda_Medica disponibilidadMedico = disponibilidadList.get(position);
        int idMedico = disponibilidadMedico.getMedico().getId();
        long idFecha = disponibilidadMedico.getFechaCita().getId();
        long idHora = disponibilidadMedico.getHoraCita().getId();

        return new DatosCitaSeleccionada(idMedico, idFecha, idHora);
    }

}
