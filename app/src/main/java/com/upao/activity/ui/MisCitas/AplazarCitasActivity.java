package com.upao.activity.ui.MisCitas;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upao.R;
import com.upao.adapter.AplazarCitasAdapter;
import com.upao.entity.GenericResponse;
import com.upao.entity.service.Citas;
import com.upao.entity.service.DisponibilidadMedico;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplazar_cita);

        initViews();
        initViewModels();
        setUpRecyclerView();
        cargarEspecialidades();
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
        dropdownEspecialidad = findViewById(R.id.dropdownEspecialidad);
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

    private void cargarEspecialidades() {
        medicoViewModel.getEspecialidades().observe(this, genericResponse -> {
            if (genericResponse != null && genericResponse.getRpta() == 1) {
                List<String> especialidades = genericResponse.getBody();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, especialidades);
                dropdownEspecialidad.setAdapter(adapter);
            } else {
                // Manejo de errores, por ejemplo, mostrar un mensaje si no hay especialidades disponibles o si hay un error
                String mensajeError = genericResponse != null ? genericResponse.getMessage() : "Error al cargar especialidades";
                Toast.makeText(this, mensajeError, Toast.LENGTH_LONG).show();
            }
        });
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
        dropdownEspecialidad.setOnItemClickListener((parent, view, position, id) -> actualizarCitas());
    }

    private void actualizarCitas() {
        String fechaSeleccionada = dropdownFecha.getText().toString();
        String especialidadSeleccionada = dropdownEspecialidad.getText().toString();

        if (!fechaSeleccionada.isEmpty() && !especialidadSeleccionada.isEmpty()) {
            citasViewModel.obtenerCitasPorFechaYEspecialidad(fechaSeleccionada, especialidadSeleccionada)
                    .observe(this, genericResponse -> {
                        System.err.println("Error en agregarFechaConHoras: " + fechaSeleccionada + especialidadSeleccionada);
                        if (genericResponse != null && genericResponse.getRpta() == 1) {
                            // Suponiendo que tu adaptador pueda manejar una lista de DisponibilidadMedico
                            List<DisponibilidadMedico> disponibilidadMedicos = genericResponse.getBody();
                            aplazarCitasAdapter.setDisponibilidadList(disponibilidadMedicos);
                        } else {
                            Toast.makeText(AplazarCitasActivity.this, "No hay doctores disponibles en esta fecha y especialidad.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void confirmarAplazamiento() {
        String nuevaFecha = dropdownFecha.getText().toString();
        String nuevaHora = aplazarCitasAdapter.getHoraSeleccionada();

        if (nuevaFecha.isEmpty() || nuevaHora == null) {
            Toast.makeText(this, "Por favor, seleccione una fecha y hora.", Toast.LENGTH_LONG).show();
            return;
        }

        // Aquí debes obtener el ID de la cita que quieres aplazar.
        // Este es un ejemplo, debes reemplazarlo con el ID real de tu cita.
        Long citaId = 1L;

        citasViewModel.aplazarCita(citaId, nuevaFecha, nuevaHora).observe(this, new Observer<GenericResponse<Citas>>() {
            @Override
            public void onChanged(GenericResponse<Citas> respuesta) {
                if (respuesta.getRpta() == 1) {
                    Toast.makeText(AplazarCitasActivity.this, "Cita aplazada con éxito.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AplazarCitasActivity.this, "Error al aplazar la cita.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
