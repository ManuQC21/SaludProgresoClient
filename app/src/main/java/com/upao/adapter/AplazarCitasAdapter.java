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
import com.upao.entity.service.DisponibilidadMedico;
import java.util.List;

public class AplazarCitasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TIPO_NORMAL = 0;
    private static final int TIPO_SIN_CITAS = 1;
    private List<DisponibilidadMedico> disponibilidadList;

    private Context context;
    private String horaSeleccionada;

    private Long idMedicoSeleccionado;

    private String especialidad;

    public AplazarCitasAdapter(Context context, List<DisponibilidadMedico> disponibilidadList) {
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
            return new SinCitasViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor_hora, parent, false);
            return new AplazarCitasViewHolder(itemView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TIPO_NORMAL) {
            DisponibilidadMedico disponibilidadMedico = disponibilidadList.get(position);
            AplazarCitasViewHolder viewHolder = (AplazarCitasViewHolder) holder;
            viewHolder.textViewNombreDoctor.setText(disponibilidadMedico.getMedico().getNombreMedico());
            viewHolder.textViewEspecialidadDoctor.setText(disponibilidadMedico.getMedico().getEspecialidad());
            viewHolder.buttonHoraCita.setText(disponibilidadMedico.getHoraCita().getHora());

            String imageUrl = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + disponibilidadMedico.getMedico().getFoto().getFileName();
            Picasso.get().load(imageUrl).error(R.drawable.image_not_found).into(viewHolder.imageViewDoctor);
            viewHolder.buttonHoraCita.setOnClickListener(v -> {
                String horaSeleccionadaTemp = disponibilidadMedico.getHoraCita().getHora();
                idMedicoSeleccionado = Long.valueOf(disponibilidadMedico.getMedico().getId()); // Guardar ID del médico
                especialidad = disponibilidadMedico.getMedico().getEspecialidad();
                mostrarDialogoConfirmacion(horaSeleccionadaTemp, viewHolder.buttonHoraCita);
            });
        }
    }
    @Override
    public int getItemCount() {
        return disponibilidadList.isEmpty() ? 1 : disponibilidadList.size();
    }

    public void setDisponibilidadList(List<DisponibilidadMedico> disponibilidadList) {
        this.disponibilidadList = disponibilidadList;
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

    private void mostrarDialogoConfirmacion(String hora, Button buttonHoraCita) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmar selección de hora");
        builder.setMessage("Usted seleccionó la hora: " + hora);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                horaSeleccionada = hora;
                Log.d("Nueva hora es: ",horaSeleccionada);
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

    public Long idDelMedico(){
        return idMedicoSeleccionado;
    }

    public String getEspecialidad(){
        return especialidad;
    }
}
