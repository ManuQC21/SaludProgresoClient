package com.upao.activity.ui.MisCitas;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upao.R;
import com.upao.adapter.CitasVigentesAdapter;
import com.upao.entity.service.Usuario;
import com.upao.viewmodel.CitasViewModel;

public class CitasVigentesFragment extends Fragment {

    private RecyclerView recyclerView;
    private CitasViewModel citasViewModel;
    private TextView textViewCitaInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_citas_vigentes, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCitasVigentes);
        textViewCitaInfo = view.findViewById(R.id.txtCitasVigentesInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        citasViewModel = new ViewModelProvider(this).get(CitasViewModel.class);
        CitasVigentesAdapter citasAdapter = new CitasVigentesAdapter(citasViewModel);
        recyclerView.setAdapter(citasAdapter);

        loadData(citasAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData((CitasVigentesAdapter) recyclerView.getAdapter());
    }

    private void loadData(CitasVigentesAdapter citasAdapter) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String usuarioJson = sp.getString("UsuarioJson", null);

        if (usuarioJson != null) {
            Gson gson = new GsonBuilder().create();
            Usuario u = gson.fromJson(usuarioJson, Usuario.class);
            citasViewModel.buscarCitasVigentes().observe(getViewLifecycleOwner(), response -> {
                if (response != null) {
                    if (response.getRpta() == 1) {
                        citasAdapter.updateCitas(response.getBody());
                        if (response.getBody().isEmpty()) {
                            mostrarMensajeError("No tiene citas vigentes.");
                        }
                    } else {
                        mostrarMensajeError("Error al cargar citas: " + response.getMessage());
                    }
                } else {
                    mostrarMensajeError("Error al cargar citas: respuesta nula del servidor.");
                }
            });
        } else {
            mostrarMensajeError("Información de usuario no disponible.");
        }
    }

    private void mostrarMensajeError(String mensaje) {
        if (textViewCitaInfo != null) {
            textViewCitaInfo.setVisibility(View.VISIBLE);
            textViewCitaInfo.setText(mensaje);
        }
    }
}
