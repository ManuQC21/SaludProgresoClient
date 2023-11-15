package com.upao.activity.ui.citas;

import android.app.AlertDialog;
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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upao.R;
import com.upao.adapter.AgregarCitasAdapter;
import com.upao.entity.service.Citas;
import com.upao.entity.service.DatosCitaSeleccionada;
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
            Toast.makeText(getContext(), "Por favor, seleccione una hora para la cita.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String usuarioJson = sp.getString("UsuarioJson", null);
        if (usuarioJson == null) {
            Toast.makeText(getContext(), "Información del usuario no disponible.", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new GsonBuilder().create();
        Usuario u = gson.fromJson(usuarioJson, Usuario.class);
        Log.d("Medico id:", String.valueOf(seleccion.getMedico().getId()));
        Citas nuevaCita = new Citas();
        nuevaCita.setPaciente(new Paciente(u.getPaciente().getId())); // Asignar paciente basado en el usuario actual
        nuevaCita.setMedico(new Medico(seleccion.getMedico().getId())); // Asignar médico basado en la selección
        nuevaCita.setFechaCita(new FechasCitas(seleccion.getFechaCita().getId())); // Asignar fecha basada en la selección
        nuevaCita.setHoraCita(new HorasCitas(seleccion.getHoraCita().getId())); // Asignar hora basada en la selección

        // Enviar la cita para guardarla
        citasViewModel.guardarCita(nuevaCita).observe(getViewLifecycleOwner(), response -> {
            mostrarDialogoConfirmacion();
        });

    }
    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(getContext())
                .setTitle("Cita Guardada")
                .setMessage("La cita ha sido guardada correctamente.")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    cerrarFragmento();
                })
                .show();
    }
    private void cerrarFragmento() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

}
