package com.upao.activity.ui.citas;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upao.R;
import com.upao.adapter.AgregarCitasAdapter;
import com.upao.entity.service.DisponibilidadMedico;
import com.upao.viewmodel.CitasViewModel;

import java.util.ArrayList;
import java.util.List;

public class CitasActivity extends AppCompatActivity {

    private AutoCompleteTextView dropdownFecha;
    private CitasViewModel citasViewModel;
    private RecyclerView recyclerViewCitas;
    private AgregarCitasAdapter agregarCitasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar_cita);

        initViews();
        initViewModels();
        setUpRecyclerView();
        cargarFechasDisponibles();
        setupDropdownListeners();
    }

    private void initViews() {
        dropdownFecha = findViewById(R.id.dropdownFecha);
        recyclerViewCitas = findViewById(R.id.recyclerViewFechasDisponibles);
    }

    private void initViewModels() {
        citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class);
    }

    private void setUpRecyclerView() {
        agregarCitasAdapter = new AgregarCitasAdapter(this, new ArrayList<>());
        recyclerViewCitas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCitas.setAdapter(agregarCitasAdapter);
    }

    private void cargarFechasDisponibles() {
        citasViewModel.buscarFechasDisponibles().observe(this, genericResponse -> {
            if (genericResponse != null && genericResponse.getRpta() == 1) {
                List<String> fechasDisponibles = genericResponse.getBody();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, fechasDisponibles);
                dropdownFecha.setAdapter(adapter);
            } else {
                String mensajeError = genericResponse != null ? genericResponse.getMessage() : "Error al cargar fechas";
                Toast.makeText(CitasActivity.this, mensajeError, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupDropdownListeners() {
        dropdownFecha.setOnItemClickListener((parent, view, position, id) -> actualizarCitas());
    }

    private void actualizarCitas() {
        String fechaSeleccionada = dropdownFecha.getText().toString();

        if (!fechaSeleccionada.isEmpty()) {
            citasViewModel.obtenerCitasDisponibles(fechaSeleccionada)
                    .observe(this, genericResponse -> {
                        if (genericResponse != null && genericResponse.getRpta() == 1) {
                            List<DisponibilidadMedico> disponibilidadMedicos = genericResponse.getBody();
                            agregarCitasAdapter.setDisponibilidadList(disponibilidadMedicos);
                        } else {
                            Toast.makeText(CitasActivity.this, "No hay Citas Disponibles.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}
