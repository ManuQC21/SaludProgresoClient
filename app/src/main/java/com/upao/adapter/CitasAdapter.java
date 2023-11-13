package com.upao.adapter;
/*
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.upao.R;
import com.upao.api.ConfigApi;
import com.upao.entity.service.Citas;
import com.upao.entity.service.DocumentoAlmacenado;
import com.upao.entity.service.Medico;
import com.upao.entity.service.Paciente;
import com.upao.entity.service.dto.CitaConDetalleDTO;
import com.upao.viewmodel.CitasViewModel;

import java.lang.reflect.Field;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.ViewHolder> {
   private List<CitaConDetalleDTO> c;

    private CitasViewModel citasViewModel;
    private SharedPreferences sharedPreferences;

    public CitasAdapter(List<CitaConDetalleDTO> c, CitasViewModel citasViewModel, SharedPreferences sharedPreferences) {
        this.c = c;
        this.citasViewModel = citasViewModel;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mis_citas, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(this.c.get(position));
    }

    @Override
    public int getItemCount() {
        return this.c.size();
    }

    public void updateItems(List<CitaConDetalleDTO> citas) {
        this.c.clear();
        this.c.addAll(citas);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView doctorImageView;
        private TextView doctorNameTextView, doctorSpecialtyTextView, citaDateTextView, citaHoraTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.doctorImageView = itemView.findViewById(R.id.doctorImageView);
            this.doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            this.doctorSpecialtyTextView = itemView.findViewById(R.id.doctorSpecialtyTextView);
            this.citaDateTextView = itemView.findViewById(R.id.citaDateTextView);
            this.citaHoraTextView = itemView.findViewById(R.id.citaHoraTextView);
        }

        public void setItem(final CitaConDetalleDTO c) {
            String foto = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + c.getCitas().getMedico().getFoto().getFileName();
            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(foto)
                    .error(R.drawable.image_not_found)
                    .into(doctorImageView);
            Citas citas = c.getCitas();
            String nombreMedico = citas.getMedico().getNombreMedico();
            String especialidad = citas.getAreaEspecialidad();
            Date fechaCita = citas.getFechaCita();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String fechaFormateada = sdf.format(fechaCita);
            String horaCita = citas.getLashoras();
            // Actualiza las vistas con los datos obtenidos
            doctorNameTextView.setText(nombreMedico != null ? nombreMedico : "No se encuentra disponible");
            doctorSpecialtyTextView.setText(especialidad != null ? especialidad : "No se encuentra disponible");
            citaDateTextView.setText(fechaFormateada  != null ? fechaFormateada  : "No se encuentra disponible");
            citaHoraTextView.setText(horaCita != null ? horaCita : "No se encuentra disponible");
            log(c);
        }

        public void log(CitaConDetalleDTO c){

            Paciente paciente = c.getCitas().getPaciente();
            Class<?> pacienteClass = paciente.getClass();
            Field[] fieldspaciente = pacienteClass.getDeclaredFields();

            for (Field field : fieldspaciente) {
                field.setAccessible(true); // Para acceder a campos privados si es necesario
                try {
                    String fieldName = field.getName();
                    Object value = field.get(paciente);
                    Log.d("Paciente Data", fieldName + ": " + value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }


            Medico medico = c.getCitas().getMedico();
            Class<?> medicoClass = medico.getClass();
            Field[] fieldsmedico = medicoClass.getDeclaredFields();

            for (Field field : fieldsmedico) {
                field.setAccessible(true); // Para acceder a campos privados si es necesario
                try {
                    String fieldName = field.getName();
                    Object value = field.get(medico);
                    Log.d("Medico Data", fieldName + ": " + value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }



            DocumentoAlmacenado medico1 = c.getCitas().getMedico().getFoto();
            Class<?> DocumentoClass = medico1.getClass();
            Field[] fieldsdocumento= DocumentoClass.getDeclaredFields();

            for (Field field : fieldsdocumento) {
                field.setAccessible(true); // Para acceder a campos privados si es necesario
                try {
                    String fieldName = field.getName();
                    Object value = field.get(medico1);
                    Log.d("Foto Medico Data", fieldName + ": " + value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            Citas citas = c.getCitas();
            Class<?> citasClass = citas.getClass();
            Field[] fields = citasClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true); // Para acceder a campos privados si es necesario
                try {
                    String fieldName = field.getName();
                    Object value = field.get(citas);
                    Log.d("Citas Data", fieldName + ": " + value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}*/
