package com.upao.activity.ui.citas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upao.R;
import com.upao.adapter.AgregarCitasAdapter;
import com.upao.entity.service.Citas;
import com.upao.entity.service.DisponibilidadMedico;
import com.upao.entity.service.FechasCitas;
import com.upao.entity.service.HorasCitas;
import com.upao.entity.service.Medico;
import com.upao.entity.service.Paciente;
import com.upao.entity.service.Usuario;
import com.upao.viewmodel.CitasViewModel;

import java.util.ArrayList;
import java.util.List;

public class CitasFragment extends Fragment {

    private AutoCompleteTextView dropdownFecha;
    private CitasViewModel citasViewModel;
    private RecyclerView recyclerViewCitas;
    private AgregarCitasAdapter agregarCitasAdapter;
    private Button btnGuardarCita;

    private CitasActivity citasActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guardar_cita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initViewModels();
        setUpRecyclerView();
        cargarFechasDisponibles();
        setupDropdownListeners();
        //se agrego:
        btnGuardarCita = view.findViewById(R.id.btnGuardarCita);

        btnGuardarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCita();
            }
        });
    }

    private void initViews(View view) {
        dropdownFecha = view.findViewById(R.id.dropdownFecha);
        recyclerViewCitas = view.findViewById(R.id.recyclerViewFechasDisponibles);
    }

    private void initViewModels() {
        citasViewModel = new ViewModelProvider(requireActivity()).get(CitasViewModel.class);
    }

    private void setUpRecyclerView() {
        agregarCitasAdapter = new AgregarCitasAdapter(requireContext(), new ArrayList<>());
        recyclerViewCitas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCitas.setAdapter(agregarCitasAdapter);
    }

    private void cargarFechasDisponibles() {
        citasViewModel.buscarFechasDisponibles().observe(getViewLifecycleOwner(), genericResponse -> {
            if (genericResponse != null && genericResponse.getRpta() == 1) {
                List<String> fechasDisponibles = genericResponse.getBody();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, fechasDisponibles);
                dropdownFecha.setAdapter(adapter);
            } else {
                String mensajeError = genericResponse != null ? genericResponse.getMessage() : "Error al cargar fechas";
                Toast.makeText(requireContext(), mensajeError, Toast.LENGTH_LONG).show();
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
                    .observe(getViewLifecycleOwner(), genericResponse -> {
                        if (genericResponse != null && genericResponse.getRpta() == 1) {
                            List<DisponibilidadMedico> disponibilidadMedicos = genericResponse.getBody();
                            agregarCitasAdapter.setDisponibilidadList(disponibilidadMedicos);
                        } else {
                            Toast.makeText(requireContext(), "No hay Citas Disponibles para esta fecha.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    //se agrego:
    private void guardarCita() {
        DisponibilidadMedico seleccion = agregarCitasAdapter.getSeleccionActual();
        if (seleccion == null || seleccion.getMedico() == null || seleccion.getHoraCita() == null || seleccion.getFechaCita() == null) {
            Toast.makeText(getContext(), "Por favor, seleccione una hora y fecha para la cita.", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String usuarioJson = sp.getString("UsuarioJson", null);
        Gson gson = new GsonBuilder().create();
        Usuario u = gson.fromJson(usuarioJson, Usuario.class);
        // Obtener el ID del paciente de SharedPreferences
        int pacienteId = u.getPaciente().getId();
        Log.d("Id DEL USUARIO", String.valueOf(pacienteId));
        Citas nuevaCita = new Citas();
        nuevaCita.setPaciente(new Paciente(pacienteId));

        // Crear el objeto Citas
        String fechaseleccionada = dropdownFecha.getText().toString();
        Log.d("fecha seleccionada :",fechaseleccionada);


        String horaseleccionada = agregarCitasAdapter.getHoraSeleccionada().toString();
        Log.d("hora seleccionada :",horaseleccionada);

        DisponibilidadMedico disponibilidadMedico = agregarCitasAdapter.datos(1);
        disponibilidadMedico.g

        nuevaCita.setFechaCita(new FechasCitas(seleccion.getFechaCita().getId()));
        nuevaCita.setHoraCita(new HorasCitas(seleccion.getHoraCita().getId()));
        nuevaCita.setMedico(new Medico(seleccion.getMedico().getId()));


        // Enviar la cita para guardarla
        citasViewModel.guardarCita(nuevaCita).observe(getViewLifecycleOwner(), response -> {
            if (response != null && response.getRpta() == 1) {
                Toast.makeText(getContext(), "Cita guardada con éxito.", Toast.LENGTH_SHORT).show();
            } else {
                String mensajeError = response != null ? response.getMessage() : "Error al guardar la cita";
                Toast.makeText(getContext(), mensajeError, Toast.LENGTH_SHORT).show();
            }
        });
    }




}
