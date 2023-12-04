package com.upao.activity.ui.MisCitas;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.upao.R;
import com.upao.adapter.AplazarCitasAdapter;
import com.upao.entity.service.Agenda_Medica;
import com.upao.viewmodel.CitasViewModel;
import com.upao.viewmodel.MedicoViewModel;
import java.util.ArrayList;
import java.util.List;

public class AplazarCitasActivity extends AppCompatActivity {

    private AutoCompleteTextView dropdownFecha, dropdownEspecialidad;
    private CitasViewModel citasViewModel;
    private MedicoViewModel medicoViewModel;
    private RecyclerView recyclerViewCitas;
    private AplazarCitasAdapter aplazarCitasAdapter;
    private Button btnConfirmarAplazamiento;

    private Long citaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplazar_cita);


        // Obtener el citaId del Intent
        Intent intent = getIntent();
        citaId = intent.getLongExtra("citaId", -1); // El valor por defecto es -1 para indicar que no se pasó un ID válido
        Log.e("EL id de la cita es: ",citaId.toString());
        initViews();
        initViewModels();
        setUpRecyclerView();
        cargarFechasDisponibles();
        setupDropdownListeners();

        btnConfirmarAplazamiento = findViewById(R.id.btnConfirmarAplazamiento);
        btnConfirmarAplazamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarAplazamiento();
            }
        });
    }

    private void initViews() {
        dropdownFecha = findViewById(R.id.dropdownFecha);
        recyclerViewCitas = findViewById(R.id.recyclerViewFechasDisponibles);
    }

    private void initViewModels() {
        citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class);
        medicoViewModel = new ViewModelProvider(this).get(MedicoViewModel.class);
    }

    private void setUpRecyclerView() {
        aplazarCitasAdapter = new AplazarCitasAdapter(this,new ArrayList<>());
        recyclerViewCitas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCitas.setAdapter(aplazarCitasAdapter);
    }

    private void cargarFechasDisponibles() {
        citasViewModel.buscarFechasDisponibles().observe(this, genericResponse -> {
            if (genericResponse != null && genericResponse.getRpta() == 1) {
                List<String> fechasDisponibles = genericResponse.getBody();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, fechasDisponibles);
                dropdownFecha.setAdapter(adapter);
            } else {
                // Manejo de errores, por ejemplo, mostrar un mensaje si no hay fechas disponibles o si hay un error
                // Puedes usar un Toast o un Snackbar para informar al usuario
                String mensajeError = genericResponse != null ? genericResponse.getMessage() : "Error al cargar fechas";
                Toast.makeText(this, mensajeError, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setupDropdownListeners() {
        dropdownFecha.setOnItemClickListener((parent, view, position, id) -> actualizarCitas());
    }

    private void actualizarCitas() {
        String fechaSeleccionada = dropdownFecha.getText().toString();

        if (!fechaSeleccionada.isEmpty() && citaId != -1) {
            // Primero obtener la especialidad de la cita
            citasViewModel.buscarEspecialidadPorId(citaId).observe(this, responseEspecialidad -> {
                if (responseEspecialidad != null && responseEspecialidad.getRpta() == 1) {
                    String especialidadObtenida = responseEspecialidad.getBody();

                    // Ahora obtener citas disponibles para la fecha y especialidad obtenida
                    citasViewModel.obtenerCitasPorFechaYEspecialidad(fechaSeleccionada, especialidadObtenida)
                            .observe(this, genericResponse -> {
                                if (genericResponse != null && genericResponse.getRpta() == 1) {
                                    List<Agenda_Medica> disponibilidadMedicos = genericResponse.getBody();
                                    aplazarCitasAdapter.setDisponibilidadList(disponibilidadMedicos);
                                } else {
                                    Toast.makeText(AplazarCitasActivity.this, "Lo sentimos no hay horarios disponibles para esta fecha.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(AplazarCitasActivity.this, "Error al obtener la especialidad de la cita.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(AplazarCitasActivity.this, "Por favor, seleccione una fecha.", Toast.LENGTH_SHORT).show();
        }
    }


    private void mostrarMensaje(String titulo, String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void mostrarMensajeYCerrar(String titulo, String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    finish(); // Cierra la actividad actual
                })
                .show();
    }
    private void confirmarAplazamiento() {
        String nuevaFecha = dropdownFecha.getText().toString();
        String nuevaHora = aplazarCitasAdapter.getHoraSeleccionada();

        if (nuevaFecha.isEmpty() || nuevaHora == null) {
            mostrarMensaje("Atención", "Por favor, seleccione una fecha y hora.");
            return;
        }

        // Resto del código...
        citasViewModel.aplazarCita(citaId, nuevaFecha, nuevaHora).observe(this, respuesta -> {
            if (respuesta.getRpta() == 1) {
                mostrarMensajeYCerrar("Éxito", "Cita aplazada con éxito.");
            } else {
                mostrarMensaje("Error", "Error al aplazar la cita.");
            }
        });
    }
}
